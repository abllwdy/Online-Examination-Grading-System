import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(
            ReviewRequestTest.class,
            QuestionResultTest.class,
            ExamResultTest.class,
            StudentServiceTest.class,
            InstructorServiceTest.class,
            ReviewWorkflowTest.class,
            NotificationServiceTest.class,
            StudentDashboardTest.class,
            InstructorDashboardTest.class
        );
        
        System.out.println("=========================================");
        System.out.println("JUnit Test Results");
        System.out.println("=========================================");
        System.out.println("Total Tests: " + result.getRunCount());
        System.out.println("Passed: " + (result.getRunCount() - result.getFailureCount()));
        System.out.println("Failed: " + result.getFailureCount());
        System.out.println("Time: " + result.getRunTime() + " ms");
        System.out.println("=========================================");
        
        if (result.wasSuccessful()) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("\nFailed Tests:");
            for (Failure failure : result.getFailures()) {
                System.out.println("- " + failure.getTestHeader());
                System.out.println("  " + failure.getMessage());
                System.out.println();
            }
        }
    }
}

