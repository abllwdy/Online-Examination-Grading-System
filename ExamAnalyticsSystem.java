import java.util.*;
import java.util.stream.Collectors;

public class ExamAnalyticsSystem {

    // ==========================================
    // 1. Data Models
    // ==========================================
    static class Question {
        String id;
        String text;
        String correctAnswer;

        public Question(String id, String text, String correctAnswer) {
            this.id = id;
            this.text = text;
            this.correctAnswer = correctAnswer;
        }
    }

    static class StudentResponse {
        String examId; // We still keep this to prove data integrity (AC3)
        String studentId;
        String questionId;
        String selectedOption; 

        public StudentResponse(String examId, String studentId, String questionId, String selectedOption) {
            this.examId = examId;
            this.studentId = studentId;
            this.questionId = questionId;
            this.selectedOption = selectedOption;
        }
    }

    // ==========================================
    // 2. Service Layer
    // ==========================================
    static class AnalyticsService {
        private List<Question> questions = new ArrayList<>();
        private List<StudentResponse> responseDb = new ArrayList<>();

        public void addQuestion(Question q) { questions.add(q); }
        public void submitResponse(StudentResponse r) { responseDb.add(r); }

        public void generateReport(String activeExamId, String questionId) {
            // 1. Find Question
            Question q = questions.stream()
                    .filter(x -> x.id.equals(questionId)).findFirst().orElse(null);

            if (q == null) {
                System.out.println("[ERROR] Question not found.");
                return;
            }

            // 2. Filter responses for the ACTIVE exam only
            List<StudentResponse> relevantResponses = responseDb.stream()
                    .filter(r -> r.examId.equals(activeExamId) && r.questionId.equals(questionId))
                    .collect(Collectors.toList());

            if (relevantResponses.isEmpty()) {
                System.out.println("No student responses found for " + questionId);
                return;
            }

            // 3. Calc Stats
            int total = relevantResponses.size();
            int correctCount = 0;
            int incorrectCount = 0;
            Map<String, Integer> optionCounts = new HashMap<>();
            
            optionCounts.put("A", 0); optionCounts.put("B", 0);
            optionCounts.put("C", 0); optionCounts.put("D", 0);

            for (StudentResponse r : relevantResponses) {
                optionCounts.put(r.selectedOption, optionCounts.getOrDefault(r.selectedOption, 0) + 1);
                
                if (r.selectedOption.equals(q.correctAnswer)) correctCount++;
                else incorrectCount++;
            }

            // 4. Print Dashboard
            System.out.println("\n------------------------------------------------");
            System.out.println(" ANALYTICS REPORT: " + q.text);
            System.out.println("------------------------------------------------");
            System.out.printf(" ✅ CORRECT:   %d  (%.1f%%)\n", correctCount, ((double)correctCount/total)*100);
            System.out.printf(" ❌ INCORRECT: %d  (%.1f%%)\n", incorrectCount, ((double)incorrectCount/total)*100);
            System.out.println("------------------------------------------------");
            System.out.println(" RESPONSE DISTRIBUTION:");
            System.out.println(" [A]: " + generateBar(optionCounts.get("A")) + " " + optionCounts.get("A"));
            System.out.println(" [B]: " + generateBar(optionCounts.get("B")) + " " + optionCounts.get("B"));
            System.out.println(" [C]: " + generateBar(optionCounts.get("C")) + " " + optionCounts.get("C"));
            System.out.println(" [D]: " + generateBar(optionCounts.get("D")) + " " + optionCounts.get("D"));
            System.out.println("------------------------------------------------");
        }

        private String generateBar(int count) {
            String bar = "";
            for(int i=0; i<count; i++) bar += "█";
            return bar;
        }
    }

    // ==========================================
    // 3. Main Application (Simplified)
    // ==========================================
    public static void main(String[] args) {
        AnalyticsService service = new AnalyticsService();
        Scanner scanner = new Scanner(System.in);

        // --- PRE-SELECTED EXAM CONTEXT ---
        String activeExamId = "CS101"; 

        // Seed Data
        service.addQuestion(new Question("Q1", "What implies a fixed length event in Scrum?", "B")); 
        service.addQuestion(new Question("Q2", "Which role facilitates the Daily Scrum?", "A")); 

        // Seed Responses (Simulated Database)
        // Q1 (Answer B): 3 Correct, 2 Incorrect
        service.submitResponse(new StudentResponse(activeExamId, "S1", "Q1", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S2", "Q1", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S3", "Q1", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S4", "Q1", "A"));
        service.submitResponse(new StudentResponse(activeExamId, "S5", "Q1", "C"));

        // Q2 (Answer A): 1 Correct, 4 Incorrect (Everyone picked B)
        service.submitResponse(new StudentResponse(activeExamId, "S1", "Q2", "A"));
        service.submitResponse(new StudentResponse(activeExamId, "S2", "Q2", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S3", "Q2", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S4", "Q2", "B"));
        service.submitResponse(new StudentResponse(activeExamId, "S5", "Q2", "B"));

        boolean running = true;

        while (running) {
            System.out.println("\n==========================================");
            System.out.println("   ANALYTICS DASHBOARD");
            System.out.println("==========================================");
            System.out.println(" Viewing Results for: " + activeExamId);
            System.out.println("------------------------------------------");
            System.out.println("1. View Q1 Stats (Sprint Length)");
            System.out.println("2. View Q2 Stats (Scrum Roles)");
            System.out.println("3. Exit");
            System.out.print("> Select Question: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    service.generateReport(activeExamId, "Q1");
                    pressEnter(scanner);
                    break;
                case "2":
                    service.generateReport(activeExamId, "Q2");
                    pressEnter(scanner);
                    break;
                case "3":
                    running = false;
                    System.out.println("Closing analytics view...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        scanner.close();
    }

    private static void pressEnter(Scanner s) {
        System.out.println("\n(Press Enter to continue)");
        s.nextLine();
    }
}