package ir.ac.kntu.helper;

import ir.ac.kntu.models.enums.TextColor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger instance;
    private boolean isDebugMode;

    
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    
    private Logger(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    
    public static void initialize(boolean isDebugMode) {
        if (instance == null) {
            instance = new Logger(isDebugMode);
        }
    }

    
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger(true); 
            System.err.println("WARNING: Logger not initialized! Defaulting to Debug Mode.");
        }
        return instance;
    }

    

    public void info(String message) {
        logInternal(TextColor.CYAN, "INFO", message);
    }

    public void success(String message) {
        logInternal(TextColor.GREEN, "SUCCESS", message);
    }

    public void error(String message) {
        logInternal(TextColor.RED, "ERROR", message);
    }

    public void debug(String message) {
        if (isDebugMode) {
            logInternal(TextColor.YELLOW, "DEBUG", message);
        }
    }

    /**
     * Custom Log: Prints with Time, but you choose the color.
     * Usage: Logger.getInstance().customLog("My Message", TextColor.PURPLE);
     */
    public void customLog(String message, TextColor color) {
        logInternal(color, "LOG", message);
    }

    

    /**
     * Prints a normal white message (like System.out.println).
     */
    public void print(String message) {
        System.out.println(message);
    }

    /**
     * Prints a raw message in a specific color.
     */
    public void print(String message, TextColor color) {
        System.out.println(color + message + TextColor.RESET);
    }

    

    
    private void logInternal(TextColor color, String tag, String message) {
        String time = LocalTime.now().format(timeFormatter);
        System.out.println(
                TextColor.WHITE + "[" + time + "] " +
                        color + "[" + tag + "]" +
                        TextColor.RESET + " :: " +
                        message
        );
    }
}