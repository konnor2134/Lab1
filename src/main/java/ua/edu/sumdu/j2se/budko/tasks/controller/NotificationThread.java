package ua.edu.sumdu.j2se.budko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.budko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.budko.tasks.model.Task;
import ua.edu.sumdu.j2se.budko.tasks.view.Notification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationThread extends Thread {
    private static final Logger log = Logger.getLogger(Controller.class);

    private AbstractTaskList taskList;
    private Notification notification;
    private Task lastTask;

    public NotificationThread(AbstractTaskList taskList) {
        super("NotificationThread");
        this.taskList = taskList;

        log.info("Notification created.");
    }

    public void setTaskList(AbstractTaskList taskList) {
        this.taskList = taskList;
    }

    public void register(Notification notification) {
        this.notification = notification;
    }

    @Override
    public void run() {
        ChronoUnit seconds = ChronoUnit.SECONDS;
        long sec;
        LocalDateTime time = null;
        while (true) {
            for (Task task: taskList) {
                try {
                    time = task.nextTimeAfter(LocalDateTime.now());
                }
                catch (NullPointerException npe) {
                }
                if (time != null) {
                    sec = seconds.between(LocalDateTime.now(), time);
                    if (sec <= 10800) {
                        if (lastTask == null || time.minusSeconds(sec).isAfter(lastTask.getTime())) {
                            notification.display(sec, task.getTitle());
                            try {
                                lastTask = task.clone();
                            }
                            catch (CloneNotSupportedException e) {
                                log.error("Failed to clone task.");
                            }
                            log.info("The user is notified about " + task.getTitle());
                        }
                    }
                }
            }
        }
    }
}