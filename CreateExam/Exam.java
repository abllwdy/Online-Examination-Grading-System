import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Exam entity class representing an examination
 * Contains exam details, questions, and metadata
 */
public class Exam {
    private final int examId;
    private String title;
    private String instructions;
    private final LocalDateTime createdAt;
    private final List<Question> questions;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Constructor for new exams
    public Exam(int examId, String title, String instructions) {
        this.examId = examId;
        this.title = title;
        this.instructions = instructions;
        this.createdAt = LocalDateTime.now();
        this.questions = new ArrayList<>();
    }
    
    // Constructor for loading existing exams (e.g., from database)
    public Exam(int examId, String title, String instructions, LocalDateTime createdAt) {
        this.examId = examId;
        this.title = title;
        this.instructions = instructions;
        this.createdAt = createdAt;
        this.questions = new ArrayList<>();
    }

    // Getters
    public int getExamId() {
        return examId;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructions() {
        return instructions;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns unmodifiable list to protect internal state
     */
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
    
    public int getQuestionCount() {
        return questions.size();
    }

    // Setters with validation
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title.trim();
    }

    public void setInstructions(String instructions) {
        this.instructions = (instructions == null) ? "" : instructions.trim();
    }

    /**
     * Controlled method to add questions
     */
    public void addQuestion(Question question) {
        if (question == null) {
            throw new IllegalArgumentException("Question cannot be null");
        }
        this.questions.add(question);
    }
    
    /**
     * Remove question by ID
     */
    public boolean removeQuestion(int questionId) {
        return questions.removeIf(q -> q.getQuestionId() == questionId);
    }

    @Override
    public String toString() {
        return String.format("Exam ID: %d | Title: %s | Instructions: %s | Created: %s | Questions: %d",
                examId, title, instructions, createdAt.format(DATE_FORMATTER), questions.size());
    }
}
