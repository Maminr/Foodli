package ir.ac.kntu;

import ir.ac.kntu.handlers.MainMenu;
import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.models.enums.TextColor;

public class Main {

    public static void main(String[] args) {
        boolean isDebug = args.length > 1 && args[0].equals("--debug");
        Logger.initialize(isDebug);
        Logger logger = Logger.getInstance();
        logger.info("Application is starting...");

        printSupportCredentials();

        MainMenu startMenu = new MainMenu();
        startMenu.enterMenu();
        logger.info("Application finished. Goodbye!");
    }

    private static void printSupportCredentials() {
        Logger logger = Logger.getInstance();
        logger.print("\n" + "=".repeat(50), TextColor.BLUE);
        logger.print("           SUPPORT ACCOUNT CREDENTIALS", TextColor.BLUE);
        logger.print("=".repeat(50), TextColor.BLUE);
        logger.print("Use these accounts to access Support features:", TextColor.CYAN);
        logger.print("");
        logger.print("Phone: support     | Password: support", TextColor.GREEN);
        logger.print("Phone: 09123456789 | Password: support123", TextColor.GREEN);
        logger.print("Phone: 09129876543 | Password: tech456", TextColor.GREEN);
        logger.print("Phone: 09121234567 | Password: care789", TextColor.GREEN);
        logger.print("");
        logger.print("Multiple Support Accounts Available:", TextColor.YELLOW);
        logger.print("=".repeat(50), TextColor.BLUE);
        logger.print("Press Enter to continue...", TextColor.CYAN);

        try {
            System.in.read();
        } catch (java.io.IOException e) {
            // Ignore input errors
        }
    }
}