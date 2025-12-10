import org.junit.Test;
import static org.junit.Assert.*;

public class InstructorDashboardTest {
    
    @Test
    public void testHasDisputePendingWhenNoPending() {
        NotificationService notifications = new NotificationService();
        InstructorDashboard dashboard = new InstructorDashboard(notifications);
        assertFalse(dashboard.hasDisputePending());
    }
    
    @Test
    public void testHasDisputePendingWhenPending() {
        NotificationService notifications = new NotificationService();
        InstructorDashboard dashboard = new InstructorDashboard(notifications);
        notifications.addPending();
        assertTrue(dashboard.hasDisputePending());
    }
    
    @Test
    public void testHasDisputePendingAfterResolve() {
        NotificationService notifications = new NotificationService();
        InstructorDashboard dashboard = new InstructorDashboard(notifications);
        notifications.addPending();
        assertTrue(dashboard.hasDisputePending());
        
        notifications.resolvePending();
        assertFalse(dashboard.hasDisputePending());
    }
}

