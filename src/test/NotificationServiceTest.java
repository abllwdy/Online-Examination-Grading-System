import org.junit.Test;
import static org.junit.Assert.*;

public class NotificationServiceTest {
    
    @Test
    public void testInitialState() {
        NotificationService service = new NotificationService();
        assertFalse(service.hasPending());
    }
    
    @Test
    public void testAddPending() {
        NotificationService service = new NotificationService();
        service.addPending();
        assertTrue(service.hasPending());
    }
    
    @Test
    public void testMultiplePending() {
        NotificationService service = new NotificationService();
        service.addPending();
        service.addPending();
        assertTrue(service.hasPending());
    }
    
    @Test
    public void testResolvePending() {
        NotificationService service = new NotificationService();
        service.addPending();
        assertTrue(service.hasPending());
        
        service.resolvePending();
        assertFalse(service.hasPending());
    }
    
    @Test
    public void testResolveMultiplePending() {
        NotificationService service = new NotificationService();
        service.addPending();
        service.addPending();
        service.addPending();
        
        service.resolvePending();
        assertTrue(service.hasPending()); // Still has pending
        
        service.resolvePending();
        assertTrue(service.hasPending()); // Still has pending
        
        service.resolvePending();
        assertFalse(service.hasPending()); // All resolved
    }
    
    @Test
    public void testResolveWhenNoPending() {
        NotificationService service = new NotificationService();
        service.resolvePending(); // Should not throw exception
        assertFalse(service.hasPending());
    }
}

