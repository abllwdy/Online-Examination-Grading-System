package model;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public interface SessionManager {

    String STATUS_READY = "READY";
    String STATUS_IN_PROGRESS = "IN_PROGRESS";
    String STATUS_SUBMITTED = "SUBMITTED";

    void start();

    // US06: Saves the student's answer
    void saveAnswer(int questionIndex, String answer);

    // US10: Submits the exam
    void submit();

    // US09/US10: Gets remaining time
    Duration getTimeRemaining();

    // Getters for state and data
    String getStatus();
    Instant getStartTime();
    String getAnswer(int questionIndex);
    Map<Integer, String> getAnswersMap();
}