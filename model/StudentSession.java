package model;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StudentSession implements SessionManager {

    // Set exam duration to 1 minute for easy testing
    private final Duration examDuration = Duration.ofMinutes(1);

    private Instant startTime;
    private String status;
    private Map<Integer, String> answers;

    public StudentSession() {
        this.status = STATUS_READY;
        this.answers = new HashMap<>();
        this.startTime = null;
    }

    @Override
    public void start() {
        if (this.status.equals(STATUS_READY)) {
            this.status = STATUS_IN_PROGRESS;
            this.startTime = Instant.now();
        } else {
            throw new IllegalStateException("Exam is already in progress or submitted.");
        }
    }

    @Override
    public void saveAnswer(int questionIndex, String answer) {
        answers.put(questionIndex, answer);
    }

    @Override
    public Duration getTimeRemaining() {
        if (!status.equals(STATUS_IN_PROGRESS) || startTime == null) {
            return examDuration;
        }
        Duration elapsed = Duration.between(startTime, Instant.now());
        Duration remaining = examDuration.minus(elapsed);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    @Override
    public void submit() {
        if (!status.equals(STATUS_IN_PROGRESS)) {
            throw new IllegalStateException("Exam is not in progress.");
        }
        this.status = STATUS_SUBMITTED;
    }

    @Override
    public String getAnswer(int questionIndex) {
        return answers.get(questionIndex);
    }

    @Override
    public String getStatus() { return status; }
    @Override
    public Instant getStartTime() { return startTime; }
    @Override
    public Map<Integer, String> getAnswersMap() { return answers; }
}