import org.junit.Test;
import static org.junit.Assert.*;

public class StudentDashboardTest {
    
    @Test
    public void testIsRequestReviewVisibleWhenPublishedAndGraded() {
        StudentDashboard dashboard = new StudentDashboard();
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        assertTrue(dashboard.isRequestReviewVisible(exam, q));
    }
    
    @Test
    public void testIsRequestReviewVisibleWhenNotPublished() {
        StudentDashboard dashboard = new StudentDashboard();
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        // Not published
        
        assertFalse(dashboard.isRequestReviewVisible(exam, q));
    }
    
    @Test
    public void testIsRequestReviewVisibleWhenUnderReview() {
        StudentDashboard dashboard = new StudentDashboard();
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        ReviewRequest request = new ReviewRequest("I need a review");
        q.markUnderReview(request);
        
        assertFalse(dashboard.isRequestReviewVisible(exam, q));
    }
}

