import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling exam business logic
 * Manages exam creation, retrieval, and validation
 */
public class ExamService {
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_INSTRUCTIONS_LENGTH = 1000;
    
    private final List<Exam> examList;
    private int nextExamId;

    public ExamService() {
        this.examList = new ArrayList<>();
        this.nextExamId = 1;
    }

    /**
     * Create exam with validation
     * AC1: Create exam with valid title and instructions
     * AC2: Reject blank title
     */
    public ExamCreationResult createExam(String title, String instructions) {
        Logger.debug("Attempting to create exam with title: " + title);
        
        // Validate title
        String validationError = validateTitle(title);
        if (validationError != null) {
            Logger.error("Exam creation failed: " + validationError);
            return new ExamCreationResult(false, validationError, null);
        }
        
        // Validate instructions
        validationError = validateInstructions(instructions);
        if (validationError != null) {
            Logger.error("Exam creation failed: " + validationError);
            return new ExamCreationResult(false, validationError, null);
        }
        
        try {
            String sanitizedTitle = title.trim();
            String sanitizedInstructions = (instructions == null) ? "" : instructions.trim();
            
            Exam newExam = new Exam(nextExamId++, sanitizedTitle, sanitizedInstructions);
            examList.add(newExam);
            
            Logger.info("Exam created successfully with ID: " + newExam.getExamId());
            return new ExamCreationResult(true, "Exam created successfully", newExam);
            
        } catch (Exception e) {
            Logger.error("Unexpected error during exam creation: " + e.getMessage());
            return new ExamCreationResult(false, "Error: An unexpected error occurred", null);
        }
    }

    private String validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return "Error: Exam title cannot be empty";
        }
        if (title.trim().length() > MAX_TITLE_LENGTH) {
            return "Error: Exam title cannot exceed " + MAX_TITLE_LENGTH + " characters";
        }
        return null;
    }

    private String validateInstructions(String instructions) {
        if (instructions != null && instructions.trim().length() > MAX_INSTRUCTIONS_LENGTH) {
            return "Error: Instructions cannot exceed " + MAX_INSTRUCTIONS_LENGTH + " characters";
        }
        return null;
    }

    /**
     * AC3: Retrieve all exams
     */
    public List<Exam> getAllExams() {
        return new ArrayList<>(examList);
    }

    public Exam getExamById(int examId) {
        return examList.stream()
                .filter(exam -> exam.getExamId() == examId)
                .findFirst()
                .orElse(null);
    }

    public int getExamCount() {
        return examList.size();
    }
    
    public List<Exam> searchExamsByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllExams();
        }
        
        String searchTerm = keyword.trim().toLowerCase();
        return examList.stream()
                .filter(exam -> exam.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    public boolean deleteExam(int examId) {
        boolean removed = examList.removeIf(exam -> exam.getExamId() == examId);
        if (removed) {
            Logger.info("Exam deleted: ID " + examId);
        }
        return removed;
    }
    
    /**
     * Initialize next ID based on existing exams (for database loading)
     */
    public void initializeNextId() {
        if (!examList.isEmpty()) {
            int maxId = examList.stream()
                    .mapToInt(Exam::getExamId)
                    .max()
                    .orElse(0);
            nextExamId = maxId + 1;
            Logger.debug("Next exam ID initialized to: " + nextExamId);
        }
    }
}
