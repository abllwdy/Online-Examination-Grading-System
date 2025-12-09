package test.model;

import model.StudentSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for US06 (Data Preservation).
 * Verifies that answers are correctly saved, retrieved, and overwritten as required by the user story.
 */
public class StudentSessionTest {

    private StudentSession session;
    private final int Q_INDEX_1 = 0; // Represents Question 1
    private final int Q_INDEX_2 = 1; // Represents Question 2

    @BeforeEach
    void setUp() {
        session = new StudentSession();
        // US07 prerequisite: start the session to be in a valid saving state
        session.start();
    }

    // --- POSITIVE TESTS (Core US06 Requirements) ---

    @Test
    void testStandardSaveAndRetrieval() {
        // As a student, my answer must be instantly recorded.
        String answerA = "A";

        // When: An answer is saved for Question 1
        session.saveAnswer(Q_INDEX_1, answerA);

        // Then: Verify the correct answer is retrieved (Data Preservation)
        assertEquals(answerA, session.getAnswer(Q_INDEX_1),
                "Verification: System must correctly save and retrieve the answer for Q1.");
    }

    @Test
    void testAnswerOverwriteIntegrity() {
        // As a student, I must be able to change my answer, and the latest choice must be preserved.
        session.saveAnswer(Q_INDEX_2, "C");
        String newAnswer = "D";

        // When: The student changes the answer (Overwriting)
        session.saveAnswer(Q_INDEX_2, newAnswer);

        // Then: Verify the system returns the new, latest answer.
        assertEquals(newAnswer, session.getAnswer(Q_INDEX_2),
                "Verification: System must overwrite the old answer and retrieve the latest one.");
    }

    @Test
    void testMultipleQuestionIntegrity() {
        // The preservation system must handle multiple questions without data loss.
        session.saveAnswer(Q_INDEX_1, "A");
        session.saveAnswer(Q_INDEX_2, "B");

        // Then: Verify that saving the second answer did not lose the first one.
        assertAll("Verification: All answers must be preserved correctly.",
                () -> assertEquals("A", session.getAnswer(Q_INDEX_1), "Answer for Q1 must be preserved."),
                () -> assertEquals("B", session.getAnswer(Q_INDEX_2), "Answer for Q2 must be preserved.")
        );
    }
}