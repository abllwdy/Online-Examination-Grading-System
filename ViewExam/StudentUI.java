import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * User Interface for Student operations
 */
public class StudentUI {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static String currentStudentId; // Set after login

    public static void setCurrentStudent(String studentId) {
        currentStudentId = studentId;
    }

    // Main student menu
    public static void showStudentMenu() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       Student Dashboard Menu           ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("1. View Upcoming Exams");
            System.out.println("2. View Completed Exams");
            System.out.println("3. Take Exam");
            System.out.println("4. Logout");
            System.out.print("\nEnter choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewUpcomingExams();
                    break;
                case "2":
                    System.out.println("Feature coming soon (US04)");
                    break;
                case "3":
                    System.out.println("Feature coming soon");
                    break;
                case "4":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("  Invalid choice. Please try again.");
            }
        }
    }

    // US03: View Upcoming Exams
    public static void viewUpcomingExams() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║         Upcoming Exams                 ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (currentStudentId == null) {
            System.out.println("  Error: No student logged in.");
            return;
        }

        List<Exam> upcomingExams = ExamService.getUpcomingExamsForStudent(currentStudentId);

        if (upcomingExams.isEmpty()) {
            System.out.println("\n No upcoming exams found.");
            System.out.println("   You're all caught up! 🎉");
            return;
        }

        System.out.println("\nYou have " + upcomingExams.size() + " upcoming exam(s):\n");
        
        for (int i = 0; i < upcomingExams.size(); i++) {
            Exam exam = upcomingExams.get(i);
            System.out.println("┌─────────────────────────────────────────");
            System.out.println("│ " + (i + 1) + ". " + exam.getTitle());
            System.out.println("├─────────────────────────────────────────");
            System.out.println("│ Scheduled: " + exam.getScheduledDateTime().format(dateFormatter));
            System.out.println("│ Duration:  " + exam.getDurationMinutes() + " minutes");
            System.out.println("│ Exam ID:   " + exam.getExamId());
            
            if (exam.getInstructions() != null && !exam.getInstructions().isEmpty()) {
                System.out.println("│ Instructions: " + exam.getInstructions());
            }
            
            System.out.println("└─────────────────────────────────────────\n");
        }

        System.out.println("\nPress Enter to return to menu...");
        scanner.nextLine();
    }
}
