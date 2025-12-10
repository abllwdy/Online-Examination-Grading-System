package com.example.createexam;
/**
 * Main application entry point for Instructor Dashboard
 * Initializes services and starts the UI
 */
public class InstructorApp {
    public static void main(String[] args) {
        ExamService examService = new ExamService();
        InstructorUI ui = new InstructorUI(examService);
        
        try {
            ui.start();
        } catch (Exception e) {
            System.err.println("\n❌ Fatal error: Application crashed.");
            Logger.error("Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            ui.close();
        }
    }
}
