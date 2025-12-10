import org.junit.Test;
import static org.junit.Assert.*;

public class InstructorServiceTest {
    
    @Test
    public void testAcceptDispute() {
        NotificationService notifications = new NotificationService();
        InstructorService service = new InstructorService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        ReviewRequest request = new ReviewRequest("I need more marks");
        q.markUnderReview(request);
        notifications.addPending();
        
        int oldFinalGrade = exam.getFinalGrade();
        service.acceptDispute(exam, 1, 9);
        
        assertEquals(QuestionStatus.RESOLVED_ACCEPTED, q.getStatus());
        assertEquals(9, q.getScore());
        assertEquals(7, q.getOriginalScore()); // Original preserved
        assertEquals(9, exam.getFinalGrade()); // Updated
        assertFalse(notifications.hasPending()); // Notification resolved
    }
    
    @Test
    public void testRejectDispute() {
        NotificationService notifications = new NotificationService();
        InstructorService service = new InstructorService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        ReviewRequest request = new ReviewRequest("I need more marks");
        q.markUnderReview(request);
        notifications.addPending();
        
        int oldFinalGrade = exam.getFinalGrade();
        service.rejectDispute(exam, 1);
        
        assertEquals(QuestionStatus.RESOLVED_REJECTED, q.getStatus());
        assertEquals(7, q.getScore()); // Original score maintained
        assertEquals(7, exam.getFinalGrade()); // Unchanged
        assertFalse(notifications.hasPending()); // Notification resolved
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotAcceptWhenNotUnderReview() {
        NotificationService notifications = new NotificationService();
        InstructorService service = new InstructorService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        service.acceptDispute(exam, 1, 9); // Should throw exception
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotRejectWhenNotUnderReview() {
        NotificationService notifications = new NotificationService();
        InstructorService service = new InstructorService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        service.rejectDispute(exam, 1); // Should throw exception
    }
}

