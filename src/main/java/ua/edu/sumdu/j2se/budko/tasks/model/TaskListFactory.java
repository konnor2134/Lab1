package ua.edu.sumdu.j2se.budko.tasks.model;

public class TaskListFactory {

    /**
     * Method, according to the type parameter, must return an object of class ArrayTaskList or LinkedTaskList

     */

    public static AbstractTaskList createTaskList(ListTypes.types type) {
        if (type == ListTypes.types.ARRAY) {
            return new ArrayTaskList();
        }
        return new LinkedTaskList();
    }
}
