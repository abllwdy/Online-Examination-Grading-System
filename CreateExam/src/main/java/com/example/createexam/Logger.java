package com.example.createexam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.PrintStream;

/**
 * Simple logging utility for application messages
 * Supports INFO, ERROR, and DEBUG log levels
 */
public class Logger {
    private static final boolean DEBUG_MODE = true;
    private static final DateTimeFormatter TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private Logger() {
        // Private constructor to prevent instantiation
    }
    
    public static void info(String message) {
        log("INFO", message, System.out);
    }
    
    public static void error(String message) {
        log("ERROR", message, System.err);
    }
    
    public static void debug(String message) {
        if (DEBUG_MODE) {
            log("DEBUG", message, System.out);
        }
    }
    
    private static void log(String level, String message, PrintStream stream) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        stream.printf("[%s] %s - %s%n", level, timestamp, message);
    }
}
