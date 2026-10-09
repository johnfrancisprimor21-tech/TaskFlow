package taskflow;

// INHERITANCE: WorkTask extends Task
public class WorkTask extends Task {

    private String department;
    private String projectName;

    public WorkTask(String title, String dueDate, String priority,
                    String department, String projectName) {
        super(title, dueDate, priority, "Work");
        this.department = department;
        this.projectName = projectName;
    }

    // Getters
    public String getDepartment()  { return department; }
    public String getProjectName() { return projectName; }

    public String getProjectInfo() {
        return "Project: " + projectName + " | Department: " + department;
    }

    // POLYMORPHISM: Overrides getDetails() from Task
    @Override
    public String getDetails() {
        return "Title: " + getTitle() + "\n" +
               "Category: Work\n" +
               "Priority: " + getPriority() + "\n" +
               "Due Date: " + getDueDate() + "\n" +
               "Status: " + getStatus() + "\n" +
               "Department: " + department + "\n" +
               "Project: " + projectName;
    }
}
