/**
 * Question entity class for Multiple Choice Questions
 * Used in exams to store question details and correct answer
 */
public class Question {
    private int questionId;
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;

    public Question(int questionId, String questionText, String[] options, int correctAnswerIndex) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        if (options == null || options.length != 4) {
            throw new IllegalArgumentException("Must provide exactly 4 options");
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex > 3) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and 3");
        }
        
        this.questionId = questionId;
        this.questionText = questionText;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    // Getters
    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options.clone(); // Return copy to prevent external modification
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
    
    /**
     * Check if provided answer is correct
     */
    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }

    @Override
    public String toString() {
        return String.format("Q%d: %s", questionId, questionText);
    }
}
