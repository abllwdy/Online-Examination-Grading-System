package UI;

import model.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Duration;

import UI.ExamPanel;

public class ExamWindow extends JFrame {

    private final SessionManager session;
    private final JLabel timerLabel;
    private final ExamPanel examPanel;
    private Timer swingTimer;

    public ExamWindow(SessionManager sessionManager) {
        this.session = sessionManager;

        setTitle("Online Examination Grading System (OEGS)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. Timer Panel (North) ---
        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        timerLabel = new JLabel("READY", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerPanel.add(new JLabel("Time Remaining:", SwingConstants.LEFT), BorderLayout.WEST);
        timerPanel.add(timerLabel, BorderLayout.EAST);
        add(timerPanel, BorderLayout.NORTH);

        // --- 2. Exam Panel (Center) ---
        examPanel = new ExamPanel(sessionManager);
        add(examPanel, BorderLayout.CENTER);

        setupTimer();
        setVisible(true);
    }

    private void setupTimer() {
        swingTimer = new Timer(1000, (ActionEvent e) -> {
            updateTimerDisplay();
            examPanel.updateView();
        });
        swingTimer.start();
    }

    private void updateTimerDisplay() {
        if (!session.getStatus().equals(SessionManager.STATUS_IN_PROGRESS)) {
            timerLabel.setText(session.getStatus());
            return;
        }

        Duration remaining = session.getTimeRemaining();
        long minutes = remaining.toMinutes();
        long seconds = remaining.minusMinutes(minutes).getSeconds();

        String timeStr = String.format("%02d:%02d", minutes, seconds);
        timerLabel.setText(timeStr);

        // US09 AC: Low Time Alert Logic
        if (minutes < 5) {
            timerLabel.setForeground(Color.RED);
        } else {
            timerLabel.setForeground(Color.BLACK);
        }

        // US10 AC: Auto-Submit Check
        if (remaining.isZero() && session.getStatus().equals(SessionManager.STATUS_IN_PROGRESS)) {
            timerLabel.setText("00:00");
            swingTimer.stop();
            // Trigger the submission logic
            session.submit();
        }
    }
}