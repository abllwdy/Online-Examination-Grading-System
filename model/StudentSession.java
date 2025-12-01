package model;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class StudentSession {
    public static final String STATUS_READY = "READY";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_SUBMITTED = "SUBMITTED";

    // Tracks when the exam officially started (for US09/US10 timer logic)
    private Instant startTime;

    private String status;

    // Key: Question Index, Value: Answer (e.g., "A")
    private Map<Integer, String> answers;

    public StudentSession() {
        this.status = STATUS_READY;
        this.answers = new HashMap<>();
        this.startTime = null;
    }

    // AC: Sets status from 'Ready' to 'In Progress' and initializes timer
    public void start() {
        if (this.status.equals(STATUS_READY)) {
            this.status = STATUS_IN_PROGRESS;
            this.startTime = Instant.now();
        } else {
            // This throws the IllegalStateException needed for Test 4
            throw new IllegalStateException("Exam is already in progress or submitted.");
        }
    }

    // Getters for status checking and timer logic
    public String getStatus() { return status; }
    public Instant getStartTime() { return startTime; }

    // NOTE: More methods for US06 (saving answers) will be added later.
}