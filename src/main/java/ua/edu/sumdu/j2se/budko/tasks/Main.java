package ua.edu.sumdu.j2se.budko.tasks;

import ua.edu.sumdu.j2se.budko.tasks.controller.Functional;
import ua.edu.sumdu.j2se.budko.tasks.controller.Controller;
import ua.edu.sumdu.j2se.budko.tasks.controller.MenuController;
import ua.edu.sumdu.j2se.budko.tasks.model.*;
import ua.edu.sumdu.j2se.budko.tasks.view.*;

import java.io.File;



public class Main {

	public static void main(String[] args) {
		final String fileTask = "tasks.txt";
		System.out.println("\nHello there). Welcome to the Task Manager app!\n");
		AbstractTaskList taskList = new ArrayTaskList();
		TaskIO.readBinary(taskList, new File(fileTask));
		View view = new View();
		Controller controller = new MenuController(view, taskList);
		controller.process(Functional.MENU);
	}
}