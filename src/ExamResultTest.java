import org.junit.Test;
import static org.junit.Assert.*;

public class ExamResultTest {
    
    @Test
    public void testExamResultCreation() {
        ExamResult exam = new ExamResult(1);
        assertEquals(1, exam.getExamId());
        assertFalse(exam.isPublished());
        assertEquals(0, exam.getFinalGrade());
        assertTrue(exam.getQuestions().isEmpty());
    }
    
    @Test
    public void testAddQuestion() {
        ExamResult exam = new ExamResult(1);
        QuestionResult q1 = new QuestionResult(1, "Q1", "A1", 10, 8);
        QuestionResult q2 = new QuestionResult(2, "Q2", "A2", 10, 7);
        
        exam.addQuestion(q1);
        exam.addQuestion(q2);
        
        assertEquals(2, exam.getQuestions().size());
        assertEquals(15, exam.getFinalGrade()); // 8 + 7
    }
    
    @Test
    public void testPublish() {
        ExamResult exam = new ExamResult(1);
        assertFalse(exam.isPublished());
        
        exam.publish();
        assertTrue(exam.isPublished());
    }
    
    @Test
    public void testGetQuestionById() {
        ExamResult exam = new ExamResult(1);
        QuestionResult q1 = new QuestionResult(1, "Q1", "A1", 10, 8);
        QuestionResult q2 = new QuestionResult(2, "Q2", "A2", 10, 7);
        
        exam.addQuestion(q1);
        exam.addQuestion(q2);
        
        QuestionResult found = exam.getQuestionById(2);
        assertEquals(2, found.getId());
        assertEquals("Q2", found.getQuestionText());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetQuestionByIdNotFound() {
        ExamResult exam = new ExamResult(1);
        exam.getQuestionById(999);
    }
    
    @Test
    public void testRecalculateFinalGradeAfterAccept() {
        ExamResult exam = new ExamResult(1);
        QuestionResult q1 = new QuestionResult(1, "Q1", "A1", 10, 7);
        QuestionResult q2 = new QuestionResult(2, "Q2", "A2", 10, 8);
        
        exam.addQuestion(q1);
        exam.addQuestion(q2);
        assertEquals(15, exam.getFinalGrade()); // 7 + 8
        
        ReviewRequest request = new ReviewRequest("I need more marks");
        q1.markUnderReview(request);
        q1.accept(9);
        exam.recalculateFinalGrade();
        
        assertEquals(17, exam.getFinalGrade()); // 9 + 8
    }
    
    @Test
    public void testRecalculateFinalGradeAfterReject() {
        ExamResult exam = new ExamResult(1);
        QuestionResult q1 = new QuestionResult(1, "Q1", "A1", 10, 7);
        QuestionResult q2 = new QuestionResult(2, "Q2", "A2", 10, 8);
        
        exam.addQuestion(q1);
        exam.addQuestion(q2);
        assertEquals(15, exam.getFinalGrade());
        
        ReviewRequest request = new ReviewRequest("I need more marks");
        q1.markUnderReview(request);
        q1.reject();
        exam.recalculateFinalGrade();
        
        assertEquals(15, exam.getFinalGrade()); // Should remain same (7 + 8)
    }
}

