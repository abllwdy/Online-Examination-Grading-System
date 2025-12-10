import java.util.ArrayList;
import java.util.List;

public class ExamResult {
    private final int examId;
    private boolean published;
    private final List<QuestionResult> questions;
    private int finalGrade;

    public ExamResult(int examId) {
        this.examId = examId;
        this.published = false;
        this.questions = new ArrayList<>();
        this.finalGrade = 0;
    }

    public void addQuestion(QuestionResult q) {
        questions.add(q);
        recalculateFinalGrade();
    }

    public int getExamId() {
        return examId;
    }

    public boolean isPublished() {
        return published;
    }

    public void publish() {
        this.published = true;
    }

    public List<QuestionResult> getQuestions() {
        return questions;
    }

    public QuestionResult getQuestionById(int id) {
        for (QuestionResult q : questions) {
            if (q.getId() == id) return q;
        }
        throw new IllegalArgumentException("Question not found");
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public void recalculateFinalGrade() {
        int sum = 0;
        for (QuestionResult q : questions) {
            sum += q.getScore();
        }
        finalGrade = sum;
    }
}
