package ua.edu.sumdu.j2se.budko.tasks.model;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Class for storing tasks in linked list.
 */

public class LinkedTaskList extends AbstractTaskList implements Cloneable {
    private Node first;
    private Node last;
    private int size;

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
        }
    }

    /**
     * Method for adding tasks.
     */

    @Override
    public void add(Task task) throws NullPointerException {
        if(task == null)
            throw new NullPointerException("The task cannot be null");

        Node node = new Node(task);

        if (first == null)
            first = node;
        else {
            last.next = node;
            node.prev = last;
        }
        last = node;
        size++;
    }

    /**
     * Method for removing tasks.
     */

    @Override
    public boolean remove(Task task) {
        for (Node temp = first; temp != null; temp = temp.next) {
            if (temp.task.equals(task)) {
                if (temp.prev == null) {
                    first = temp.next;
                }
                else {
                    temp.prev.next = temp.next;
                }

                if (temp.next == null) {
                    last = temp.prev;
                }
                else {
                    temp.next.prev = temp.prev;
                }
                size--;
                return true;
            }
        }

        return false;
    }
    /**
     * Counts list size.
     */

    @Override
    public int size() {
        return size;
    }

    /**
     * Return task on index from list.
     */

    @Override
    public Task getTask(int index) throws IndexOutOfBoundsException {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Index is out of bounds");

        Node temp;

        if (index < (size / 2)) {
            temp = first;
            for (int i = 0; i < index; i++)
                temp = temp.next;
        }
        else {
            temp = last;
            for (int i = size - 1; i > index; i--)
                temp = temp.prev;
        }

        return temp.task;
    }

    @Override
    public ListTypes.types getType() {
        return ListTypes.types.LINKED;
    }

    @Override
    public Iterator<Task> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Task> {
        Node lastReturned;
        Node next = first;
        int nextIndex;

        /**
         * returns value if the iteration has more elements
         */

        @Override
        public boolean hasNext() {
            return nextIndex < size;
        }

        /**
         * Returns the next element in the iteration.
         */

        @Override
        public Task next() {
            if (!hasNext())
                throw new IndexOutOfBoundsException();

            lastReturned = next;
            next = next.next;
            nextIndex++;
            return lastReturned.task;
        }

        /**
         * Removes the current item.
         */

        @Override
        public void remove() {
            if (lastReturned == null)
                throw new IllegalStateException();

            Node lastNext = lastReturned.next;
            LinkedTaskList.this.remove(lastReturned.task);
            if (next.equals(lastReturned))
                next = lastNext;
            else
                nextIndex--;
            lastReturned = null;
        }
    }

    /**
     * The equals() method compares two strings, and returns true if the strings are equal, and false if not.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedTaskList)) return false;
        LinkedTaskList that = (LinkedTaskList) o;
        if (size != that.size) return false;
        Iterator<Task> j = iterator();
        for (Object obj: that) {
            if (!j.next().equals(obj))
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

        for (Node temp = first; temp != null; temp = temp.next) {
            hash = 16 * hash + temp.task.hashCode();
        }

        return hash;
    }

    @Override
        public LinkedTaskList clone() throws CloneNotSupportedException {
            LinkedTaskList clone = (LinkedTaskList) super.clone();

            clone.first = clone.last = null;
            clone.size = 0;

            for (Node temp = first; temp != null; temp = temp.next)
                clone.add(temp.task.clone());

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
        Stream.Builder<Task> streamBuilder = Stream.builder();
        for (Task task: this) {
            streamBuilder.add(task);
        }

        return streamBuilder.build();
    }
}