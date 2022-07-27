package ua.edu.sumdu.j2se.budko.tasks.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.io.Serializable;

public class Task implements Cloneable, Serializable {

    private String title;
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;
    private boolean active;

    public Task() {
        title = "No title";
    }

    /**
     * Constructor - creates an inactive task that runs in time without repetition with a given name.
     * @param title - task title.
     * @param time - execution time.
     */

    public Task(String title, LocalDateTime time) {
        if (time == null)
            throw new IllegalArgumentException("The title cannot be null and the time cannot be a negative");

        this.title = title;
        this.time = time;
        start = time;
        end = start;
        interval = 0;
    }

    /**
     * Constructor - creates an inactive task that runs in a period of time with an interval and has a given name.
     * @param title - task title.
     * @param start - task execution start time.
     * @param end - end time of task execution.
     * @param interval - task repetition interval.
     */

    public Task(String title, LocalDateTime start, LocalDateTime end, int interval)  {
        if (start == null || end == null || interval < 0 || start.isAfter(end))
            throw new IllegalArgumentException("The title cannot be null and the interval cannot be a negative");

        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        time = start;
    }

    /**
     * The equals() method compares two strings, and returns true if the strings are equal, and false if not.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        if (isRepeated()) {
            return title.equals(task.title)
                    && start.isEqual(task.start)
                    && end.isEqual(task.end)
                    && interval == task.interval
                    && active == task.active;
        } else {
            return title.equals(task.title) && time.isEqual(task.time) && active == task.active;
        }

    }

    /**
     * Function that returns the hashcode value of an object on calling.
     */

    @Override
    public int hashCode() {
        return Objects.hash(title, time, interval, active);
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

    /**
     * Transforms object to a string.
     */

    @Override
    public String toString() {
        if (!isRepeated()) {
            return "title='" + title + '\''
                    + ", time=" + time
                    + ", active=" + active;
        }
        else {
            return "title='" + title + '\''
                    + ", start=" + start
                    + ", end=" + end
                    + ", interval=" + interval + " seconds"
                    + ", active=" + active;
        }
    }
    /**
     * Returns the name of the task.
     * @return task name.
     */

    public String getTitle () {
            return title;
        }

    /**
     * Sets the name of the task.
     * @param title - task title.
     */

    public void setTitle (String title){
        this.title = title;
    }

    /**
     * @return - returns the activity state of the task.
     */

    public boolean isActive () {
            return active;
        }

    /**
     * Sets the activity of the task.
     * @param active - true means that the task becomes active, false - inactive.
     */

    public void setActive ( boolean active){
            this.active = active;
        }

    /**
     * A method for working with time for non-recurring tasks.
     * @return - returns the execution time of the task. If the task is repeatable, returns the start time of the task.
     */

    public LocalDateTime getTime () {
            return time;
        }

    /**
     * A method whereby if a task has been repeated, it must become such that it does not repeat itself.
     */

    public void setTime (LocalDateTime time){
        this.time = time;

        if (interval != 0) {
                start = time;
                end = start;
                interval = 0;
        }
    }

    /**
     * In case the task is not repeated, the method should return the task execution time.
     */

    public LocalDateTime getStartTime () {
            return start;
        }
    /**
     * In case the task is not repeated, the method should return the task execution time.
     */

    public LocalDateTime getEndTime () {
            return end;
        }

    /**
     * In case the task is not repeated, the method should return 0.
     */

    public int getRepeatInterval () {

        return interval;
    }

    /**
     * In case the task is not repetitive, the method should become repetitive.
     */

    public void setTime (LocalDateTime start, LocalDateTime end,int interval){
        if (this.interval == 0)
            time = start;

        this.start = start;
        this.end = end;
        this.interval = interval;

    }

    public boolean isRepeated () {
            return interval != 0;
        }

    /**
     * Returns the time of the next task after the specified time.
     * @return next task time if the task run after the specified time, null if the task doesn't.
     */

    public LocalDateTime nextTimeAfter (LocalDateTime current){
        if (current == null)
            throw new IllegalArgumentException();

        if (!isActive()) return null;

        if (isRepeated()) {
            ChronoUnit seconds = ChronoUnit.SECONDS;
            long countOfInterval = seconds.between(start, end) / interval;

            if (current.compareTo(start.plusSeconds(interval * countOfInterval)) >= 0)
                return null;
            else {
                long a = current.compareTo(start) >= 0 ? seconds.between(start, current) : -interval;
                int intervalNumber = (int) (((double) a / interval) + 1.0);
                return start.plusSeconds((long) interval * intervalNumber);
            }
        }
        else {
            if (current.compareTo(time) >= 0)
                return null;
            else
                return time;
        }
    }
}