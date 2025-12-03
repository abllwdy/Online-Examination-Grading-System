import java.util.List;
import java.util.Scanner;

/**
 * User Interface class for instructor operations
 * Handles console input/output and user interactions
 */
public class InstructorUI {
    private ExamService examService;
    private Scanner scanner;
    
    public InstructorUI(ExamService examService) {
        this.examService = examService;
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Start the instructor UI loop
     */
    public void start() {
        boolean running = true;
        
        System.out.println("========================================");
        System.out.println("   Online Examination & Grading System  ");
        System.out.println("        Instructor Dashboard            ");
        System.out.println("========================================\n");
        
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    handleCreateExam();
                    break;
                case "2":
                    handleViewAllExams();
                    break;
                case "3":
                    handleSearchExam();
                    break;
                case "4":
                    handleDeleteExam();
                    break;
                case "5":
                    running = false;
                    System.out.println("\nExiting application... Goodbye!");
                    break;
                default:
                    System.out.println("❌ Invalid option. Please choose 1-5.");
            }
            
            if (running) {
                pressEnterToContinue();
            }
        }
        
        scanner.close();
    }
    
    /**
     * Display main menu options
     */
    private void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("           MAIN MENU");
        System.out.println("========================================");
        System.out.println("1. Create New Exam");
        System.out.println("2. View All Exams");
        System.out.println("3. Search Exams");
        System.out.println("4. Delete Exam");
        System.out.println("5. Exit");
        System.out.println("========================================");
        System.out.print("Choose an option (1-5): ");
    }
    
    /**
     * Handle exam creation (AC1, AC2, AC3)
     */
    private void handleCreateExam() {
        System.out.println("\n========================================");
        System.out.println("         CREATE NEW EXAM");
        System.out.println("========================================");
        
        try {
            System.out.print("Enter exam title: ");
            String title = scanner.nextLine();

            System.out.print("Enter exam instructions: ");
            String instructions = scanner.nextLine();

            ExamCreationResult result = examService.createExam(title, instructions);
            
            if (result.isSuccess()) {
                System.out.println("\n✅ " + result.getMessage());
                System.out.println("📋 Exam ID: " + result.getExam().getExamId());
                System.out.println("📝 Title: " + result.getExam().getTitle());
                System.out.println("📚 Total exams: " + examService.getExamCount());
            } else {
                System.out.println("\n❌ " + result.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("\n❌ Error: An unexpected error occurred.");
            Logger.error("Exception in handleCreateExam: " + e.getMessage());
        }
    }
    
    /**
     * Handle viewing all exams (AC3)
     */
    private void handleViewAllExams() {
        System.out.println("\n========================================");
        System.out.println("           ALL EXAMS");
        System.out.println("========================================");
        
        List<Exam> exams = examService.getAllExams();

        if (exams.isEmpty()) {
            System.out.println("📭 No exams available.");
        } else {
            System.out.println("Total Exams: " + exams.size() + "\n");
            
            for (int i = 0; i < exams.size(); i++) {
                Exam exam = exams.get(i);
                System.out.println("─────────────────────────────────────");
                System.out.println((i + 1) + ". " + exam.toString());
            }
            System.out.println("─────────────────────────────────────");
        }
    }
    
    /**
     * Handle exam search by title
     */
    private void handleSearchExam() {
        System.out.println("\n========================================");
        System.out.println("         SEARCH EXAMS");
        System.out.println("========================================");
        
        try {
            System.out.print("Enter search keyword (title): ");
            String keyword = scanner.nextLine();
            
            List<Exam> results = examService.searchExamsByTitle(keyword);
            
            if (results.isEmpty()) {
                System.out.println("\n🔍 No exams found matching \"" + keyword + "\"");
            } else {
                System.out.println("\n✅ Found " + results.size() + " exam(s):\n");
                
                for (int i = 0; i < results.size(); i++) {
                    Exam exam = results.get(i);
                    System.out.println("─────────────────────────────────────");
                    System.out.println((i + 1) + ". " + exam.toString());
                }
                System.out.println("─────────────────────────────────────");
            }
            
        } catch (Exception e) {
            System.out.println("\n❌ Error: Search failed.");
            Logger.error("Exception in handleSearchExam: " + e.getMessage());
        }
    }
    
    /**
     * Handle exam deletion
     */
    private void handleDeleteExam() {
        System.out.println("\n========================================");
        System.out.println("         DELETE EXAM");
        System.out.println("========================================");
        
        // First show all exams
        List<Exam> exams = examService.getAllExams();
        
        if (exams.isEmpty()) {
            System.out.println("📭 No exams available to delete.");
            return;
        }
        
        System.out.println("Available Exams:\n");
        for (int i = 0; i < exams.size(); i++) {
            Exam exam = exams.get(i);
            System.out.println((i + 1) + ". ID: " + exam.getExamId() + " - " + exam.getTitle());
        }
        
        try {
            System.out.print("\nEnter Exam ID to delete (0 to cancel): ");
            String input = scanner.nextLine().trim();
            
            if (input.equals("0")) {
                System.out.println("❌ Deletion cancelled.");
                return;
            }
            
            int examId = Integer.parseInt(input);
            
            // Confirm deletion
            Exam examToDelete = examService.getExamById(examId);
            if (examToDelete == null) {
                System.out.println("\n❌ Error: Exam ID " + examId + " not found.");
                return;
            }
            
            System.out.print("\n⚠️  Are you sure you want to delete \"" + examToDelete.getTitle() + "\"? (yes/no): ");
            String confirmation = scanner.nextLine().trim().toLowerCase();
            
            if (confirmation.equals("yes") || confirmation.equals("y")) {
                boolean deleted = examService.deleteExam(examId);
                if (deleted) {
                    System.out.println("\n✅ Exam deleted successfully.");
                    System.out.println("📚 Remaining exams: " + examService.getExamCount());
                } else {
                    System.out.println("\n❌ Error: Could not delete exam.");
                }
            } else {
                System.out.println("\n❌ Deletion cancelled.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Error: Invalid Exam ID. Please enter a number.");
        } catch (Exception e) {
            System.out.println("\n❌ Error: An unexpected error occurred.");
            Logger.error("Exception in handleDeleteExam: " + e.getMessage());
        }
    }
    
    /**
     * Pause and wait for user to press Enter
     */
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clean up resources
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
