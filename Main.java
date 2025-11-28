import model.Question;
import data.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== US08: INSTRUCTOR MENU ===");
            System.out.println("1. Create New Question");
            System.out.println("2. View All Questions (Verify Save)");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            String choice = keyboard.nextLine();

            if (choice.equals("1")) {
                createNewQuestion(keyboard);
            } else if (choice.equals("2")) {
                viewQuestions();
            } else if (choice.equals("3")) {
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

    private static void viewQuestions() {
        System.out.println("\n--- Current Exam Content (RAM) ---");
        List<Question> all = DataStore.getAllQuestions();
        if (all.isEmpty()) {
            System.out.println("No questions created yet.");
        } else {
            for (int i = 0; i < all.size(); i++) {
                System.out.println("ID " + i + ": " + all.get(i).getQuestionText());
            }
        }
    }
}