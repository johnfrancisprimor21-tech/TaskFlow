package taskflow;

// INHERITANCE: PersonalTask extends Task
public class PersonalTask extends Task {

    private String location;
    private String reminderNote;

    public PersonalTask(String title, String dueDate, String priority,
                        String location, String reminderNote) {
        super(title, dueDate, priority, "Personal");
        this.location = location;
        this.reminderNote = reminderNote;
    }

    // Getters
    public String getLocation()     { return location; }
    public String getReminderNote() { return reminderNote; }

    public String getReminder() {
        return "Reminder: " + reminderNote + " @ " + location;
    }

    // POLYMORPHISM: Overrides getDetails() from Task
    @Override
    public String getDetails() {
        return "Title: " + getTitle() + "\n" +
               "Category: Personal\n" +
               "Priority: " + getPriority() + "\n" +
               "Due Date: " + getDueDate() + "\n" +
               "Status: " + getStatus() + "\n" +
               "Location: " + location + "\n" +
               "Reminder: " + reminderNote;
    }
}
