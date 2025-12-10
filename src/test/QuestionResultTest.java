import org.junit.Test;
import static org.junit.Assert.*;

public class QuestionResultTest {
    
    @Test
    public void testQuestionResultCreation() {
        QuestionResult q = new QuestionResult(1, "What is Java?", "Java is a programming language", 10, 8);
        assertEquals(1, q.getId());
        assertEquals("What is Java?", q.getQuestionText());
        assertEquals("Java is a programming language", q.getStudentAnswer());
        assertEquals(10, q.getMaxMarks());
        assertEquals(8, q.getOriginalScore());
        assertEquals(8, q.getScore());
        assertEquals(QuestionStatus.GRADED, q.getStatus());
    }
    
    @Test
    public void testBackwardCompatibilityConstructor() {
        QuestionResult q = new QuestionResult(1, 7);
        assertEquals(1, q.getId());
        assertEquals(7, q.getOriginalScore());
        assertEquals(7, q.getScore());
        assertEquals(QuestionStatus.GRADED, q.getStatus());
    }
    
    @Test
    public void testMarkUnderReview() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 8);
        ReviewRequest request = new ReviewRequest("I need a review");
        
        q.markUnderReview(request);
        
        assertEquals(QuestionStatus.UNDER_REVIEW, q.getStatus());
        assertNotNull(q.getReviewRequest());
        assertEquals("I need a review", q.getReviewRequest().getJustification());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotMarkUnderReviewWhenAlreadyUnderReview() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 8);
        ReviewRequest request1 = new ReviewRequest("First request");
        ReviewRequest request2 = new ReviewRequest("Second request");
        
        q.markUnderReview(request1);
        q.markUnderReview(request2); // Should throw exception
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotMarkUnderReviewWhenResolvedAccepted() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 8);
        ReviewRequest request = new ReviewRequest("Request");
        
        q.markUnderReview(request);
        q.accept(9);
        
        ReviewRequest request2 = new ReviewRequest("Second request");
        q.markUnderReview(request2); // Should throw exception
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotMarkUnderReviewWhenResolvedRejected() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 8);
        ReviewRequest request = new ReviewRequest("Request");
        
        q.markUnderReview(request);
        q.reject();
        
        ReviewRequest request2 = new ReviewRequest("Second request");
        q.markUnderReview(request2); // Should throw exception
    }
    
    @Test
    public void testAcceptDispute() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        ReviewRequest request = new ReviewRequest("I deserve more marks");
        
        q.markUnderReview(request);
        q.accept(9);
        
        assertEquals(QuestionStatus.RESOLVED_ACCEPTED, q.getStatus());
        assertEquals(9, q.getScore());
        assertEquals(7, q.getOriginalScore()); // Original should be preserved
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotAcceptWhenNotUnderReview() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        q.accept(9); // Should throw exception
    }
    
    @Test
    public void testRejectDispute() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        ReviewRequest request = new ReviewRequest("I deserve more marks");
        
        q.markUnderReview(request);
        q.reject();
        
        assertEquals(QuestionStatus.RESOLVED_REJECTED, q.getStatus());
        assertEquals(7, q.getScore()); // Original score maintained
        assertEquals(7, q.getOriginalScore());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testCannotRejectWhenNotUnderReview() {
        QuestionResult q = new QuestionResult(1, "Question", "Answer", 10, 7);
        q.reject(); // Should throw exception
    }
}

