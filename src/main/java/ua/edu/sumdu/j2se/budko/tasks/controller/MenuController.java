package ua.edu.sumdu.j2se.budko.tasks.controller;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.budko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.budko.tasks.model.TaskIO;
import ua.edu.sumdu.j2se.budko.tasks.view.View;

import java.io.File;

public class MenuController extends Controller {
    private static final Logger LOG = Logger.getLogger(MenuController.class);

    public MenuController(View view, AbstractTaskList taskList) {
        super(view, taskList);
    }

    @Override
    public void process(Functional actions) {
        switch (actions) {
            case MENU: mainMenu();
                break;
            case SHOW_TASKS: showTasks();
                break;
            case ADD_TASK: addTask();
                break;
            case REMOVE_TASK: removeTask();
                break;
            case CHANGE_TASK: changeTask();
                break;
            case CALENDAR: calendar();
                break;
            case FINISH:
                TaskIO.writeBinary(taskList, new File("tasks.txt"));
                System.out.println("\nThank you for using our program! Good day!");
                System.exit(0);
                break;
        }
    }
}

