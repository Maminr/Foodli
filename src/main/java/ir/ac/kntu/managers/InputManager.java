package ir.ac.kntu.managers;

import java.util.Scanner;

public class InputManager {
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
}