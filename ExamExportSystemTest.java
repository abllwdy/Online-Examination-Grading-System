import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths; // Correct import for Java 8+
import java.util.ArrayList;
import java.util.List;

class ExamExportSystemTest {

    private static final String TEST_FILENAME = "junit_test_export.csv";

    @AfterEach
    void tearDown() {
        // Cleanup: Deletes the file after every test
        File file = new File(TEST_FILENAME);
        if (file.exists()) file.delete();
    }

    @Test
    void testGradeCalculation() {
        ExamExportSystem.StudentResult student = 
            new ExamExportSystem.StudentResult("S1", "John", 85.5);
        
        // Assert that the CSV string contains the calculated grade "A"
        assertTrue(student.toCSVRow().contains(",A"), "Score 85.5 should be Grade A");
    }

    @Test
    void testFileCreation() throws IOException {
        List<ExamExportSystem.StudentResult> data = new ArrayList<>();
        data.add(new ExamExportSystem.StudentResult("S1", "John", 90.0));
        
        // Run the export
        ExamExportSystem.exportDataToCSV(data, TEST_FILENAME);
        
        // Verify file exists
        assertTrue(new File(TEST_FILENAME).exists(), "File should be created");
        
        // Verify content
        String content = new String(Files.readAllBytes(Paths.get(TEST_FILENAME)));
        assertTrue(content.contains("Student ID,Name"), "Header missing");
        assertTrue(content.contains("S1,John,90.0,A"), "Data row missing");
    }

    @Test
    void testEmptyExport() throws IOException {
        // Run export with empty list
        ExamExportSystem.exportDataToCSV(new ArrayList<>(), TEST_FILENAME);
        
        // Verify only header exists
        List<String> lines = Files.readAllLines(Paths.get(TEST_FILENAME));
        assertEquals(1, lines.size(), "Only header should exist");
    }
}