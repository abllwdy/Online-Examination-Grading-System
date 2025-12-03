/**
 * Main application entry point for Instructor Dashboard
 * Initializes services and starts the UI
 */
public class InstructorApp {
    public static void main(String[] args) {
        // Initialize service layer
        ExamService examService = new ExamService();
        
        // Initialize UI layer
        InstructorUI ui = new InstructorUI(examService);
        
        // Start the application
        try {
            ui.start();
        } catch (Exception e) {
            System.err.println("Fatal error: Application crashed.");
            Logger.error("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ui.close();
        }
    }
}
