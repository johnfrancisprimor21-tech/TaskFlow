package taskflow;

// ABSTRACTION + ENCAPSULATION: Abstract class with private fields
public abstract class Task {

    private String title;
    private String dueDate;
    private String priority; // "High", "Medium", "Low"
    private String status;   // "Pending", "Done"
    private String category; // "Work", "Personal", "School"

    public Task(String title, String dueDate, String priority, String category) {
        this.title = title;
        this.dueDate = dueDate;
        this.priority = priority;
        this.category = category;
        this.status = "Pending";
    }

    // ABSTRACTION: Subclasses must implement this
    public abstract String getDetails();

    // Getters
    public String getTitle()    { return title; }
    public String getDueDate()  { return dueDate; }
    public String getPriority() { return priority; }
    public String getStatus()   { return status; }
    public String getCategory() { return category; }

    // Setters
    public void setTitle(String title)       { this.title = title; }
    public void setDueDate(String dueDate)   { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setCategory(String category) { this.category = category; }

    public void markAsDone() {
        this.status = "Done";
    }

    @Override
    public String toString() {
        return "[" + status + "] " + title + " | " + priority + " | Due: " + dueDate + " | " + category;
    }
}
