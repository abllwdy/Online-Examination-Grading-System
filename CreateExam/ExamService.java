import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class handling exam business logic
 * Manages exam creation, retrieval, and validation
 */
public class ExamService {
    private List<Exam> examList;
    private int nextExamId;
    
    // Validation constants
    private static final int MAX_TITLE_LENGTH = 200;
    private static final int MAX_INSTRUCTIONS_LENGTH = 1000;

    public ExamService() {
        this.examList = new ArrayList<>();
        this.nextExamId = 1;
    }

    /**
     * AC1: Create exam with valid title and instructions
     * AC2: Reject blank title
     * @param title - Exam title
     * @param instructions - Exam instructions
     * @return ExamCreationResult containing success status and message
     */
    public ExamCreationResult createExam(String title, String instructions) {
        Logger.debug("Attempting to create exam with title: " + title);
        
        // AC2: Validate title is not blank
        if (title == null || title.trim().isEmpty()) {
            Logger.error("Exam creation failed: Empty title");
            return new ExamCreationResult(false, "Error: Exam title cannot be empty", null);
        }
        
        // Additional validation: Check title length
        if (title.trim().length() > MAX_TITLE_LENGTH) {
            Logger.error("Exam creation failed: Title too long");
            return new ExamCreationResult(false, 
                "Error: Exam title cannot exceed " + MAX_TITLE_LENGTH + " characters", null);
        }
        
        // Additional validation: Check instructions length
        if (instructions != null && instructions.trim().length() > MAX_INSTRUCTIONS_LENGTH) {
            Logger.error("Exam creation failed: Instructions too long");
            return new ExamCreationResult(false, 
                "Error: Instructions cannot exceed " + MAX_INSTRUCTIONS_LENGTH + " characters", null);
        }
        
        try {
            // AC1: Create and save exam
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

    /**
     * AC3: Retrieve all exams
     * @return List of all exams (copy to prevent external modification)
     */
    public List<Exam> getAllExams() {
        return new ArrayList<>(examList);
    }

    /**
     * Get exam by ID
     * @param examId - ID of the exam
     * @return Exam object or null if not found
     */
    public Exam getExamById(int examId) {
        return examList.stream()
                .filter(exam -> exam.getExamId() == examId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get total number of exams
     * @return Count of exams
     */
    public int getExamCount() {
        return examList.size();
    }
    
    /**
     * Search exams by title
     * @param keyword - Search keyword
     * @return List of matching exams
     */
    public List<Exam> searchExamsByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllExams();
        }
        
        String searchTerm = keyword.trim().toLowerCase();
        return examList.stream()
                .filter(exam -> exam.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }
    
    /**
     * Delete exam by ID
     * @param examId - ID of exam to delete
     * @return true if deleted, false if not found
     */
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
