package ua.edu.sumdu.j2se.budko.tasks.view;

/**
 * Class to display the messages.
 */

public class Notification {
    public void display(long sec, String title) {
        System.out.println("\nTask " + title + " must be done in " + (sec / 60) + " minutes");
    }
}
