package ir.ac.kntu.managers;

import ir.ac.kntu.helper.Logger;

import java.util.Scanner;

public class InputManager {
    private Logger logger = Logger.getInstance();
    private static final InputManager INSTANCE = new InputManager();
    private final Scanner scanner;
    private InputManager() {
        this.scanner = new Scanner(System.in);
    }

    public static InputManager getInstance() {
        return INSTANCE;
    }

    public String getLine() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }

    public void pressEnterToContinue() {
        logger.print("Press Enter to continue...");
        scanner.nextLine();
    }
}