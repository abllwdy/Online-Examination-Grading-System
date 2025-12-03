/**
 * Question entity class for Multiple Choice Questions
 * Used in exams to store question details and correct answer
 */
public class Question {
    private final int questionId;
    private final String questionText;
    private final String[] options;
    private final int correctAnswerIndex;

    public Question(int questionId, String questionText, String[] options, int correctAnswerIndex) {
        validateInputs(questionText, options, correctAnswerIndex);
        
        this.questionId = questionId;
        this.questionText = questionText.trim();
        this.options = options.clone();
        this.correctAnswerIndex = correctAnswerIndex;
    }

    private void validateInputs(String questionText, String[] options, int correctAnswerIndex) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be empty");
        }
        if (options == null || options.length != 4) {
            throw new IllegalArgumentException("Must provide exactly 4 options");
        }
        for (String option : options) {
            if (option == null || option.trim().isEmpty()) {
                throw new IllegalArgumentException("All options must be non-empty");
            }
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex > 3) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and 3");
        }
    }

    // Getters
    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options.clone();
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
    
    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }

    @Override
    public String toString() {
        return String.format("Q%d: %s", questionId, questionText);
    }
}
