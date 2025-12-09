import model.Question;
import model.SessionManager;
import data.DataStore;
import UI.ExamWindow;

import javax.swing.SwingUtilities;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    private static final DateTimeFormatter CUSTOM_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a")
                    .withZone(ZoneId.systemDefault());

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        // Ensure a test question exists
        if (DataStore.questionList.isEmpty()) {
            try {
                DataStore.questionList.add(new Question("Q1: What is the capital of Malaysia?", List.of("Kuala Lumpur", "Penang", "Johor Bahru", "Melaka"), 0));
                DataStore.questionList.add(new Question("Q2: Which language is Java written in?", List.of("English", "C++", "Java", "Python"), 2));
                System.out.println("SYSTEM: Added two test questions for immediate Start Exam use.");
            } catch (Exception ignored) {}
        }

        // --- US09/US11 INTEGRATION: START GUI WINDOW ---
        SwingUtilities.invokeLater(() -> {
            new ExamWindow(DataStore.currentSession);
        });
        // ----------------------------------------------

        while (true) {
            System.out.println("\n=== OEGS MAIN MENU ===");
            System.out.println("1. Create New Question (US08)");
            System.out.println("2. Check GUI (Start Exam is now in GUI)");
            System.out.println("3. View All Questions (Verify US08)");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = keyboard.nextLine().trim();

            if (choice.equals("1")) {
                createNewQuestion(keyboard);
            }
            else if (choice.equals("2") || choice.equalsIgnoreCase("START")) {
                System.out.println("Please use the 'START EXAM' button in the GUI window.");
            }
            else if (choice.equals("3") || choice.equalsIgnoreCase("VIEW")) {
                viewQuestions();
            } else if (choice.equals("4") || choice.equalsIgnoreCase("EXIT")) {
                System.out.println("Exiting system...");
                break;
            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    // US08: Create New Question
    private static void createNewQuestion(Scanner keyboard) {
        try {
            System.out.println("\n--- Create MCQ ---");
            System.out.print("Enter Question Text: ");
            String text = keyboard.nextLine();

            List<String> options = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                System.out.print("Enter Option " + (char)('A' + i) + ": ");
                options.add(keyboard.nextLine());
            }

            System.out.print("Enter Correct Option Number (0 for A, 1 for B...): ");
            int correctIndex = Integer.parseInt(keyboard.nextLine());

            Question q = new Question(text, options, correctIndex);
            DataStore.addQuestion(q);

        } catch (NumberFormatException e) {
            System.out.println("❌ ERROR: You must enter a number (0-3). Text input rejected.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ FAILED: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ SYSTEM ERROR: " + e.getMessage());
        }
    }

    // View All Questions (Verification)
    private static void viewQuestions() {
        System.out.println("\n--- Current Exam Content (RAM) ---");
        List<Question> all = DataStore.getAllQuestions();
        if (all.isEmpty()) {
            System.out.println("No questions created yet.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                Question q = all.get(i);
                System.out.println("\n--- ID " + i + " ---");
                System.out.println("Q: " + q.getQuestionText());
                List<String> options = q.getOptions();
                for (int j = 0; j < options.size(); j++) {
                    System.out.println("   " + (char)('A' + j) + ") " + options.get(j));
                }
            }
        }
    }
}