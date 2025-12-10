public class InstructorDashboard implements IInstructorDashboard {
    private final INotificationService notifications;
    public InstructorDashboard(INotificationService notifications) {
        this.notifications = notifications;
    }
    public boolean hasDisputePending() {
        return notifications.hasPending();
    }
}
