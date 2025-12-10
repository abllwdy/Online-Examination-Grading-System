//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        INotificationService notifications = new NotificationService();
        IReviewWorkflow workflow = new ReviewWorkflow(notifications);
        
        System.out.println("=== OEGS-18: Request Review Feature ===");
        System.out.println("Interface: IReviewWorkflow");
        System.out.println("Implementation: " + workflow.getClass().getSimpleName());
        System.out.println("Methods: isRequestReviewVisible, canSubmitReview, submitReview, instructorHasPending, accept, reject, getStatus, finalGrade");
        System.out.println();
        
        // Launch GUI interface
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            ReviewRequestGUI gui = new ReviewRequestGUI(workflow);
            gui.setVisible(true);
        });
        
        // Uncomment below to use console interface instead:
        // ConsoleApp app = new ConsoleApp(workflow);
        // app.run();
    }
}
