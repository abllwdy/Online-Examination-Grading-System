package UI;

import model.Question;
import model.SessionManager;
import data.DataStore;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ExamPanel extends JPanel {
    private final SessionManager session;
    private final JLabel questionTextLabel;
    private final JLabel statusLabel;
    private final ButtonGroup optionGroup;
    private final JButton nextButton;
    private final JButton prevButton;
    private final JButton submitButton;
    private final JPanel optionsPanel;
    private final JButton startButton; // New Start Button

    private int currentQuestionIndex = 0;

    public ExamPanel(SessionManager sessionManager) {
        this.session = sessionManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Question Text Area (North)
        questionTextLabel = new JLabel("Exam Not Started. Click START.", SwingConstants.LEFT);
        questionTextLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollPane = new JScrollPane(questionTextLabel);
        scrollPane.setPreferredSize(new Dimension(500, 150));
        add(scrollPane, BorderLayout.NORTH);

        // FIX: Initialized statusLabel early
        statusLabel = new JLabel("Ready.", SwingConstants.LEFT);

        // 2. Options Panel (Center)
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            JRadioButton btn = new JRadioButton("Option " + (char)('A' + i));
            btn.setActionCommand(String.valueOf((char)('A' + i)));

            // US06 Logic: Save answer instantly on click
            btn.addActionListener(e -> {
                session.saveAnswer(currentQuestionIndex, e.getActionCommand());
                statusLabel.setText("Answer " + e.getActionCommand() + " saved instantly.");
            });

            optionGroup.add(btn);
            optionsPanel.add(btn);
        }
        add(optionsPanel, BorderLayout.CENTER);

        // 3. Navigation and Status Panel (South)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.add(statusLabel, BorderLayout.NORTH);

        // Navigation and START Buttons
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // US07: START BUTTON (Now handled in GUI)
        startButton = new JButton("START EXAM");
        startButton.setBackground(Color.GREEN.darker());
        startButton.setForeground(Color.WHITE);
        startButton.addActionListener(e -> startExam());
        navPanel.add(startButton);

        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> navigate(-1));
        navPanel.add(prevButton);

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> navigate(1));
        navPanel.add(nextButton);

        submitButton = new JButton("SUBMIT EXAM");
        submitButton.setBackground(Color.RED);
        submitButton.setForeground(Color.WHITE);
        submitButton.addActionListener(e -> submitExam());
        navPanel.add(submitButton);

        footerPanel.add(navPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Initial setup
        updateView();
    }

    // --- US07 Logic Moved to GUI ---
    private void startExam() {
        if (DataStore.questionList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Error: Exam has no questions. Create one first (Option 1 in console).", "Start Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (session.getStatus().equals(SessionManager.STATUS_READY)) {
            try {
                session.start(); // US07: Start the session
                currentQuestionIndex = 0; // Reset to first question
                JOptionPane.showMessageDialog(this, "Exam Session Started!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Start Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        updateView();
    }
    // --------------------------------

    public void updateView() {
        if (session.getStatus().equals(SessionManager.STATUS_IN_PROGRESS) && !DataStore.questionList.isEmpty()) {
            Question q = DataStore.getQuestion(currentQuestionIndex);

            // US11: Update Question Text
            String questionNumber = (currentQuestionIndex + 1) + " of " + DataStore.questionList.size();
            questionTextLabel.setText("<html><b>Q" + questionNumber + ":</b> " + q.getQuestionText() + "</html>");

            // US06: Update Options and Restore Saved Answer
            optionGroup.clearSelection();
            String savedAnswer = session.getAnswer(currentQuestionIndex);
            List<String> options = q.getOptions();

            for (int i = 0; i < optionsPanel.getComponentCount(); i++) {
                JRadioButton btn = (JRadioButton) optionsPanel.getComponent(i);
                String optionText = options.get(i);
                String command = String.valueOf((char)('A' + i));

                btn.setText(command + ") " + optionText);
                btn.setVisible(true);

                if (command.equals(savedAnswer)) {
                    btn.setSelected(true);
                }
            }

        } else {
            questionTextLabel.setText("Exam is " + session.getStatus());

            for (Component comp : optionsPanel.getComponents()) {
                comp.setVisible(false);
            }
        }
        updateNavigationButtons();
    }

    private void updateNavigationButtons() {
        boolean inProgress = session.getStatus().equals(SessionManager.STATUS_IN_PROGRESS);

        // Enable START button only if READY and questions exist
        startButton.setEnabled(!inProgress && DataStore.questionList.size() > 0 &&
                !session.getStatus().equals(SessionManager.STATUS_SUBMITTED));

        // US12: Enable navigation only if IN_PROGRESS
        prevButton.setEnabled(inProgress && currentQuestionIndex > 0);
        nextButton.setEnabled(inProgress && currentQuestionIndex < DataStore.questionList.size() - 1);
        submitButton.setEnabled(inProgress);

        if (session.getStatus().equals(SessionManager.STATUS_SUBMITTED)) {
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            submitButton.setEnabled(false);

            for (Component comp : optionsPanel.getComponents()) {
                if (comp instanceof JRadioButton) {
                    comp.setEnabled(false);
                }
            }
            questionTextLabel.setText("EXAM SUBMITTED. Thank you.");
            statusLabel.setText("Answers are locked.");
        }
    }

    private void navigate(int direction) {
        int newIndex = currentQuestionIndex + direction;
        if (newIndex >= 0 && newIndex < DataStore.questionList.size()) {
            currentQuestionIndex = newIndex;
            updateView();
        }
    }

    private void submitExam() {
        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to submit the exam? You cannot make changes afterwards.",
                "Confirm Submission (US13)",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            session.submit();
            updateView();
        }
    }
}