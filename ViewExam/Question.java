public class Question {
    private int questionId;
    private String questionText;
    private String questionType; // e.g., "MCQ", "Essay"
    
    public Question(int questionId, String questionText, String questionType) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.questionType = questionType;
    }
    
    // Getters
    public int getQuestionId() { return questionId; }
    public String getQuestionText() { return questionText; }
    public String getQuestionType() { return questionType; }
    
    // Setters
    public void setQuestionText(String questionText) { 
        this.questionText = questionText; 
    }
    public void setQuestionType(String questionType) { 
        this.questionType = questionType; 
    }
}
