public interface INotificationService {
    void addPending();
    void resolvePending();
    boolean hasPending();
}
