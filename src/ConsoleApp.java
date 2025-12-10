import java.util.Scanner;

public class ConsoleApp {
    private final IReviewWorkflow workflow;
    private final ExamResult exam;
    private final Scanner scanner;

    public ConsoleApp(IReviewWorkflow workflow) {
        this.workflow = workflow;
        this.exam = new ExamResult(1);
        
        // Question 1
        this.exam.addQuestion(new QuestionResult(1, 
            "Explain the concept of polymorphism in object-oriented programming.", 
            "Polymorphism allows objects of different types to be treated as objects of a common base type.",
            10, 7));
        
        // Question 2
        this.exam.addQuestion(new QuestionResult(2, 
            "What is the difference between method overriding and method overloading?",
            "Method overriding occurs when a subclass provides a specific implementation of a method.",
            10, 5));
        
        // Question 3
        this.exam.addQuestion(new QuestionResult(3, 
            "Describe the SOLID principles in software design.",
            "SOLID stands for Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion.",
            10, 8));
        
        // Question 4
        this.exam.addQuestion(new QuestionResult(4,
            "What is the difference between abstract classes and interfaces in Java?",
            "Abstract classes can have both abstract and concrete methods, while interfaces can only have abstract methods.",
            10, 6));
        
        // Question 5
        this.exam.addQuestion(new QuestionResult(5,
            "Explain the concept of encapsulation and why it is important in OOP.",
            "Encapsulation is the bundling of data and methods that operate on that data within a single unit.",
            10, 9));
        
        // Question 6
        this.exam.addQuestion(new QuestionResult(6,
            "What is the difference between checked and unchecked exceptions in Java?",
            "Checked exceptions must be declared in the method signature or handled with try-catch blocks.",
            10, 4));
        
        // Question 7
        this.exam.addQuestion(new QuestionResult(7,
            "Describe the difference between ArrayList and LinkedList. When would you use each?",
            "ArrayList uses a dynamic array internally, providing fast random access but slower insertions/deletions.",
            10, 7));
        
        // Question 8
        this.exam.addQuestion(new QuestionResult(8,
            "What is the purpose of the 'final' keyword in Java? Give examples of its usage.",
            "The 'final' keyword prevents modification: final variables cannot be reassigned, final methods cannot be overridden.",
            10, 8));
        
        // Question 9
        this.exam.addQuestion(new QuestionResult(9,
            "Explain the difference between '==' and '.equals()' in Java.",
            "'==' compares object references, while '.equals()' compares the actual content/values of objects.",
            10, 6));
        
        // Question 10
        this.exam.addQuestion(new QuestionResult(10,
            "What is a design pattern? Name and explain three common design patterns.",
            "Design patterns are reusable solutions to common problems. Three common patterns: Singleton, Factory, and Observer.",
            10, 5));
        
        // Question 11
        this.exam.addQuestion(new QuestionResult(11,
            "What is the difference between 'static' and 'instance' methods? When would you use each?",
            "Static methods belong to the class and can be called without creating an instance. Instance methods belong to an object.",
            10, 9));
        
        // Question 12
        this.exam.addQuestion(new QuestionResult(12,
            "Explain the concept of inheritance and provide an example.",
            "Inheritance allows a class to inherit properties and methods from another class. Example: class Dog extends Animal.",
            10, 7));
        
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Interface: IReviewWorkflow -> " + workflow.getClass().getSimpleName());
        System.out.println("Methods: isRequestReviewVisible, canSubmitReview, submitReview, instructorHasPending, accept, reject, getStatus, finalGrade");
        boolean running = true;
        while (running) {
            System.out.println("Exam " + exam.getExamId() + " | Published: " + exam.isPublished() + " | Final grade: " + workflow.finalGrade(exam));
            System.out.println("1) Publish results");
            System.out.println("2) Student view");
            System.out.println("3) Instructor view");
            System.out.println("4) Show questions");
            System.out.println("0) Exit");
            String choice = scanner.nextLine().trim();
            if ("1".equals(choice)) publish();
            else if ("2".equals(choice)) studentView();
            else if ("3".equals(choice)) instructorView();
            else if ("4".equals(choice)) showQuestions();
            else if ("0".equals(choice)) running = false;
        }
    }

    private void publish() {
        exam.publish();
        System.out.println("Results published");
    }

    private void showQuestions() {
        System.out.println("=== All Questions ===");
        for (QuestionResult q : exam.getQuestions()) {
            System.out.println("Q" + q.getId() + " | Score: " + q.getScore() + 
                             " | Original Score: " + q.getOriginalScore() + 
                             " | Status: " + q.getStatus());
            if (q.getReviewRequest() != null) {
                System.out.println("  Review Request: \"" + q.getReviewRequest().getJustification() + "\"");
            }
        }
    }

    private void studentView() {
        System.out.println("=== Student Dashboard ===");
        for (QuestionResult q : exam.getQuestions()) {
            boolean visible = workflow.isRequestReviewVisible(exam, q.getId());
            String buttonStatus = visible ? "[Request Review button: VISIBLE]" : "[Request Review button: HIDDEN]";
            System.out.println("Q" + q.getId() + " | Score: " + q.getScore() + " | Status: " + q.getStatus() + " | " + buttonStatus);
        }
        System.out.println("\nEnter question id to request review (or blank to return):");
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return;
        try {
            int qid = Integer.parseInt(s);
            if (!workflow.isRequestReviewVisible(exam, qid)) {
                System.out.println("ERROR: Request Review button is not available for this question.");
                System.out.println("Reason: Results may not be published or question is not in GRADED status.");
                return;
            }
            if (!workflow.canSubmitReview(exam, qid)) {
                System.out.println("ERROR: Cannot submit review for this question.");
                return;
            }
            System.out.println("\n=== Request Review Modal ===");
            System.out.println("Please provide a justification for your review request:");
            System.out.print("Justification: ");
            String justification = scanner.nextLine();
            try {
                workflow.submitReview(exam, qid, justification);
                System.out.println("\n✓ Review request submitted successfully!");
                System.out.println("Question status changed to: " + workflow.getStatus(exam, qid));
            } catch (IllegalArgumentException e) {
                System.out.println("\n✗ VALIDATION ERROR: " + e.getMessage());
                System.out.println("Submission prevented. Please provide a non-empty justification.");
            } catch (IllegalStateException e) {
                System.out.println("\n✗ ERROR: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("\n✗ ERROR: " + e.getMessage());
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid question ID. Please enter a number.");
        }
    }

    private void instructorView() {
        System.out.println("=== Instructor Dashboard ===");
        boolean hasPending = workflow.instructorHasPending();
        String indicator = hasPending ? "[DISPUTE PENDING - Notification Received]" : "[No pending disputes]";
        System.out.println("Dispute Pending Indicator: " + indicator);
        
        System.out.println("\nQuestions under review:");
        boolean foundPending = false;
        for (QuestionResult q : exam.getQuestions()) {
            if (q.getStatus() == QuestionStatus.UNDER_REVIEW) {
                foundPending = true;
                ReviewRequest request = q.getReviewRequest();
                String justification = request != null ? request.getJustification() : "N/A";
                System.out.println("  Q" + q.getId() + " | Original Score: " + q.getOriginalScore() + 
                                 " | Current Score: " + q.getScore() + 
                                 " | Status: " + q.getStatus());
                System.out.println("    Justification: \"" + justification + "\"");
            }
        }
        if (!foundPending) {
            System.out.println("  (No questions currently under review)");
        }
        
        System.out.println("\nEnter question id to resolve dispute (or blank to return):");
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return;
        try {
            int qid = Integer.parseInt(s);
            QuestionStatus status = workflow.getStatus(exam, qid);
            if (status != QuestionStatus.UNDER_REVIEW) {
                System.out.println("ERROR: No pending dispute for this question. Status: " + status);
                return;
            }
            
            QuestionResult q = exam.getQuestionById(qid);
            ReviewRequest request = q.getReviewRequest();
            System.out.println("\n=== Resolve Dispute for Q" + qid + " ===");
            System.out.println("Original Score: " + q.getOriginalScore());
            System.out.println("Student Justification: \"" + (request != null ? request.getJustification() : "N/A") + "\"");
            System.out.println("\nOptions:");
            System.out.println("  'a' - Accept dispute and enter new score");
            System.out.println("  'r' - Reject dispute (keep original score)");
            System.out.print("Your choice: ");
            String action = scanner.nextLine().trim().toLowerCase();
            
            if ("a".equals(action)) {
                System.out.print("Enter new score: ");
                try {
                    int newScore = Integer.parseInt(scanner.nextLine().trim());
                    int oldFinalGrade = workflow.finalGrade(exam);
                    workflow.accept(exam, qid, newScore);
                    int newFinalGrade = workflow.finalGrade(exam);
                    System.out.println("\n✓ Dispute ACCEPTED");
                    System.out.println("Question status: " + workflow.getStatus(exam, qid));
                    System.out.println("Final grade updated: " + oldFinalGrade + " → " + newFinalGrade);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid score. Please enter a number.");
                } catch (IllegalStateException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            } else if ("r".equals(action)) {
                int oldFinalGrade = workflow.finalGrade(exam);
                workflow.reject(exam, qid);
                int newFinalGrade = workflow.finalGrade(exam);
                System.out.println("\n✓ Dispute REJECTED");
                System.out.println("Question status: " + workflow.getStatus(exam, qid));
                System.out.println("Original score maintained. Final grade: " + newFinalGrade);
            } else {
                System.out.println("Invalid choice. Please enter 'a' or 'r'.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid question ID. Please enter a number.");
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}
