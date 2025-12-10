public interface IReviewWorkflow {
    boolean isRequestReviewVisible(ExamResult exam, int questionId);
    boolean canSubmitReview(ExamResult exam, int questionId);
    void submitReview(ExamResult exam, int questionId, String justification);
    boolean instructorHasPending();
    void accept(ExamResult exam, int questionId, int newScore);
    void reject(ExamResult exam, int questionId);
    QuestionStatus getStatus(ExamResult exam, int questionId);
    int finalGrade(ExamResult exam);
}
