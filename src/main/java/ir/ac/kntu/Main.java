package ir.ac.kntu;

import ir.ac.kntu.handlers.FirstMenu;
import ir.ac.kntu.helper.Logger; // Import Logger

public class Main {

    public static void main(String[] args) {
        // 1. INITIALIZE LOGGER
        // Set to 'true' to see debug messages, 'false' to hide them.
        boolean isDebug = true;
        Logger.initialize(isDebug);

        // Test the logger immediately
        Logger.getInstance().info("Application is starting...");


        // 2. Create and Enter Menu
        FirstMenu startMenu = new FirstMenu();
        startMenu.enterMenu();

        // 3. Exit Log
        Logger.getInstance().info("Application finished. Goodbye!");
    }
}