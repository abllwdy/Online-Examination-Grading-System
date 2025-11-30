public class Question {
    private int questionId;
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;

    public Question(int questionId, String questionText, String[] options, int correctAnswerIndex) {
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
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
