package ua.edu.sumdu.j2se.budko.tasks.view;

import ua.edu.sumdu.j2se.budko.tasks.model.*;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;

import static java.util.Locale.forLanguageTag;

public class View {
    private enum ChangeType {
        EMPTY,
        RENAME,
        ACTIVE,
        TIME,
        START_TIME,
        END_TIME,
        REPEAT_INTERVAL
    }
    Scanner reader;
    private static final Logger log = Logger.getLogger(View.class);

    public int getAction() {
        reader = new Scanner(System.in);
        int value = -1;
        boolean flag = true;
        while (flag) {
            try {
                value = Integer.parseInt(reader.nextLine());
                if (value < 0 || value > 5) {
                    throw new Exception("Enter the correct number ");
                }
                flag = false;
            }
            catch (NumberFormatException e) {
                System.out.println("Enter the correct number: ");
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return value;
    }

    public Task addTask() {
        Task task;
        System.out.println("Enter the name of the task (0 - cancel):");
        String title = reader.nextLine();
        if ("0".equals(title)) {
            return null;
        }
        System.out.println("Does the task run more than once? (true/false)");
        boolean repeat = Boolean.parseBoolean(reader.nextLine());
        if (!repeat) {
            System.out.println("Enter a due date for the task (format: dd-MM-yyyy HH:mm:ss) (0 - cancel): ");
            LocalDateTime time = inputDate();
            if (time == null) {
                return null;
            }
            task = new Task(title, time);
        }
        else {
            System.out.println("Enter the start date for the task (format: dd-MM-yyyy HH:mm:ss) (0 - cancel): ");
            LocalDateTime startTime = inputDate();
            if (startTime == null) {
                return null;
            }
            System.out.println("Enter the end date for the task (format: dd-MM-yyyy HH:mm:ss) (0 - cancel): ");
            LocalDateTime endTime = inputDate();
            if (endTime == null) {
                return null;
            }
            while (startTime.isAfter(endTime)) {
                System.out.println("The task cannot end before it started");
                log.info("The task cannot end before it started");
                endTime = inputDate();
            }
            System.out.println("Enter the iteration interval (in seconds): ");
            int interval = Integer.parseInt(reader.nextLine());
            task = new Task(title, startTime, endTime, interval);
        }
        System.out.println("Do you want to make the task active? true/false ");
        boolean isActive = Boolean.parseBoolean(reader.nextLine());
        task.setActive(isActive);
        System.out.println("Task " + task.getTitle() + " added");
        return task;
    }

    public void removeTask(AbstractTaskList taskList) {
        System.out.println("Enter the name of the task to delete (0 - cancel): ");
        String title = reader.nextLine();
        if ("0".equals(title)) {
            return;
        }

        for (Task task : taskList) {
            if (Objects.equals(task.getTitle(), title)) {
                taskList.remove(task);
                System.out.println("Task " + task.getTitle() + " deleted");
                log.info("Task " + task.getTitle() + " deleted");
                return;
            }
        }
        System.out.println("Task " + title + " not found");
        log.info("Task " + title + " not found");
    }

    public void changeTask(AbstractTaskList taskList) {
        System.out.println("Enter the name of the task to edit (0 - cancel): ");
        String title = reader.nextLine();
        if ("0".equals(title)) {
            return;
        }

        for (Task task : taskList) {
            if (Objects.equals(task.getTitle(), title)) {
                System.out.println(task);
                System.out.println("Choose what you want to edit: ");
                System.out.println("1. Rename");
                System.out.println("2. Make Active/Inactive");
                System.out.println("3. Edit the due date");
                System.out.println("4. Edit the start date");
                System.out.println("5. Edit the end date");
                System.out.println("6. Edit the iteration interval");
                System.out.println("0. Cancel");
                LocalDateTime dateTime;
                String result = "";
                ChangeType changeType = ChangeType.EMPTY;
                while (changeType.equals(ChangeType.EMPTY)) {
                    int value = Integer.parseInt(reader.nextLine());
                    switch (value) {
                        case 0:
                            return;
                        case 1:
                            System.out.println("Enter the name of the task (0 - cancel): ");
                            result = reader.nextLine();
                            if ("0".equals(result)) {
                                return;
                            }
                            task.setTitle(result);
                            changeType = ChangeType.RENAME;
                            break;
                        case 2:
                            System.out.println("Make active? true/false (0 - cancel): ");
                            result = reader.nextLine();
                            if ("0".equals(result)) {
                                return;
                            }
                            task.setActive(Boolean.parseBoolean(result));
                            changeType = ChangeType.ACTIVE;
                            break;
                        case 3:
                            System.out.println("Enter a due date for the task (format: dd-MM-yyyy HH:mm:ss) :");
                            dateTime = inputDate();
                            task.setTime(dateTime);
                            changeType = ChangeType.TIME;
                            break;
                        case 4:
                            System.out.println("Enter the start date for the task(format: dd-MM-yyyy HH:mm:ss) : ");
                            dateTime = inputDate();
                            task.setTime(dateTime, task.getEndTime(), task.getRepeatInterval());
                            changeType = ChangeType.START_TIME;
                            break;

                        case 5:
                            System.out.println("Enter the end date for the task (format: dd-MM-yyyy HH:mm:ss) : ");
                            dateTime = inputDate();
                            task.setTime(task.getStartTime(), dateTime, task.getRepeatInterval());
                            changeType = ChangeType.END_TIME;
                            break;
                        case 6:
                            System.out.println("Enter the iteration interval (in seconds): ");
                            result = reader.nextLine();
                            task.setTime(task.getStartTime(), task.getEndTime(), Integer.parseInt(result));
                            changeType = ChangeType.REPEAT_INTERVAL;
                            break;
                        default:
                            System.out.println("Error. Enter the correct number: ");
                    }
                }
                System.out.println("At the task " + title + " changed " + changeType + " on " + result);
                log.info("At the task " + title + " changed " + changeType + " on " + result);
                return;
            }
        }
        System.out.println("Task " + title + " not found");
        log.info("Task " + title + " not found.");
    }

    public void showTasks(AbstractTaskList taskList) {
        System.out.println(taskList);
        log.info("Showing all tasks");
        System.out.println("Enter any character");
        reader.nextLine();
    }

    public void mainMenu() {
        System.out.println("1. Show all tasks");
        System.out.println("2. Add task");
        System.out.println("3. Delete task");
        System.out.println("4. Edit task");
        System.out.println("5. Calendar");
        System.out.println("0. Exit");
    }

    public void calendar(AbstractTaskList taskList) {
        System.out.println("Enter the first date on the calendar (format: dd-MM-yyyy HH:mm:ss) : ");
        LocalDateTime start = inputDate();

        System.out.println("Enter the last date on the calendar (format: dd-MM-yyyy HH:mm:ss) : ");
        LocalDateTime end = inputDate();
        while (start.isAfter(end)) {

            System.out.println("The task cannot end before it started");
            log.info("The task cannot end before it started");
            end = inputDate();

        }

        SortedMap<LocalDateTime, Set<Task>> sortedMap = Tasks.calendar(taskList, start, end);
        if (sortedMap.entrySet().size() == 0) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println("Date                  | Task         ");
        Object[] tasks;
        for (SortedMap.Entry<LocalDateTime, Set<Task>> entry : sortedMap.entrySet()) {
            tasks = entry.getValue().toArray();
            System.out.print(entry.getKey().format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss",forLanguageTag("eng"))) + " | ");
            for (int i = 0; i < tasks.length; i++) {
                if (i + 1 == tasks.length) {
                    System.out.print(((Task) tasks[i]).getTitle() + ".");
                } else {
                    System.out.print(((Task) tasks[i]).getTitle() + ", ");
                }
            }
            System.out.println();
        }
        System.out.println("\nEnter any character");
        reader.nextLine();
    }

    private LocalDateTime inputDate() {
        while (true) {
            try {
                String date = reader.nextLine();
                if ("0".equals(date)) {
                    return null;
                }
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            }
            catch (DateTimeParseException e) {
                System.out.println("Enter the date in the correct format (format: dd-MM-yyyy HH:mm:ss): ");
                log.info("Wrong date format.");
            }
        }
    }

    public void showMessage(String message) {
        System.out.println(message);
    }
}