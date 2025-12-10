import org.junit.Test;
import static org.junit.Assert.*;

public class ReviewRequestTest {
    
    @Test
    public void testValidJustification() {
        ReviewRequest request = new ReviewRequest("I believe I deserve more marks for this answer.");
        assertEquals("I believe I deserve more marks for this answer.", request.getJustification());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullJustification() {
        new ReviewRequest(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyJustification() {
        new ReviewRequest("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWhitespaceOnlyJustification() {
        new ReviewRequest("   ");
    }
    
    @Test
    public void testJustificationTrimming() {
        ReviewRequest request = new ReviewRequest("  I need a review  ");
        assertEquals("I need a review", request.getJustification());
    }
    
    @Test
    public void testJustificationWithNewlines() {
        ReviewRequest request = new ReviewRequest("  Line 1\nLine 2  ");
        assertEquals("Line 1\nLine 2", request.getJustification());
    }
}

