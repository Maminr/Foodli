package ir.ac.kntu.handlers;

import ir.ac.kntu.managers.InputManager;

public abstract class Menu {
    protected String menuText;

    public Menu(String menuText) {
        this.menuText = menuText;
    }

    public void enterMenu() {
        InputManager inputManager = InputManager.getInstance();
        while (true) {
            System.out.println(menuText);

            String command = inputManager.getLine();
            boolean shouldExitMenu = handleCommand(command);
            if (shouldExitMenu)
                return;
        }
    }

    protected abstract boolean handleCommand(String command);
}
