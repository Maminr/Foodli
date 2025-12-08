package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.TextColor;

public class MenuItem {
    private final String id;
    private final String text;
    private final TextColor color;
    private final Runnable runnable;

    public MenuItem(String id, String text, Runnable runnable) {
        this(id, text, TextColor.WHITE, runnable);
    }

    public MenuItem(String id, String text, TextColor color, Runnable runnable) {
        this.id = id;
        this.text = text;
        this.color = color;
        this.runnable = runnable;
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
}