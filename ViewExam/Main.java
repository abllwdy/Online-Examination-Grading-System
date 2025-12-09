import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   OEGS - US03 Demo: View Upcoming Exams║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Create sample exams - ALL with FUTURE dates
        Exam exam1 = ExamService.createExam(
            "Midterm Exam - Java Programming",
            "Bring your laptop. Open book exam.",
            LocalDateTime.now().plusDays(3),
            120
        );

        Exam exam2 = ExamService.createExam(
            "Final Exam - Data Structures",
            "Closed book. Calculator allowed.",
            LocalDateTime.now().plusDays(7),
            180
        );

        Exam exam3 = ExamService.createExam(
            "Quiz 1 - Algorithms",
            "Short quiz covering Week 1-3",
            LocalDateTime.now().plusDays(1),
            30
        );

        // Enroll student "S001" in the three upcoming exams
        ExamService.enrollStudentInExam(exam1.getExamId(), "S001");
        ExamService.enrollStudentInExam(exam2.getExamId(), "S001");
        ExamService.enrollStudentInExam(exam3.getExamId(), "S001");

        // Simulate student S001 login and view upcoming exams
        StudentUI.setCurrentStudent("S001");
        StudentUI.viewUpcomingExams();
    }
}
