import org.junit.Test;
import static org.junit.Assert.*;

public class StudentServiceTest {
    
    @Test
    public void testRequestReviewSuccess() {
        NotificationService notifications = new NotificationService();
        StudentService service = new StudentService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        service.requestReview(exam, 1, "I believe I deserve more marks");
        
        assertEquals(QuestionStatus.UNDER_REVIEW, q.getStatus());
        assertNotNull(q.getReviewRequest());
        assertTrue(notifications.hasPending());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotRequestReviewWhenNotPublished() {
        NotificationService notifications = new NotificationService();
        StudentService service = new StudentService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        // Not published
        
        service.requestReview(exam, 1, "I need a review");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotRequestReviewWhenNotGraded() {
        NotificationService notifications = new NotificationService();
        StudentService service = new StudentService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        ReviewRequest request = new ReviewRequest("First request");
        q.markUnderReview(request); // Status is now UNDER_REVIEW
        
        service.requestReview(exam, 1, "Second request"); // Should fail
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCannotRequestReviewWithEmptyJustification() {
        NotificationService notifications = new NotificationService();
        StudentService service = new StudentService(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        service.requestReview(exam, 1, ""); // Empty justification
    }
}

