
package ui;

import model.SessionManager;
import data.DataStore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;

public class TimerGUI extends JFrame {

    private final SessionManager session;
    private final JLabel timerLabel;
    private final JLabel statusLabel;
    private Timer swingTimer;

    public TimerGUI(SessionManager sessionManager) {
        this.session = sessionManager;

        setTitle("Exam Timer (US09/US10)");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Timer Label ---
        timerLabel = new JLabel("READY", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
        timerLabel.setForeground(Color.BLUE);
        add(timerLabel, BorderLayout.CENTER);

        // --- Status Label (For alerts) ---
        statusLabel = new JLabel("Status: Waiting for Exam Start", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        add(statusLabel, BorderLayout.SOUTH);

        // Initialize the Swing Timer
        setupTimer();

        // Make the window visible
        setVisible(true);
    }

    private void setupTimer() {
        // Create a Swing Timer that fires an action every 1000 milliseconds (1 second)
        swingTimer = new Timer(1000, (ActionEvent e) -> {
            updateTimerDisplay();
        });
        swingTimer.start();
    }

    private void updateTimerDisplay() {
        if (!session.getStatus().equals(SessionManager.STATUS_IN_PROGRESS)) {
            // Update display when not started
            timerLabel.setText(session.getStatus());
            return;
        }

        Duration remaining = session.getTimeRemaining();
        long minutes = remaining.toMinutes();
        long seconds = remaining.minusMinutes(minutes).getSeconds();

        // Format the time as MM:SS
        String timeStr = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(timeStr);

        // --- US09 AC: Low Time Alert ---
        if (minutes < 5) {
            timerLabel.setForeground(Color.RED);
            statusLabel.setText(">>> WARNING: LESS THAN 5 MINUTES LEFT! <<<");
        } else {
            timerLabel.setForeground(Color.BLACK);
            statusLabel.setText("Status: IN PROGRESS");
        }

        // --- US10 AC: Auto-Submit Check ---
        if (remaining.isZero()) {
            swingTimer.stop(); // Stop the timer when time is up
            timerLabel.setText("00:00");
            timerLabel.setForeground(Color.RED);
            statusLabel.setText("--- TIME EXPIRED ---");
            // The Main console loop is still responsible for calling session.submit() 
            // the next time the user interacts, but this visually enforces it.
        }
    }
}