package ir.ac.kntu.utilities;

import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;

import java.util.List;

public class MenuPrinter {

    private static final int WIDTH = 40;

    public static String getMenuString(String title, List<MenuItem> items) {
        StringBuilder sb = new StringBuilder();

        
        TextColor border = TextColor.CYAN;
        TextColor titleColor = TextColor.YELLOW;
        TextColor reset = TextColor.RESET;

        
        sb.append(border).append("╔").append("═".repeat(WIDTH - 2)).append("╗\n");

        
        sb.append(addCenteredLine(title, border, titleColor));

        
        sb.append(border).append("╠").append("═".repeat(WIDTH - 2)).append("╣\n");

        
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);

            String content = item.getId() + ". " + item.getColor() + item.getText() + border;

            
            int visibleLen = (item.getId() + ". ").length() + item.getText().length();

            sb.append(addLine(content, visibleLen, border));
        }

        
        sb.append(border).append("╚").append("═".repeat(WIDTH - 2)).append("╝\n");
        sb.append(reset).append("Choose an option: ");

        return sb.toString();
    }

    private static String addLine(String content, int visibleLen, TextColor border) {
        int spaces = WIDTH - 2 - 2 - visibleLen;
        if (spaces < 0) spaces = 0;
        return border + "║ " + TextColor.RESET + content + " ".repeat(spaces) + " ║\n";
    }

    private static String addCenteredLine(String text, TextColor border, TextColor innerColor) {
        int textLen = text.length();
        int totalSpace = WIDTH - 2;
        int leftPad = (totalSpace - textLen) / 2;
        int rightPad = totalSpace - textLen - leftPad;

        return border + "║" + " ".repeat(leftPad) + innerColor + text + border + " ".repeat(rightPad) + "║\n";
    }
}