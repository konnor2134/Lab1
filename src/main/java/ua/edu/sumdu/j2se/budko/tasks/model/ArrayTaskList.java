package ua.edu.sumdu.j2se.budko.tasks.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Class for storing tasks in array list.
 */

public class ArrayTaskList extends AbstractTaskList implements Cloneable {
    private Task[] tasks = new Task[10];
    private int size;

    /**
     * Add task to list.
     */

    @Override
    public void add (Task task) throws NullPointerException {
        if (task == null)
            throw new NullPointerException("The task cannot be null");

        if (size >= tasks.length) {
            Task[] tasksCopy = tasks;
            tasks = new Task[tasks.length * 2];

            System.arraycopy(tasksCopy, 0, tasks, 0, tasksCopy.length);
        }

        tasks[size] = task;
        size++;
    }

    /**
     * Remove task from list.
     */

    @Override
    public boolean remove (Task task){
        int index = -1;
        for (int i = 0; i < size; i++)
            if (tasks[i].equals(task)) {
                index = i;
                break;
            }

        if (index == -1)
            return false;

        tasks[index] = null;
        for (int i = index + 1; i < size; i++) {
            tasks[i - 1] = tasks[i];
            tasks[i] = null;
        }
        size--;

        return true;
    }

    /**
     * Counts list size.
     */

    @Override
    public int size () {
        return size;
    }

    /**
     * Return task on index from list.
     */

    @Override
    public Task getTask ( int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Index is out of bounds");

        return tasks[index];
    }

    @Override
    public ListTypes.types getType() {
        return ListTypes.types.ARRAY;
    }

    @Override
    public Iterator<Task> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Task> {
        int cursor;
        int lastReturned = -1;

        /**
         * return if the iteration has more elements.
         */

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        /**
         * Returns the next element in the iteration.
         */

        @Override
        public Task next() {
            int i = cursor;
            if (i >= size)
                throw new IndexOutOfBoundsException();

            cursor++;
            return tasks[lastReturned = i];
        }

        @Override
        public void remove() {
            if (lastReturned < 0)
                throw new IllegalStateException();

            ArrayTaskList.this.remove(getTask(lastReturned));
            cursor = lastReturned;
            lastReturned = -1;
        }
    }

    /**
     * The equals() method compares two strings, and returns true if the strings are equal, and false if not.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) o;
        if (size != that.size) return false;

        for (int i = 0; i < size; i++) {
            if (!tasks[i].equals(that.tasks[i]))
                return false;
        }
        return true;
    }

    /**
     * Function that returns the hashcode value of an object on calling.
     */

    @Override
    public int hashCode() {
        int hash = 1;
        for (int i = 0; i < size; i++) {
            hash = 16 * hash + tasks[i].hashCode();
        }

        return hash;
    }

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList clone = (ArrayTaskList) super.clone();
        clone.tasks = Arrays.copyOf(tasks, size);
        for (int i = 0; i < size; i++) {
            clone.tasks[i] = tasks[i].clone();
        }
        return clone;
    }

    /**
     * Transforms object to a string.
     */


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Task task: this) {
            stringBuilder.append(task).append(";").append("\n");
        }

        return stringBuilder.toString();
    }

    /**
     * Transforms list to stream and returns stream.
     */

    @Override
    public Stream<Task> getStream() {
        return Arrays.stream(tasks, 0, size);

    }
}