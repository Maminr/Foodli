package ir.ac.kntu.handlers;

import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu {
    private InputManager inputManager = InputManager.getInstance();
    private String title;
    protected String menuText;
    protected final List<MenuItem> items = new ArrayList<>();

    public Menu(String title) {
        this.title = title;
    }

    public void enterMenu() {
        String menuString = MenuPrinter.getMenuString(title, items);
        System.out.println(menuString);
        while (true) {
            String input = inputManager.getLine();
            for (MenuItem item : items) {
                if (input.equals(item.getId())) {
                    item.getRunnable().run();
                }
            }
        }
    }

}
