public class NotificationService implements INotificationService {
    private int pendingCount;
    public void addPending() {
        pendingCount++;
    }
    public void resolvePending() {
        if (pendingCount > 0) pendingCount--;
    }
    public boolean hasPending() {
        return pendingCount > 0;
    }
}
