package model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Question {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {

        // --- AC CHECK: Empty Text ---
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Question text cannot be empty.");
        }

        // --- AC CHECK: Exact 4 Options ---
        if (options == null || options.size() != 4) {
            throw new IllegalArgumentException("Error: MCQ must have exactly 4 options.");
        }

        // --- AC CHECK: Empty Option Text ---
        for (String opt : options) {
            if (opt == null || opt.trim().isEmpty()) {
                throw new IllegalArgumentException("Error: Option text cannot be empty.");
            }
        }

        // --- AC CHECK: Duplicate Options ---
        Set<String> distinctOptions = new HashSet<>(options);
        if (distinctOptions.size() < options.size()) {
            throw new IllegalArgumentException("Error: Duplicate options detected. All options must be distinct.");
        }

        // --- AC CHECK: Valid Index Range (0-3) ---
        if (correctOptionIndex < 0 || correctOptionIndex >= 4) {
            throw new IllegalArgumentException("Error: Correct answer index must be between 0 (A) and 3 (D).");
        }

        this.questionText = questionText.trim();
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    // --- FUTURE USE: For US06 (Answering) & US11 (Grading) ---
    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctOptionIndex;
    }

    public String getQuestionText() { return questionText; }
    public List<String> getOptions() { return options; }

    // Helper to print nicely in terminal
    @Override
    public String toString() {
        return "Q: " + questionText + "\n   A) " + options.get(0) + "\n   B) " + options.get(1) +
                "\n   C) " + options.get(2) + "\n   D) " + options.get(3);
    }
}