import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Exam {
    private static int examCounter = 0; // Auto-increment ID
    private int examId;
    private String title;
    private String instructions;
    private LocalDateTime createdAt;
    private List<Question> questions; // For future use (US08)

    // Constructor
    public Exam(String title, String instructions) {
        this.examId = ++examCounter;
        this.title = title;
        this.instructions = instructions;
        this.createdAt = LocalDateTime.now();
        this.questions = new ArrayList<>();
    }

    // Getters and Setters
    public int getExamId() {
        return examId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    @Override
    public String toString() {
        return "Exam ID: " + examId + 
               " | Title: " + title + 
               " | Instructions: " + instructions + 
               " | Created: " + createdAt.toLocalDate();
    }
}
