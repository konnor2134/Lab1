package ua.edu.sumdu.j2se.budko.tasks;

import java.util.stream.Stream;
import java.io.Serializable;

public abstract class AbstractTaskList implements java.lang.Iterable<Task>, Serializable  {
    public abstract void add(Task task) throws NullPointerException;
    public abstract boolean remove(Task task);
    public abstract int size();
    public abstract Task getTask(int index) throws IndexOutOfBoundsException;
    public abstract ListTypes.types getType();
    public abstract Stream<Task> getStream();

}