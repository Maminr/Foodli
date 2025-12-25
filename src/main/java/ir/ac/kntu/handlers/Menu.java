package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.MenuItemAction;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
    private Logger logger = Logger.getInstance();
    private InputManager inputManager = InputManager.getInstance();
    private String title;
    protected String menuText;
    protected final List<MenuItem> items = new ArrayList<>();

    public Menu(String title) {
        this.title = title;
    }

    public void enterMenu() {
        String menuString = MenuPrinter.getMenuString(title, items);
        while (true) {
            logger.print(menuString);
            String input = inputManager.getLine();
            MenuItem item = items.stream().filter(i -> i.getId().equals(input)).findFirst().orElse(null);
            if (item != null) {
                item.getRunnable().run();
                if (item.getActionAfterExecution() == MenuItemAction.BACK) {
                    return;
                } else if (item.getActionAfterExecution() == MenuItemAction.EXIT) {
                    System.exit(0);
                }
            } else {
                logger.print("Command is not valid, try again...");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // Ignored
                }
            }
        }
    }

}
