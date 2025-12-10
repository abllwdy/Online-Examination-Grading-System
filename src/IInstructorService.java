public interface IInstructorService {
    void acceptDispute(ExamResult exam, int questionId, int newScore);
    void rejectDispute(ExamResult exam, int questionId);
}
