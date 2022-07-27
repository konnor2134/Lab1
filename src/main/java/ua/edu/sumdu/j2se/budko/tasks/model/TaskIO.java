package ua.edu.sumdu.j2se.budko.tasks.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;

/**
 * This class contains methods for reading and writing to a file.
 */

public class TaskIO {
    private static final Logger LOG = Logger.getLogger(TaskIO.class);

    /**
     * Writes tasks from the list to the stream in the binary format.
     */

    public static void write (AbstractTaskList tasks, OutputStream out) {
        try (ObjectOutputStream oos = new ObjectOutputStream(out)) {
            oos.writeInt(tasks.size());
            for (Task task: tasks) {
                oos.writeInt(task.getTitle().length());
                oos.writeChars(task.getTitle());
                oos.writeBoolean(task.isActive());
                oos.writeInt(task.getRepeatInterval());

                if (task.isRepeated()) {
                    oos.writeObject(task.getStartTime());
                    oos.writeObject(task.getEndTime());
                }
                else {
                    oos.writeObject(task.getTime());
                }
            }
        }
        catch (IOException e) {
            LOG.error("Can't write", e);
        }
    }

    /**
     * Reads tasks from the stream to this task list.
     */

    public static void read (AbstractTaskList tasks, InputStream in) {
        try (ObjectInputStream ois = new ObjectInputStream(in)) {
            int size = ois.readInt();
            for (int i = 0; i < size; i++) {
                int titleLength = ois.readInt();
                char[] chars = new char[titleLength];

                for (int j = 0; j < titleLength; j++) {
                    chars[j] = ois.readChar();
                }

                boolean active = ois.readBoolean();
                int interval = ois.readInt();
                LocalDateTime time = (LocalDateTime) ois.readObject();

                Task task;
                String title = String.valueOf(chars);
                if (interval == 0)
                    task = new Task(title, time);
                else {
                    LocalDateTime endTime = (LocalDateTime) ois.readObject();
                    task = new Task(title, time, endTime, interval);
                }

                task.setActive(active);
                tasks.add(task);
            }
        }
        catch (IOException | ClassNotFoundException e){
            LOG.error("Can't read, class not find", e);
        }
    }

    /**
     * Writes tasks from the list to a file.
     */

    public static void writeBinary(AbstractTaskList tasks, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            write(tasks, fileOutputStream);
        }
        catch (IOException e) {
            LOG.error("Can't write binary", e);
        }
    }

    /**
     * Reads tasks from the file to the task list.
     */

    public static void readBinary (AbstractTaskList tasks, File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            read(tasks, fileInputStream);
        }
        catch (IOException e){
            LOG.error("Can't read binary", e);
        }
    }

    /**
     * Writes tasks from the list to the stream in JSON format.
     */

    public static void write (AbstractTaskList tasks, Writer out) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();

        try (FileWriter fileWriter = (FileWriter) out) {
            fileWriter.write(gson.toJson(tasks.getStream().toArray(Task[]::new)));
        }
        catch (IOException e) {
            LOG.error("Can't file write", e);
        }
    }

    /**
     * Reads tasks from the stream to the list.
     */

    public static void read (AbstractTaskList tasks, Reader in){
        Gson gson = new Gson();

        try (FileReader fileReader = (FileReader) in) {
            for (Task task : gson.fromJson(fileReader, Task[].class)) {
                tasks.add(task);
            }
        }
        catch (IOException e) {
            LOG.error("Can't file read", e);
        }
    }

    /**
     * Writes tasks to a file in JSON format
     */

    public static void writeText (AbstractTaskList tasks, File file){
        try (FileWriter fileWriter = new FileWriter(file)) {
            write(tasks, fileWriter);
        }
        catch (IOException e) {
            LOG.error("Can't write text", e);
        }
    }

    /**
     * Reads tasks from a file
     */

    public static void readText (AbstractTaskList tasks, File file) {
        try (FileReader fileReader = new FileReader(file)) {
            read(tasks, fileReader);
        }
        catch (IOException e) {
            LOG.error("Can't read text", e);
        }
    }
}
