public class ReviewWorkflow implements IReviewWorkflow {
    private final INotificationService notifications;
    private final IStudentDashboard studentDashboard;
    private final IInstructorDashboard instructorDashboard;
    private final IStudentService studentService;
    private final IInstructorService instructorService;

    public ReviewWorkflow(INotificationService notifications) {
        this.notifications = notifications;
        this.studentDashboard = new StudentDashboard();
        this.instructorDashboard = new InstructorDashboard(notifications);
        this.studentService = new StudentService(notifications);
        this.instructorService = new InstructorService(notifications);
    }

    public boolean isRequestReviewVisible(ExamResult exam, int questionId) {
        return studentDashboard.isRequestReviewVisible(exam, exam.getQuestionById(questionId));
    }

    public boolean canSubmitReview(ExamResult exam, int questionId) {
        QuestionStatus s = exam.getQuestionById(questionId).getStatus();
        return exam.isPublished() && s == QuestionStatus.GRADED;
    }

    public void submitReview(ExamResult exam, int questionId, String justification) {
        studentService.requestReview(exam, questionId, justification);
    }

    public boolean instructorHasPending() {
        return instructorDashboard.hasDisputePending();
    }

    public void accept(ExamResult exam, int questionId, int newScore) {
        instructorService.acceptDispute(exam, questionId, newScore);
    }

    public void reject(ExamResult exam, int questionId) {
        instructorService.rejectDispute(exam, questionId);
    }

    public QuestionStatus getStatus(ExamResult exam, int questionId) {
        return exam.getQuestionById(questionId).getStatus();
    }

    public int finalGrade(ExamResult exam) {
        return exam.getFinalGrade();
    }
}
