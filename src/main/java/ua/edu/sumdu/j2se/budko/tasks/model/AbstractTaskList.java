package ua.edu.sumdu.j2se.budko.tasks.model;

import java.util.stream.Stream;
import java.io.Serializable;

public abstract class AbstractTaskList implements java.lang.Iterable<Task>, Serializable {

    /**
     * Add task to list.
     */

    public abstract void add(Task task) throws NullPointerException;

    /**
     * Remove task from list.
     */

    public abstract boolean remove(Task task);

    /**
     * Counts list size.
     */

    public abstract int size();

    /**
     * Return task on index from list.
     */

    public abstract Task getTask(int index) throws IndexOutOfBoundsException;

    public abstract ListTypes.types getType();

    /**
     * Transforms list to stream and returns stream.
     */

    public abstract Stream<Task> getStream();


}
