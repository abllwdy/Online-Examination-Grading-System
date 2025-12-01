import model.Question;
import model.StudentSession;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import data.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    private static final DateTimeFormatter CUSTOM_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                    .withZone(ZoneId.systemDefault());
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        if (DataStore.questionList.isEmpty()) {
            try {
                // Ensure a valid question is loaded for US07 testing
                DataStore.questionList.add(new Question("Q1: What is the capital of Malaysia?", List.of("Kuala Lumpur", "Penang", "Johor Bahru", "Melaka"), 0));
                System.out.println("SYSTEM: Added one test question for immediate Start Exam use.");
            } catch (Exception ignored) {}
        }

        while (true) {
            // Updated Menu Flow with 'View' restored
            System.out.println("\n=== OEGS MAIN MENU ===");
            System.out.println("1. Create New Question (US08)");
            System.out.println("2. Start Exam (US07)");
            System.out.println("3. View All Questions (Verify US08)"); // RESTORED OPTION
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = keyboard.nextLine().trim();

            if (choice.equals("1")) {
                createNewQuestion(keyboard);
            } else if (choice.equals("2") || choice.equalsIgnoreCase("START")) {
                startExam(keyboard); // New US07 Logic
            } else if (choice.equals("3") || choice.equalsIgnoreCase("VIEW")) { // RESTORED CALL
                viewQuestions();
            } else if (choice.equals("4") || choice.equalsIgnoreCase("EXIT")) {
                System.out.println("Exiting system...");
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    // The Logic for US08
    private static void createNewQuestion(Scanner keyboard) {
        try {
            System.out.println("\n--- Create MCQ ---");

            // 1. Get Text (Validates not empty later)
            System.out.print("Enter Question Text: ");
            String text = keyboard.nextLine();

            // 2. Get 4 Options
            List<String> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter Option " + (char)('A' + i) + ": ");
                String opt = keyboard.nextLine();
                options.add(opt);
            }

            // 3. Get Correct Answer (AC Negative Test: Non-numeric input)
            System.out.print("Enter Correct Option Number (0 for A, 1 for B...): ");
            int correctIndex = -1;

            try {
                correctIndex = Integer.parseInt(keyboard.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ ERROR: You must enter a number (0-3). Text input rejected.");
                return; // Stop function
            }

            // 4. Build & Save (Triggers all Question.java validations)
            Question q = new Question(text, options, correctIndex);

            // 5. Save to "Database"
            DataStore.addQuestion(q);

        } catch (IllegalArgumentException e) {
            // Catches: Empty text, Duplicates, Invalid Index
            System.out.println("❌ FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ SYSTEM ERROR: " + e.getMessage());
        }
    }
    // Final commit to test Jira link
    private static void viewQuestions() {
        System.out.println("\n--- Current Exam Content (RAM) ---");
        List<Question> all = DataStore.getAllQuestions();

        if (all.isEmpty()) {
            System.out.println("No questions created yet.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                Question q = all.get(i);

                // Print Separator and Question ID/Text
                System.out.println("\n--- ID " + i + " ---");
                System.out.println("Q: " + q.getQuestionText());

                // Print Options (A, B, C, D)
                List<String> options = q.getOptions();
                for (int j = 0; j < options.size(); j++) {
                    System.out.println("   " + (char)('A' + j) + ") " + options.get(j));
                }
            }
        }
    }

    // US07: Start Exam Logic (New Sprint 2 Task)
    private static void startExam(Scanner keyboard) {

        // AC: Block start if questionList is empty (Test 2)
        if (DataStore.questionList.isEmpty()) {
            System.out.println("❌ Error: Exam has no questions. Create one first (Option 1).");
            return;
        }

        // AC: Block start if already SUBMITTED (Test 3)
        if (DataStore.currentSession.getStatus().equals(StudentSession.STATUS_SUBMITTED)) {
            System.out.println("❌ You have already completed this exam.");
            return;
        }

        try {
            // AC: Initializes time and status to IN_PROGRESS (Test 1)
            DataStore.currentSession.start();

            // AC: System must clear the console and display Q1
            System.out.println("\n\n================================================");
            System.out.println("✅ Exam Session Started! Status: IN PROGRESS");
            System.out.println("================================================\n");

            Question firstQuestion = DataStore.questionList.get(0);
            System.out.println("Q1: " + firstQuestion.getQuestionText());

            // Displays start time (part of Test 6 verification)
            System.out.println("Started at: " + CUSTOM_FORMATTER.format(DataStore.currentSession.getStartTime()));

            // --- This is where US06 (Answering loop) will be implemented next ---

        } catch (IllegalStateException e) {
            // Catches the error when trying to start twice (Test 4)
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}