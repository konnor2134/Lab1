package ua.edu.sumdu.j2se.budko.tasks.model;

import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Tasks {

    /**
     * Saves tasks to the collection.
     */

    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        if (tasks == null || start == null || end == null || start.isAfter(end))
            throw new IllegalArgumentException();

        Stream<Task> taskStream = StreamSupport.stream(tasks.spliterator(), false);
        LinkedTaskList taskList = new LinkedTaskList();
        taskStream.filter(task -> task.nextTimeAfter(start) != null
                        && task.nextTimeAfter(start).compareTo(end) <= 0)
                .distinct()
                .forEach(taskList::add);
        return taskList;
    }

    /**
     * Build a calendar of tasks for a given period - a table where each date corresponds
     * to a set of tasks to be performed at this time, and one task can occur according to several dates,
     * if it must be performed several times during the specified period.
     */

    public static SortedMap<LocalDateTime, Set<Task>> calendar(
            Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        if (tasks == null || start == null || end == null || start.isAfter(end))
            throw new IllegalArgumentException();

        tasks = incoming(tasks, start, end);
        TreeMap<LocalDateTime, Set<Task>> calendar = new TreeMap<>();

        for(Task task : tasks) {
            LocalDateTime startTime = start;
            while (true) {
                startTime = task.nextTimeAfter(startTime);
                if (startTime == null || startTime.isAfter(end)) break;

                Set<Task> taskSet;
                if (calendar.containsKey(startTime)) {
                    taskSet = calendar.get(startTime);
                }
                else {
                    taskSet = new HashSet<>();
                    calendar.put(startTime, taskSet);
                }

                taskSet.add(task);
            }
        }
        return calendar;
    }
}