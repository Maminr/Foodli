package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.MenuItemAction;
import ir.ac.kntu.models.enums.TextColor;

public class MenuItem {
    private final String id;
    private final String text;
    private final TextColor color;
    private final Runnable runnable;
    private final MenuItemAction action;

    public MenuItem(String id, String text, TextColor textColor, Runnable runnable) {
        this(id, text, textColor, runnable, MenuItemAction.RUN_FUNCTION);
    }

    public MenuItem(String id, String text, TextColor textColor, MenuItemAction menuItemAction) {
        this(id, text, textColor, () -> {
        }, menuItemAction);
    }

    public MenuItem(String id, String text, TextColor color, Runnable runnable, MenuItemAction action) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.runnable = runnable;
        this.action = action;
    }


    public String getText() {
        return text;
    }

    public TextColor getColor() {
        return color;
    }

    public String getId() {
        return id;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public MenuItemAction getActionAfterExecution() {
        return action;
    }
}