package com.example.createexam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExamService Create Exam Tests")
class ExamServiceTest {
    
    private ExamService examService;
    
    @BeforeEach
    void setUp() {
        examService = new ExamService();
    }
    
    // AC1: Create exam with valid title and instructions
    @Test
    @DisplayName("Should create exam with valid title and instructions")
    void shouldCreateExamWithValidTitleAndInstructions() {
        // Arrange
        String title = "Midterm Exam";
        String instructions = "Answer all questions";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Exam created successfully", result.getMessage());
        assertNotNull(result.getExam());
        assertEquals(1, result.getExam().getExamId());
        assertEquals("Midterm Exam", result.getExam().getTitle());
        assertEquals("Answer all questions", result.getExam().getInstructions());
        assertEquals(1, examService.getExamCount());
    }
    
    @Test
    @DisplayName("Should create exam with valid title and empty instructions")
    void shouldCreateExamWithEmptyInstructions() {
        // Arrange
        String title = "Quiz 1";
        String instructions = "";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.getExam());
        assertEquals("", result.getExam().getInstructions());
    }
    
    @Test
    @DisplayName("Should create exam with null instructions")
    void shouldCreateExamWithNullInstructions() {
        // Arrange
        String title = "Final Exam";
        
        // Act
        ExamCreationResult result = examService.createExam(title, null);
        
        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.getExam());
        assertEquals("", result.getExam().getInstructions());
    }
    
    // AC2: Reject blank title
    @Test
    @DisplayName("Should reject null title")
    void shouldRejectNullTitle() {
        // Arrange
        String title = null;
        String instructions = "Test instructions";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Error: Exam title cannot be empty", result.getMessage());
        assertNull(result.getExam());
        assertEquals(0, examService.getExamCount());
    }
    
    @Test
    @DisplayName("Should reject empty title")
    void shouldRejectEmptyTitle() {
        // Arrange
        String title = "";
        String instructions = "Test instructions";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Error: Exam title cannot be empty", result.getMessage());
        assertNull(result.getExam());
    }
    
    @Test
    @DisplayName("Should reject whitespace-only title")
    void shouldRejectWhitespaceTitle() {
        // Arrange
        String title = "   ";
        String instructions = "Test instructions";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Error: Exam title cannot be empty", result.getMessage());
        assertNull(result.getExam());
    }
    
    // Edge Cases: Title length validation
    @Test
    @DisplayName("Should accept title at maximum length (200 characters)")
    void shouldAcceptTitleAtMaxLength() {
        // Arrange
        String title = "A".repeat(200);
        String instructions = "Test";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals(200, result.getExam().getTitle().length());
    }
    
    @Test
    @DisplayName("Should reject title exceeding maximum length")
    void shouldRejectTitleExceedingMaxLength() {
        // Arrange
        String title = "A".repeat(201);
        String instructions = "Test";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Error: Exam title cannot exceed 200 characters", result.getMessage());
        assertNull(result.getExam());
    }
    
    // Edge Cases: Instructions length validation
    @Test
    @DisplayName("Should accept instructions at maximum length (1000 characters)")
    void shouldAcceptInstructionsAtMaxLength() {
        // Arrange
        String title = "Test Exam";
        String instructions = "B".repeat(1000);
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals(1000, result.getExam().getInstructions().length());
    }
    
    @Test
    @DisplayName("Should reject instructions exceeding maximum length")
    void shouldRejectInstructionsExceedingMaxLength() {
        // Arrange
        String title = "Test Exam";
        String instructions = "B".repeat(1001);
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertFalse(result.isSuccess());
        assertEquals("Error: Instructions cannot exceed 1000 characters", result.getMessage());
        assertNull(result.getExam());
    }
    
    // Edge Cases: Whitespace handling
    @Test
    @DisplayName("Should trim whitespace from title")
    void shouldTrimWhitespaceFromTitle() {
        // Arrange
        String title = "  Test Exam  ";
        String instructions = "Instructions";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Test Exam", result.getExam().getTitle());
    }
    
    @Test
    @DisplayName("Should trim whitespace from instructions")
    void shouldTrimWhitespaceFromInstructions() {
        // Arrange
        String title = "Test Exam";
        String instructions = "  Instructions  ";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals("Instructions", result.getExam().getInstructions());
    }
    
    // Multiple exam creation
    @Test
    @DisplayName("Should create multiple exams with incrementing IDs")
    void shouldCreateMultipleExamsWithIncrementingIds() {
        // Act
        ExamCreationResult result1 = examService.createExam("Exam 1", "Instructions 1");
        ExamCreationResult result2 = examService.createExam("Exam 2", "Instructions 2");
        ExamCreationResult result3 = examService.createExam("Exam 3", "Instructions 3");
        
        // Assert
        assertTrue(result1.isSuccess());
        assertTrue(result2.isSuccess());
        assertTrue(result3.isSuccess());
        assertEquals(1, result1.getExam().getExamId());
        assertEquals(2, result2.getExam().getExamId());
        assertEquals(3, result3.getExam().getExamId());
        assertEquals(3, examService.getExamCount());
    }
    
    // Special characters
    @Test
    @DisplayName("Should accept special characters in title")
    void shouldAcceptSpecialCharactersInTitle() {
        // Arrange
        String title = "Final Exam 2025 - Part A/B (Section #1)";
        String instructions = "Test";
        
        // Act
        ExamCreationResult result = examService.createExam(title, instructions);
        
        // Assert
        assertTrue(result.isSuccess());
        assertEquals(title, result.getExam().getTitle());
    }
}
