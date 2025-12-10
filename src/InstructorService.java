public class InstructorService implements IInstructorService {
    private final INotificationService notifications;
    public InstructorService(INotificationService notifications) {
        this.notifications = notifications;
    }
    public void acceptDispute(ExamResult exam, int questionId, int newScore) {
        QuestionResult q = exam.getQuestionById(questionId);
        q.accept(newScore);
        exam.recalculateFinalGrade();
        notifications.resolvePending();
    }
    public void rejectDispute(ExamResult exam, int questionId) {
        QuestionResult q = exam.getQuestionById(questionId);
        q.reject();
        exam.recalculateFinalGrade();
        notifications.resolvePending();
    }
}
