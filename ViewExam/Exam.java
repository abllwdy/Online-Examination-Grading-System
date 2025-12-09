import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Exam {
    private static int examCounter = 0;
    private int examId;
    private String title;
    private String instructions;
    private LocalDateTime scheduledDateTime;
    private int durationMinutes;
    private LocalDateTime createdAt;
    private List<String> enrolledStudentIds; // Student IDs enrolled in this exam
    // private List<Question> questions;

    // Constructor
    public Exam(String title, String instructions, LocalDateTime scheduledDateTime, int durationMinutes) {
        this.examId = ++examCounter;
        this.title = title;
        this.instructions = instructions;
        this.scheduledDateTime = scheduledDateTime;
        this.durationMinutes = durationMinutes;
        this.createdAt = LocalDateTime.now();
        this.enrolledStudentIds = new ArrayList<>();
        // this.questions = new ArrayList<>();
    }

    // Getters
    public int getExamId() { return examId; }
    public String getTitle() { return title; }
    public String getInstructions() { return instructions; }
    public LocalDateTime getScheduledDateTime() { return scheduledDateTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<String> getEnrolledStudentIds() { return enrolledStudentIds; }
    //public List<Question> getQuestions() { return questions; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setInstructions(String instructions) { this.instructions = instructions; }
    public void setScheduledDateTime(LocalDateTime scheduledDateTime) { 
        this.scheduledDateTime = scheduledDateTime; 
    }
    public void setDurationMinutes(int durationMinutes) { 
        this.durationMinutes = durationMinutes; 
    }

    // Enroll student
    public void enrollStudent(String studentId) {
        if (!enrolledStudentIds.contains(studentId)) {
            enrolledStudentIds.add(studentId);
        }
    }

    // Check if exam is upcoming
    public boolean isUpcoming() {
        return scheduledDateTime.isAfter(LocalDateTime.now());
    }

    // Check if student is enrolled
    public boolean isStudentEnrolled(String studentId) {
        return enrolledStudentIds.contains(studentId);
    }

    @Override
    public String toString() {
        return String.format("Exam #%d: %s | Scheduled: %s | Duration: %d mins", 
            examId, title, scheduledDateTime, durationMinutes);
    }
}
