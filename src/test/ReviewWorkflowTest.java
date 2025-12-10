import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewWorkflowTest {
    
    @Test
    public void testIsRequestReviewVisibleBeforePublish() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        assertFalse(workflow.isRequestReviewVisible(exam, 1));
    }
    
    @Test
    public void testIsRequestReviewVisibleAfterPublish() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        assertTrue(workflow.isRequestReviewVisible(exam, 1));
    }
    
    @Test
    public void testIsRequestReviewVisibleWhenUnderReview() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        ReviewRequest request = new ReviewRequest("I need a review");
        q.markUnderReview(request);
        
        assertFalse(workflow.isRequestReviewVisible(exam, 1));
    }
    
    @Test
    public void testCanSubmitReview() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        assertTrue(workflow.canSubmitReview(exam, 1));
    }
    
    @Test
    public void testCannotSubmitReviewWhenNotPublished() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        
        assertFalse(workflow.canSubmitReview(exam, 1));
    }
    
    @Test
    public void testSubmitReview() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        workflow.submitReview(exam, 1, "I need a review");
        
        assertEquals(QuestionStatus.UNDER_REVIEW, workflow.getStatus(exam, 1));
        assertTrue(workflow.instructorHasPending());
    }
    
    @Test
    public void testInstructorHasPending() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        assertFalse(workflow.instructorHasPending());
        
        workflow.submitReview(exam, 1, "I need a review");
        assertTrue(workflow.instructorHasPending());
    }
    
    @Test
    public void testAcceptDispute() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        workflow.submitReview(exam, 1, "I need a review");
        int oldFinalGrade = workflow.finalGrade(exam);
        
        workflow.accept(exam, 1, 9);
        
        assertEquals(QuestionStatus.RESOLVED_ACCEPTED, workflow.getStatus(exam, 1));
        assertEquals(9, workflow.finalGrade(exam));
        assertFalse(workflow.instructorHasPending());
    }
    
    @Test
    public void testRejectDispute() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        exam.addQuestion(q);
        exam.publish();
        
        workflow.submitReview(exam, 1, "I need a review");
        int oldFinalGrade = workflow.finalGrade(exam);
        
        workflow.reject(exam, 1);
        
        assertEquals(QuestionStatus.RESOLVED_REJECTED, workflow.getStatus(exam, 1));
        assertEquals(oldFinalGrade, workflow.finalGrade(exam)); // Should remain same
        assertFalse(workflow.instructorHasPending());
    }
    
    @Test
    public void testFinalGradeCalculation() {
        NotificationService notifications = new NotificationService();
        ReviewWorkflow workflow = new ReviewWorkflow(notifications);
        ExamResult exam = new ExamResult(1);
        QuestionResult q1 = new QuestionResult(1, "Q1", "A1", 10, 7);
        QuestionResult q2 = new QuestionResult(2, "Q2", "A2", 10, 8);
        exam.addQuestion(q1);
        exam.addQuestion(q2);
        
        assertEquals(15, workflow.finalGrade(exam)); // 7 + 8
        
        exam.publish();
        workflow.submitReview(exam, 1, "I need more marks");
        workflow.accept(exam, 1, 9);
        
        assertEquals(17, workflow.finalGrade(exam)); // 9 + 8
    }
}

