import java.util.ArrayList;
import java.util.List;

public class ExamService {
    private List<Exam> examList;

    public ExamService() {
        this.examList = new ArrayList<>();
    }

    /**
     * AC1: Create exam with valid title and instructions
     * AC2: Reject blank title
     * @param title - Exam title
     * @param instructions - Exam instructions
     * @return Validation message
     */
    public String createExam(String title, String instructions) {
        // AC2: Validate title is not blank
        if (title == null || title.trim().isEmpty()) {
            return "Error: Exam title cannot be empty";
        }

        // AC1: Create and save exam
        Exam newExam = new Exam(title.trim(), instructions.trim());
        examList.add(newExam);
        
        return "Exam created successfully";
    }

    /**
     * AC3: Retrieve all exams
     * @return List of all exams
     */
    public List<Exam> getAllExams() {
        return new ArrayList<>(examList); // Return a copy to prevent external modification
    }

    /**
     * Get exam by ID
     * @param examId - ID of the exam
     * @return Exam object or null if not found
     */
    public Exam getExamById(int examId) {
        for (Exam exam : examList) {
            if (exam.getExamId() == examId) {
                return exam;
            }
        }
        return null;
    }

    /**
     * Get total number of exams
     * @return Count of exams
     */
    public int getExamCount() {
        return examList.size();
    }
}
