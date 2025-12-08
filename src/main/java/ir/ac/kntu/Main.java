package ir.ac.kntu;

import ir.ac.kntu.handlers.MainMenu;
import ir.ac.kntu.helper.Logger; 

public class Main {

    public static void main(String[] args) {
        boolean isDebug = args.length > 1 && args[0].equals("--debug");
        Logger.initialize(isDebug);
        Logger logger = Logger.getInstance();
        logger.info("Application is starting...");
        MainMenu startMenu = new MainMenu();
        startMenu.enterMenu();
        logger.info("Application finished. Goodbye!");
    }
}