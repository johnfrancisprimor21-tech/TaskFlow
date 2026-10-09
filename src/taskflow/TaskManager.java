package taskflow;

import java.util.ArrayList;

// ABSTRACTION: Hides ArrayList logic behind simple methods
public class TaskManager {

    // ARRAYLIST: stores all tasks
    private ArrayList<Task> taskList;

    public TaskManager() {
        taskList = new ArrayList<>();
    }

    // Add a task
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        taskList.add(task);
    }

    // Delete task by index
    public void deleteTask(int index) {
        if (taskList.isEmpty()) {
            throw new IllegalStateException("No tasks to delete.");
        }
        if (index < 0 || index >= taskList.size()) {
            throw new IndexOutOfBoundsException("Invalid task selection.");
        }
        taskList.remove(index);
    }

    // Mark task as done by index
    public void markTaskDone(int index) {
        if (taskList.isEmpty()) {
            throw new IllegalStateException("No tasks available.");
        }
        if (index < 0 || index >= taskList.size()) {
            throw new IndexOutOfBoundsException("Invalid task selection.");
        }
        taskList.get(index).markAsDone();
    }

    // Get all tasks
    public ArrayList<Task> getAllTasks() {
        return taskList;
    }

    // Filter by priority
    public ArrayList<Task> filterByPriority(String priority) {
        ArrayList<Task> filtered = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getPriority().equalsIgnoreCase(priority)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    // Filter by category
    public ArrayList<Task> filterByCategory(String category) {
        ArrayList<Task> filtered = new ArrayList<>();
        for (Task task : taskList) {
            if (task.getCategory().equalsIgnoreCase(category)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    // Get task by index
    public Task getTask(int index) {
        if (index < 0 || index >= taskList.size()) {
            throw new IndexOutOfBoundsException("Invalid task index.");
        }
        return taskList.get(index);
    }

    public int getTaskCount() {
        return taskList.size();
    }
}
