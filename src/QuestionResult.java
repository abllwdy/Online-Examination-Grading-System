import java.util.Objects;

public class QuestionResult {
    private final int id;
    private final String questionText;
    private final String studentAnswer;
    private final int maxMarks;
    private final int originalScore;
    private Integer currentScore;
    private QuestionStatus status;
    private ReviewRequest reviewRequest;

    public QuestionResult(int id, String questionText, String studentAnswer, int maxMarks, int originalScore) {
        this.id = id;
        this.questionText = questionText;
        this.studentAnswer = studentAnswer;
        this.maxMarks = maxMarks;
        this.originalScore = originalScore;
        this.currentScore = originalScore;
        this.status = QuestionStatus.GRADED;
    }

    // Backward compatibility constructor
    public QuestionResult(int id, int originalScore) {
        this(id, "Question " + id, "Answer not provided", 10, originalScore);
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public int getOriginalScore() {
        return originalScore;
    }

    public int getScore() {
        return currentScore == null ? originalScore : currentScore;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public ReviewRequest getReviewRequest() {
        return reviewRequest;
    }

    public void markUnderReview(ReviewRequest request) {
        if (status == QuestionStatus.RESOLVED_ACCEPTED || status == QuestionStatus.RESOLVED_REJECTED) {
            throw new IllegalStateException("Dispute already decided");
        }
        if (status == QuestionStatus.UNDER_REVIEW) {
            throw new IllegalStateException("Dispute already pending");
        }
        this.reviewRequest = Objects.requireNonNull(request);
        this.status = QuestionStatus.UNDER_REVIEW;
    }

    public void accept(int newScore) {
        if (status != QuestionStatus.UNDER_REVIEW) {
            throw new IllegalStateException("No pending dispute");
        }
        this.currentScore = newScore;
        this.status = QuestionStatus.RESOLVED_ACCEPTED;
    }

    public void reject() {
        if (status != QuestionStatus.UNDER_REVIEW) {
            throw new IllegalStateException("No pending dispute");
        }
        this.status = QuestionStatus.RESOLVED_REJECTED;
    }
}
