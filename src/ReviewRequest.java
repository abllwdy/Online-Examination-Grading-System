public class ReviewRequest {
    private final String justification;
    public ReviewRequest(String justification) {
        if (justification == null || justification.trim().isEmpty()) {
            throw new IllegalArgumentException("Justification cannot be empty. Please provide a reason for requesting a review.");
        }
        this.justification = justification.trim();
    }
    public String getJustification() {
        return justification;
    }
}
