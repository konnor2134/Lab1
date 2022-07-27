package ua.edu.sumdu.j2se.budko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.budko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.budko.tasks.model.Task;
import ua.edu.sumdu.j2se.budko.tasks.view.Notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NotificationThread extends Thread {
    private static final Logger LOG = Logger.getLogger(NotificationThread.class);

    private AbstractTaskList taskList;
    private Notification notification;
    private Task lastTask;

    public NotificationThread(AbstractTaskList taskList) {
        super("NotificationThread");
        this.taskList = taskList;

        LOG.info("Notification created.");
    }

    public void setTaskList(AbstractTaskList taskList) {
        this.taskList = taskList;
    }

    public void register(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void run() {
        final int timeNotification = 3600;
        ChronoUnit seconds = ChronoUnit.SECONDS;
        long sec;
        LocalDateTime time = null;
        while (true) {
            for (Task task: taskList) {
                try {
                    time = task.nextTimeAfter(LocalDateTime.now());
                }
                catch (NullPointerException e) {
                    LOG.error("Cannot be null",e);
                }
                if (time != null) {
                    sec = seconds.between(LocalDateTime.now(), time);
                    if (sec <= timeNotification) {
                        if (lastTask == null || time.minusSeconds(sec).isAfter(lastTask.getTime())) {
                            notification.display(sec, task.getTitle());
                            try {
                                lastTask = task.clone();
                            }
                            catch (CloneNotSupportedException e) {
                                LOG.error("Failed to clone task.");
                            }
                            LOG.info("The user is notified about " + task.getTitle());
                        }
                    }
                }
            }
        }
    }
}