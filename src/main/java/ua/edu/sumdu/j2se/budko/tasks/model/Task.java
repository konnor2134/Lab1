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
     * Конструктор - создает неактивную задачу, которая выполняется за время без повторения с заданным названием.
     * @param title - название задачи.
     * @param time - время выполнения.
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
     * Конструктор - создает неактивную задачу, выполняемую в промежутке времени  с интервалом и имеющее заданное название.
     * @param title - название задачи.
     * @param start - время начала выполнения задачи.
     * @param end - время конца выполнения задачи.
     * @param interval - интервал повторений выполнения задачи.
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

    @Override
    public int hashCode() {
        return Objects.hash(title, time, interval, active);
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }

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
     * Возвращает название задачи.
     * @return название задачи.
     */

    public String getTitle () {
            return title;
        }
    /**
    * Устанавливает название задачи.
    * @param title - название задачи.
    */

    public void setTitle (String title){
        this.title = title;
    }

    /**
    * @return - возвращает состояние активности задачи.
    */

    public boolean isActive () {
            return active;
        }

    /**
    * Устанавливает активность задачи.
    * @param active – true означает, что задача становится активной, false – неактивна.
    */

    public void setActive ( boolean active){
            this.active = active;
        }

    /**
    * Метод для работы со временем у неповторяющихся задач.
    * @return - возвращает время выполнения задачи. Если задача повторяемая – возвращает время начала выполнения задачи.
    */

    public LocalDateTime getTime () {
            return time;
        }

    /**
    * Метод, при котором если задача повторялась, она должна стать такой, что не повторяется.
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
    * В случае, если задача не повторяется, метод должен возвращать время выполнения задачи.
    */

    public LocalDateTime getStartTime () {
            return start;
        }
    /**
    * В случае, если задача не повторяется, метод должен возвращать время выполнения задачи.
    */

    public LocalDateTime getEndTime () {
            return end;
        }

    /**
    * В случае, если задача не повторяется, метод должен возвращать 0.
    */

    public int getRepeatInterval () {

        return interval;
    }

    /**
    * В случае, если задача не повторяется, метод должен стать повторяющимся.
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