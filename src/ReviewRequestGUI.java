import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReviewRequestGUI extends JFrame {
    private final IReviewWorkflow workflow;
    private final ExamResult exam;
    
    private JPanel mainPanel;
    private JLabel examStatusLabel;
    private JLabel finalGradeLabel;
    private JButton publishButton;
    private JTabbedPane tabbedPane;
    
    // Student View Components
    private JPanel studentPanel;
    private JList<String> studentQuestionList;
    private DefaultListModel<String> studentListModel;
    private JButton requestReviewButton;
    private JTextArea questionDetailArea;
    private JTextArea studentStatusArea;
    
    // Instructor View Components
    private JPanel instructorPanel;
    private JLabel disputePendingLabel;
    private JList<String> instructorQuestionList;
    private DefaultListModel<String> instructorListModel;
    private JTextArea instructorDetailArea;
    private JTextArea instructorStatusArea;
    private JButton acceptButton;
    private JButton rejectButton;
    private JTextField newScoreField;
    
    public ReviewRequestGUI(IReviewWorkflow workflow) {
        this.workflow = workflow;
        this.exam = new ExamResult(1);
        
        // Question 1
        this.exam.addQuestion(new QuestionResult(1, 
            "Explain the concept of polymorphism in object-oriented programming.", 
            "Polymorphism allows objects of different types to be treated as objects of a common base type. It enables one interface to be used for a general class of actions.",
            10, 7));
        
        // Question 2
        this.exam.addQuestion(new QuestionResult(2, 
            "What is the difference between method overriding and method overloading?",
            "Method overriding occurs when a subclass provides a specific implementation of a method that is already defined in its parent class. Method overloading allows multiple methods with the same name but different parameters.",
            10, 5));
        
        // Question 3
        this.exam.addQuestion(new QuestionResult(3, 
            "Describe the SOLID principles in software design.",
            "SOLID stands for Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion principles. These help create maintainable and scalable software.",
            10, 8));
        
        // Question 4
        this.exam.addQuestion(new QuestionResult(4,
            "What is the difference between abstract classes and interfaces in Java?",
            "Abstract classes can have both abstract and concrete methods, while interfaces can only have abstract methods (before Java 8). A class can implement multiple interfaces but extend only one abstract class.",
            10, 6));
        
        // Question 5
        this.exam.addQuestion(new QuestionResult(5,
            "Explain the concept of encapsulation and why it is important in OOP.",
            "Encapsulation is the bundling of data and methods that operate on that data within a single unit (class). It provides data hiding and protects the internal state of objects from unauthorized access.",
            10, 9));
        
        // Question 6
        this.exam.addQuestion(new QuestionResult(6,
            "What is the difference between checked and unchecked exceptions in Java?",
            "Checked exceptions must be declared in the method signature or handled with try-catch blocks. Unchecked exceptions (RuntimeException and its subclasses) do not need to be declared or caught.",
            10, 4));
        
        // Question 7
        this.exam.addQuestion(new QuestionResult(7,
            "Describe the difference between ArrayList and LinkedList. When would you use each?",
            "ArrayList uses a dynamic array internally, providing fast random access but slower insertions/deletions. LinkedList uses a doubly-linked list, providing fast insertions/deletions but slower random access.",
            10, 7));
        
        // Question 8
        this.exam.addQuestion(new QuestionResult(8,
            "What is the purpose of the 'final' keyword in Java? Give examples of its usage.",
            "The 'final' keyword prevents modification: final variables cannot be reassigned, final methods cannot be overridden, and final classes cannot be extended. Example: final int MAX_SIZE = 100;",
            10, 8));
        
        // Question 9
        this.exam.addQuestion(new QuestionResult(9,
            "Explain the difference between '==' and '.equals()' in Java.",
            "'==' compares object references (memory addresses), while '.equals()' compares the actual content/values of objects. For String objects, always use .equals() to compare values.",
            10, 6));
        
        // Question 10
        this.exam.addQuestion(new QuestionResult(10,
            "What is a design pattern? Name and explain three common design patterns.",
            "Design patterns are reusable solutions to common problems in software design. Three common patterns: Singleton (ensures only one instance), Factory (creates objects without specifying exact class), and Observer (notifies multiple objects of state changes).",
            10, 5));
        
        // Question 11
        this.exam.addQuestion(new QuestionResult(11,
            "What is the difference between 'static' and 'instance' methods? When would you use each?",
            "Static methods belong to the class and can be called without creating an instance. Instance methods belong to an object and require an instance. Use static for utility methods, instance for methods that operate on object state.",
            10, 9));
        
        // Question 12
        this.exam.addQuestion(new QuestionResult(12,
            "Explain the concept of inheritance and provide an example.",
            "Inheritance allows a class to inherit properties and methods from another class. The child class (subclass) extends the parent class (superclass). Example: class Dog extends Animal { }",
            10, 7));
        
        initializeGUI();
        updateAllViews();
    }
    
    private void initializeGUI() {
        setTitle("OEGS-18: Request Review System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Top Panel - Exam Info
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Center Panel - Tabbed Interface
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Student View", createStudentPanel());
        tabbedPane.addTab("Instructor View", createInstructorPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBorder(new TitledBorder("Exam Information"));
        
        examStatusLabel = new JLabel("Status: Not Published");
        examStatusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        finalGradeLabel = new JLabel("Final Grade: 0");
        finalGradeLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        publishButton = new JButton("Publish Results");
        publishButton.addActionListener(e -> {
            exam.publish();
            updateAllViews();
            JOptionPane.showMessageDialog(this, "Exam results have been published!", 
                "Published", JOptionPane.INFORMATION_MESSAGE);
        });
        
        panel.add(examStatusLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(finalGradeLabel);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(publishButton);
        
        return panel;
    }
    
    private JPanel createStudentPanel() {
        studentPanel = new JPanel(new BorderLayout(10, 10));
        studentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Question List (Left Side)
        studentListModel = new DefaultListModel<>();
        studentQuestionList = new JList<>(studentListModel);
        studentQuestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentQuestionList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        studentQuestionList.addListSelectionListener(e -> updateQuestionDetail());
        
        JScrollPane listScroll = new JScrollPane(studentQuestionList);
        listScroll.setBorder(new TitledBorder("Select Question"));
        listScroll.setPreferredSize(new Dimension(250, 400));
        
        // Question Detail Panel (Right Side)
        JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
        
        // Question Detail Area
        questionDetailArea = new JTextArea(15, 50);
        questionDetailArea.setEditable(false);
        questionDetailArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        questionDetailArea.setBorder(new TitledBorder("Question Details"));
        questionDetailArea.setBackground(new Color(245, 245, 245));
        JScrollPane detailScroll = new JScrollPane(questionDetailArea);
        
        // Request Review Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        requestReviewButton = new JButton("Request Review for This Question");
        requestReviewButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        requestReviewButton.setPreferredSize(new Dimension(300, 35));
        requestReviewButton.addActionListener(e -> handleRequestReview());
        buttonPanel.add(requestReviewButton);
        
        // Status Area (Bottom)
        studentStatusArea = new JTextArea(5, 50);
        studentStatusArea.setEditable(false);
        studentStatusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        studentStatusArea.setBorder(new TitledBorder("Status Information"));
        JScrollPane statusScroll = new JScrollPane(studentStatusArea);
        
        // Layout
        detailPanel.add(detailScroll, BorderLayout.CENTER);
        detailPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(detailPanel, BorderLayout.CENTER);
        rightPanel.add(statusScroll, BorderLayout.SOUTH);
        
        studentPanel.add(listScroll, BorderLayout.WEST);
        studentPanel.add(rightPanel, BorderLayout.CENTER);
        
        return studentPanel;
    }
    
    private JPanel createInstructorPanel() {
        instructorPanel = new JPanel(new BorderLayout(10, 10));
        instructorPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Dispute Pending Indicator
        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indicatorPanel.setBorder(new TitledBorder("Notifications"));
        disputePendingLabel = new JLabel("No Disputes Pending");
        disputePendingLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        disputePendingLabel.setForeground(Color.GRAY);
        indicatorPanel.add(disputePendingLabel);
        
        // Question List
        instructorListModel = new DefaultListModel<>();
        instructorQuestionList = new JList<>(instructorListModel);
        instructorQuestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        instructorQuestionList.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        instructorQuestionList.addListSelectionListener(e -> updateInstructorDetail());
        
        JScrollPane listScroll = new JScrollPane(instructorQuestionList);
        listScroll.setBorder(new TitledBorder("Questions Under Review"));
        listScroll.setPreferredSize(new Dimension(250, 300));
        
        // Question Detail Area
        instructorDetailArea = new JTextArea(12, 50);
        instructorDetailArea.setEditable(false);
        instructorDetailArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        instructorDetailArea.setBorder(new TitledBorder("Question & Answer Details"));
        instructorDetailArea.setBackground(new Color(245, 245, 245));
        JScrollPane detailScroll = new JScrollPane(instructorDetailArea);
        
        // Action Panel
        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBorder(new TitledBorder("Resolve Dispute"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        actionPanel.add(new JLabel("New Score:"), gbc);
        gbc.gridx = 1;
        newScoreField = new JTextField(10);
        actionPanel.add(newScoreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        acceptButton = new JButton("Accept Dispute");
        acceptButton.addActionListener(e -> handleAcceptDispute());
        rejectButton = new JButton("Reject Dispute");
        rejectButton.addActionListener(e -> handleRejectDispute());
        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);
        actionPanel.add(buttonPanel, gbc);
        
        // Status Area
        instructorStatusArea = new JTextArea(6, 40);
        instructorStatusArea.setEditable(false);
        instructorStatusArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        instructorStatusArea.setBorder(new TitledBorder("Status Information"));
        JScrollPane statusScroll = new JScrollPane(instructorStatusArea);
        
        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.add(listScroll, BorderLayout.CENTER);
        leftPanel.add(actionPanel, BorderLayout.SOUTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.add(detailScroll, BorderLayout.CENTER);
        rightPanel.add(statusScroll, BorderLayout.SOUTH);
        
        instructorPanel.add(indicatorPanel, BorderLayout.NORTH);
        instructorPanel.add(leftPanel, BorderLayout.WEST);
        instructorPanel.add(rightPanel, BorderLayout.CENTER);
        
        return instructorPanel;
    }
    
    private void handleRequestReview() {
        int selectedIndex = studentQuestionList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question first.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int questionId = selectedIndex + 1;
        
        if (!workflow.isRequestReviewVisible(exam, questionId)) {
            JOptionPane.showMessageDialog(this, 
                "Request Review button is not available for this question.\n" +
                "Reason: Results may not be published or question is not in GRADED status.", 
                "Not Available", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!workflow.canSubmitReview(exam, questionId)) {
            JOptionPane.showMessageDialog(this, 
                "Cannot submit review for this question.", 
                "Cannot Submit", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Show modal dialog for justification
        JTextArea justificationArea = new JTextArea(5, 30);
        justificationArea.setLineWrap(true);
        justificationArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(justificationArea);
        
        int result = JOptionPane.showConfirmDialog(this, 
            new Object[]{"Please provide a justification for your review request:", scrollPane},
            "Request Review", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String justification = justificationArea.getText().trim();
            
            try {
                workflow.submitReview(exam, questionId, justification);
                JOptionPane.showMessageDialog(this, 
                    "Review request submitted successfully!\n" +
                    "Question status changed to: " + workflow.getStatus(exam, questionId), 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                updateAllViews();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    "VALIDATION ERROR: " + e.getMessage() + "\n" +
                    "Submission prevented. Please provide a non-empty justification.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, 
                    "ERROR: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleAcceptDispute() {
        int selectedIndex = instructorQuestionList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question first.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int questionId = getQuestionIdFromInstructorList(selectedIndex);
        
        if (workflow.getStatus(exam, questionId) != QuestionStatus.UNDER_REVIEW) {
            JOptionPane.showMessageDialog(this, 
                "No pending dispute for this question. Status: " + workflow.getStatus(exam, questionId), 
                "No Pending Dispute", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String scoreText = newScoreField.getText().trim();
        if (scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a new score.", 
                "Missing Score", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int newScore = Integer.parseInt(scoreText);
            int oldFinalGrade = workflow.finalGrade(exam);
            workflow.accept(exam, questionId, newScore);
            int newFinalGrade = workflow.finalGrade(exam);
            
            JOptionPane.showMessageDialog(this, 
                "Dispute ACCEPTED\n" +
                "Question status: " + workflow.getStatus(exam, questionId) + "\n" +
                "Final grade updated: " + oldFinalGrade + " → " + newFinalGrade, 
                "Accepted", JOptionPane.INFORMATION_MESSAGE);
            
            newScoreField.setText("");
            updateAllViews();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid score. Please enter a number.", 
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRejectDispute() {
        int selectedIndex = instructorQuestionList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a question first.", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int questionId = getQuestionIdFromInstructorList(selectedIndex);
        
        if (workflow.getStatus(exam, questionId) != QuestionStatus.UNDER_REVIEW) {
            JOptionPane.showMessageDialog(this, 
                "No pending dispute for this question. Status: " + workflow.getStatus(exam, questionId), 
                "No Pending Dispute", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to reject this dispute?\n" +
            "The original score will be maintained.", 
            "Confirm Rejection", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            int oldFinalGrade = workflow.finalGrade(exam);
            workflow.reject(exam, questionId);
            int newFinalGrade = workflow.finalGrade(exam);
            
            JOptionPane.showMessageDialog(this, 
                "Dispute REJECTED\n" +
                "Question status: " + workflow.getStatus(exam, questionId) + "\n" +
                "Original score maintained. Final grade: " + newFinalGrade, 
                "Rejected", JOptionPane.INFORMATION_MESSAGE);
            
            updateAllViews();
        }
    }
    
    private void updateInstructorDetail() {
        int selectedIndex = instructorQuestionList.getSelectedIndex();
        if (selectedIndex == -1) {
            instructorDetailArea.setText("Please select a question to view details.");
            return;
        }
        
        int questionId = getQuestionIdFromInstructorList(selectedIndex);
        QuestionResult q = exam.getQuestionById(questionId);
        ReviewRequest request = q.getReviewRequest();
        
        StringBuilder detail = new StringBuilder();
        detail.append("===========================================================\n");
        detail.append("QUESTION ").append(q.getId()).append(" - UNDER REVIEW\n");
        detail.append("===========================================================\n\n");
        
        detail.append("QUESTION:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(q.getQuestionText()).append("\n\n");
        
        detail.append("STUDENT ANSWER:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(q.getStudentAnswer()).append("\n\n");
        
        detail.append("SCORING:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(String.format("Original Score: %d / %d\n", q.getOriginalScore(), q.getMaxMarks()));
        detail.append(String.format("Current Score: %d / %d\n", q.getScore(), q.getMaxMarks()));
        detail.append(String.format("Status: %s\n\n", q.getStatus()));
        
        if (request != null) {
            detail.append("STUDENT JUSTIFICATION:\n");
            detail.append("-----------------------------------------------------------\n");
            detail.append("\"").append(request.getJustification()).append("\"\n");
        }
        
        instructorDetailArea.setText(detail.toString());
    }
    
    private int getQuestionIdFromInstructorList(int index) {
        // Extract question ID from the list item
        String item = instructorListModel.getElementAt(index);
        // Format: "Q1 | 7/10 marks | Status: UNDER_REVIEW"
        int start = item.indexOf('Q') + 1;
        int end = item.indexOf(' ', start);
        return Integer.parseInt(item.substring(start, end));
    }
    
    private void updateAllViews() {
        updateTopPanel();
        updateStudentView();
        updateInstructorView();
    }
    
    private void updateTopPanel() {
        String status = exam.isPublished() ? "Published" : "Not Published";
        examStatusLabel.setText("Status: " + status);
        examStatusLabel.setForeground(exam.isPublished() ? Color.GREEN.darker() : Color.RED);
        
        finalGradeLabel.setText("Final Grade: " + workflow.finalGrade(exam));
        
        publishButton.setEnabled(!exam.isPublished());
    }
    
    private void updateQuestionDetail() {
        int selectedIndex = studentQuestionList.getSelectedIndex();
        if (selectedIndex == -1) {
            questionDetailArea.setText("Please select a question to view details.");
            return;
        }
        
        QuestionResult q = exam.getQuestions().get(selectedIndex);
        boolean visible = workflow.isRequestReviewVisible(exam, q.getId());
        
        StringBuilder detail = new StringBuilder();
        detail.append("===========================================================\n");
        detail.append("QUESTION ").append(q.getId()).append("\n");
        detail.append("===========================================================\n\n");
        
        detail.append("QUESTION:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(q.getQuestionText()).append("\n\n");
        
        detail.append("YOUR ANSWER:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(q.getStudentAnswer()).append("\n\n");
        
        detail.append("MARKS:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append(String.format("Marks Received: %d / %d\n", q.getScore(), q.getMaxMarks()));
        detail.append(String.format("Percentage: %.1f%%\n\n", (q.getScore() * 100.0 / q.getMaxMarks())));
        
        detail.append("STATUS:\n");
        detail.append("-----------------------------------------------------------\n");
        detail.append("Current Status: ").append(q.getStatus()).append("\n");
        if (visible) {
            detail.append("Request Review: AVAILABLE [YES]\n");
        } else {
            detail.append("Request Review: NOT AVAILABLE (Results not published or already reviewed)\n");
        }
        
        if (q.getReviewRequest() != null) {
            detail.append("\nREVIEW REQUEST:\n");
            detail.append("-----------------------------------------------------------\n");
            detail.append("Justification: \"").append(q.getReviewRequest().getJustification()).append("\"\n");
        }
        
        questionDetailArea.setText(detail.toString());
        
        // Update button state
        requestReviewButton.setEnabled(visible && workflow.canSubmitReview(exam, q.getId()));
    }
    
    private void updateStudentView() {
        studentListModel.clear();
        studentStatusArea.setText("");
        
        StringBuilder statusText = new StringBuilder();
        statusText.append("=== Student Dashboard Summary ===\n\n");
        
        for (QuestionResult q : exam.getQuestions()) {
            boolean visible = workflow.isRequestReviewVisible(exam, q.getId());
            String item = String.format("Q%d - %d/%d marks - %s", 
                q.getId(), q.getScore(), q.getMaxMarks(), q.getStatus());
            studentListModel.addElement(item);
            
            statusText.append(String.format("Q%d:\n", q.getId()));
            statusText.append(String.format("  Question: %s\n", 
                q.getQuestionText().substring(0, Math.min(50, q.getQuestionText().length())) + "..."));
            statusText.append(String.format("  Marks: %d / %d\n", q.getScore(), q.getMaxMarks()));
            statusText.append(String.format("  Status: %s\n", q.getStatus()));
            statusText.append(String.format("  Request Review: %s\n", 
                visible ? "AVAILABLE" : "NOT AVAILABLE"));
            
            if (q.getReviewRequest() != null) {
                statusText.append(String.format("  Justification: \"%s\"\n", 
                    q.getReviewRequest().getJustification()));
            }
            statusText.append("\n");
        }
        
        studentStatusArea.setText(statusText.toString());
        
        // Update detail if a question is selected
        if (studentQuestionList.getSelectedIndex() != -1) {
            updateQuestionDetail();
        } else {
            questionDetailArea.setText("Please select a question from the list to view details.");
        }
    }
    
    private void updateInstructorView() {
        instructorListModel.clear();
        instructorStatusArea.setText("");
        
        boolean hasPending = workflow.instructorHasPending();
        if (hasPending) {
            disputePendingLabel.setText("⚠ DISPUTE PENDING - Notification Received");
            disputePendingLabel.setForeground(Color.RED);
        } else {
            disputePendingLabel.setText("No Disputes Pending");
            disputePendingLabel.setForeground(Color.GRAY);
        }
        
        StringBuilder statusText = new StringBuilder();
        statusText.append("=== Instructor Dashboard ===\n\n");
        statusText.append("Dispute Pending Indicator: " + 
            (hasPending ? "[DISPUTE PENDING - Notification Received]" : "[No pending disputes]") + "\n\n");
        
        boolean foundPending = false;
        for (QuestionResult q : exam.getQuestions()) {
            if (q.getStatus() == QuestionStatus.UNDER_REVIEW) {
                foundPending = true;
                ReviewRequest request = q.getReviewRequest();
                String justification = request != null ? request.getJustification() : "N/A";
                String item = String.format("Q%d | %d/%d marks | Status: %s", 
                    q.getId(), q.getScore(), q.getMaxMarks(), q.getStatus());
                instructorListModel.addElement(item);
                
                statusText.append(String.format("Q%d (Under Review):\n", q.getId()));
                statusText.append(String.format("  Question: %s\n", q.getQuestionText()));
                statusText.append(String.format("  Student Answer: %s\n", q.getStudentAnswer()));
                statusText.append(String.format("  Original Score: %d / %d\n", q.getOriginalScore(), q.getMaxMarks()));
                statusText.append(String.format("  Current Score: %d / %d\n", q.getScore(), q.getMaxMarks()));
                statusText.append(String.format("  Status: %s\n", q.getStatus()));
                statusText.append(String.format("  Student Justification: \"%s\"\n\n", justification));
            }
            
            statusText.append(String.format("Q%d:\n", q.getId()));
            statusText.append(String.format("  Question: %s\n", 
                q.getQuestionText().substring(0, Math.min(60, q.getQuestionText().length())) + "..."));
            statusText.append(String.format("  Score: %d / %d (Original: %d / %d)\n", 
                q.getScore(), q.getMaxMarks(), q.getOriginalScore(), q.getMaxMarks()));
            statusText.append(String.format("  Status: %s\n", q.getStatus()));
            if (q.getStatus() == QuestionStatus.RESOLVED_ACCEPTED) {
                statusText.append("  ✓ Dispute was ACCEPTED\n");
            } else if (q.getStatus() == QuestionStatus.RESOLVED_REJECTED) {
                statusText.append("  ✗ Dispute was REJECTED\n");
            }
            statusText.append("\n");
        }
        
        if (!foundPending) {
            statusText.append("(No questions currently under review)\n");
        }
        
        int totalMarks = 0;
        for (QuestionResult q : exam.getQuestions()) {
            totalMarks += q.getMaxMarks();
        }
        statusText.append(String.format("\nFinal Grade: %d / %d", 
            workflow.finalGrade(exam), totalMarks));
        
        instructorStatusArea.setText(statusText.toString());
        
        // Update detail if a question is selected
        if (instructorQuestionList.getSelectedIndex() != -1) {
            updateInstructorDetail();
        } else {
            instructorDetailArea.setText("Please select a question from the list to view details.");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            INotificationService notifications = new NotificationService();
            IReviewWorkflow workflow = new ReviewWorkflow(notifications);
            ReviewRequestGUI gui = new ReviewRequestGUI(workflow);
            gui.setVisible(true);
        });
    }
}

