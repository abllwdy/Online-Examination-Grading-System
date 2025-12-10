public class StudentService implements IStudentService {
    private final INotificationService notifications;
    public StudentService(INotificationService notifications) {
        this.notifications = notifications;
    }
    public void requestReview(ExamResult exam, int questionId, String justification) {
        if (!exam.isPublished()) {
            throw new IllegalStateException("Cannot request review: Exam results have not been published yet.");
        }
        QuestionResult q = exam.getQuestionById(questionId);
        if (q.getStatus() != QuestionStatus.GRADED) {
            throw new IllegalStateException("Cannot request review: Question is not in GRADED status. Current status: " + q.getStatus());
        }
        ReviewRequest request = new ReviewRequest(justification);
        q.markUnderReview(request);
        notifications.addPending();
    }
}
