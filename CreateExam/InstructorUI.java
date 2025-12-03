import java.util.List;
import java.util.Scanner;

/**
 * User Interface class for instructor operations
 * Handles console input/output and user interactions
 */
public class InstructorUI {
    private static final String SEPARATOR = "========================================";
    private static final String LINE = "─────────────────────────────────────";
    
    private final ExamService examService;
    private final Scanner scanner;
    
    public InstructorUI(ExamService examService) {
        this.examService = examService;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        printHeader();
        
        boolean running = true;
        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();
            
            running = handleMenuChoice(choice);
            
            if (running) {
                pressEnterToContinue();
            }
        }
        
        System.out.println("\nExiting application... Goodbye!");
    }
    
    private void printHeader() {
        System.out.println(SEPARATOR);
        System.out.println("   Online Examination & Grading System  ");
        System.out.println("        Instructor Dashboard            ");
        System.out.println(SEPARATOR + "\n");
    }
    
    private void displayMenu() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("           MAIN MENU");
        System.out.println(SEPARATOR);
        System.out.println("1. Create New Exam");
        System.out.println("2. View All Exams");
        System.out.println("3. Search Exams");
        System.out.println("4. Delete Exam");
        System.out.println("5. Exit");
        System.out.println(SEPARATOR);
        System.out.print("Choose an option (1-5): ");
    }
    
    private boolean handleMenuChoice(String choice) {
        switch (choice) {
            case "1":
                handleCreateExam();
                return true;
            case "2":
                handleViewAllExams();
                return true;
            case "3":
                handleSearchExam();
                return true;
            case "4":
                handleDeleteExam();
                return true;
            case "5":
                return false;
            default:
                System.out.println("❌ Invalid option. Please choose 1-5.");
                return true;
        }
    }
    
    /**
     * Handle exam creation (AC1, AC2, AC3)
     */
    private void handleCreateExam() {
        printSectionHeader("CREATE NEW EXAM");
        
        try {
            System.out.print("Enter exam title: ");
            String title = scanner.nextLine();

            System.out.print("Enter exam instructions: ");
            String instructions = scanner.nextLine();

            ExamCreationResult result = examService.createExam(title, instructions);
            
            if (result.isSuccess()) {
                Exam exam = result.getExam();
                System.out.println("\n✅ " + result.getMessage());
                System.out.println("📋 Exam ID: " + exam.getExamId());
                System.out.println("📝 Title: " + exam.getTitle());
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
        printSectionHeader("ALL EXAMS");
        
        List<Exam> exams = examService.getAllExams();

        if (exams.isEmpty()) {
            System.out.println("📭 No exams available.");
        } else {
            System.out.println("Total Exams: " + exams.size() + "\n");
            
            for (int i = 0; i < exams.size(); i++) {
                System.out.println(LINE);
                System.out.println((i + 1) + ". " + exams.get(i).toString());
            }
            System.out.println(LINE);
        }
    }
    
    /**
     * Handle exam search by title
     */
    private void handleSearchExam() {
        printSectionHeader("SEARCH EXAMS");
        
        try {
            System.out.print("Enter search keyword (title): ");
            String keyword = scanner.nextLine();
            
            List<Exam> results = examService.searchExamsByTitle(keyword);
            
            if (results.isEmpty()) {
                System.out.println("\n🔍 No exams found matching \"" + keyword + "\"");
            } else {
                System.out.println("\n✅ Found " + results.size() + " exam(s):\n");
                
                for (int i = 0; i < results.size(); i++) {
                    System.out.println(LINE);
                    System.out.println((i + 1) + ". " + results.get(i).toString());
                }
                System.out.println(LINE);
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
        printSectionHeader("DELETE EXAM");
        
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
            
            Exam examToDelete = examService.getExamById(examId);
            if (examToDelete == null) {
                System.out.println("\n❌ Error: Exam ID " + examId + " not found.");
                return;
            }
            
            if (confirmDeletion(examToDelete.getTitle())) {
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
     * Confirm deletion with user
     */
    private boolean confirmDeletion(String examTitle) {
        System.out.print("\n⚠️  Are you sure you want to delete \"" + examTitle + "\"? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        return confirmation.equals("yes") || confirmation.equals("y");
    }
    
    /**
     * Print section header
     */
    private void printSectionHeader(String title) {
        System.out.println("\n" + SEPARATOR);
        System.out.println("         " + title);
        System.out.println(SEPARATOR);
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
