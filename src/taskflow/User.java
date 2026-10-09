package taskflow;

// ENCAPSULATION: User holds their own TaskManager
public class User {

    private String name;
    private TaskManager taskManager;

    public User(String name) {
        this.name = name;
        this.taskManager = new TaskManager();
    }

    public String getName()              { return name; }
    public TaskManager getTaskManager()  { return taskManager; }
    public void setName(String name)     { this.name = name; }
}
