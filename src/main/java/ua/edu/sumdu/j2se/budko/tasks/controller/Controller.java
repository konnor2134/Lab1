package ua.edu.sumdu.j2se.budko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.budko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.budko.tasks.model.Task;
import ua.edu.sumdu.j2se.budko.tasks.model.TaskIO;
import ua.edu.sumdu.j2se.budko.tasks.view.Notification;
import ua.edu.sumdu.j2se.budko.tasks.view.View;


import java.io.File;

public abstract class Controller {

    private static final Logger LOG = Logger.getLogger(Controller.class);
    protected View view;
    protected AbstractTaskList taskList;
    protected int action = -1;
    protected NotificationThread notificationThread;
    final String fileTask = "tasks.txt";
    public Controller(View view, AbstractTaskList taskList) {
        this.view = view;
        this.taskList = taskList;

        notificationThread = new NotificationThread(taskList);
        Notification notification = new Notification();
        notificationThread.register(notification);
        notificationThread.start();
    }

    public abstract void process(Functional actions);

    public void addTask() {
        try {
            Task task = view.addTask();
            if (task == null) {
                throw new NullPointerException("Cannot be null");
            }
            taskList.add(task);
            LOG.info("Task " + task.getTitle() + " added.");
        }
        catch (NullPointerException e) {
            view.showMessage("Operation canceled.");
        }

        notificationThread.setTaskList(taskList);
        TaskIO.writeBinary(taskList, new File(fileTask));
        backMenu();
    }

    public void removeTask() {
        view.removeTask(taskList);

        notificationThread.setTaskList(taskList);
        TaskIO.writeBinary(taskList, new File(fileTask));
        backMenu();
    }

    public void changeTask() {
        view.changeTask(taskList);

        notificationThread.setTaskList(taskList);
        TaskIO.writeBinary(taskList, new File(fileTask));
        backMenu();
    }

    public void showTasks() {
        view.showTasks(taskList);
        mainMenu();
    }

    public void calendar() {
        view.calendar(taskList);
        mainMenu();
    }

    public void mainMenu() {
        view.mainMenu();
        action = view.getAction();
        Functional actions = Functional.EMPTY;
        switch (action) {
            case 1: actions = Functional.SHOW_TASKS;
                break;
            case 2: actions = Functional.ADD_TASK;
                break;
            case 3: actions = Functional.REMOVE_TASK;
                break;
            case 4: actions = Functional.CHANGE_TASK;
                break;
            case 5: actions = Functional.CALENDAR;
                break;
            case 0: actions = Functional.FINISH;
        }
        process(actions);
    }

    private void backMenu() {
        try {
            Thread.sleep(1000);
            LOG.info("Sleep succeeded.");
        }
        catch (InterruptedException e) {
            System.out.println("Error. Return to main menu");
            LOG.error("Sleep failed.");
        }
        finally {
            mainMenu();
        }
    }

}