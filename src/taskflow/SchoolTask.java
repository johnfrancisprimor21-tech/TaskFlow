package taskflow;

// INHERITANCE: SchoolTask extends Task
public class SchoolTask extends Task {

    private String subject;
    private String professor;

    public SchoolTask(String title, String dueDate, String priority,
                      String subject, String professor) {
        super(title, dueDate, priority, "School");
        this.subject = subject;
        this.professor = professor;
    }

    // Getters
    public String getSubject()   { return subject; }
    public String getProfessor() { return professor; }

    public String getSubjectInfo() {
        return "Subject: " + subject + " | Professor: " + professor;
    }

    // POLYMORPHISM: Overrides getDetails() from Task
    @Override
    public String getDetails() {
        return "Title: " + getTitle() + "\n" +
               "Category: School\n" +
               "Priority: " + getPriority() + "\n" +
               "Due Date: " + getDueDate() + "\n" +
               "Status: " + getStatus() + "\n" +
               "Subject: " + subject + "\n" +
               "Professor: " + professor;
    }
}
