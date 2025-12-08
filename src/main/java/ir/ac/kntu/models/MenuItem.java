package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.TextColor;

public class MenuItem {
    private final String text;
    private final TextColor color;

    public MenuItem(String text) {
        this(text, TextColor.WHITE);
    }

    public MenuItem(String text, TextColor color) {
        this.text = text;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public TextColor getColor() {
        return color;
    }
}