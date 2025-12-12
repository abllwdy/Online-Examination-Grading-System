import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ExamService {
    private static List<Exam> exams = new ArrayList<>();

    // Get upcoming exams for a specific student
    public static List<Exam> getUpcomingExamsForStudent(String studsentId) {
        LocalDateTime now = LocalDateTime.now();
        
        return exams.stream()
            .filter(exam -> exam.getScheduledDateTime().isAfter(now)) // Future exams only
            .filter(exam -> exam.isStudentEnrolled(studentId))        // Enrolled exams only
            .sorted(Comparator.comparing(Exam::getScheduledDateTime)) // Sort by date
            .collect(Collectors.toList());
    }

    // Create exam (from US02)
    public static Exam createExam(String title, String instructions, 
                                   LocalDateTime scheduledDateTime, int durationMinutes) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Exam title cannot be empty");
        }
        if (scheduledDateTime == null || scheduledDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date/time must be in the future");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        Exam exam = new Exam(title, instructions, scheduledDateTime, durationMinutes);
        exams.add(exam);
        return exam;
    }

    // Get exam by ID
    public static Exam getExamById(int examId) {
        return exams.stream()
            .filter(exam -> exam.getExamId() == examId)
            .findFirst()
            .orElse(null);
    }

    // Get all exams
    public static List<Exam> getAllExams() {
        return new ArrayList<>(exams);
    }

    // Enroll student in exam
    public static boolean enrollStudentInExam(int examId, String studentId) {
        Exam exam = getExamById(examId);
        if (exam != null) {
            exam.enrollStudent(studentId);
            return true;
        }
        return false;
    }
}
