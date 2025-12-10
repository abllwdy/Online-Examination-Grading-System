public class StudentDashboard implements IStudentDashboard {
    public boolean isRequestReviewVisible(ExamResult exam, QuestionResult question) {
        return exam.isPublished() && question.getStatus() == QuestionStatus.GRADED;
    }
}
