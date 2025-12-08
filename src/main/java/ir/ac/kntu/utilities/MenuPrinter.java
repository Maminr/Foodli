package ir.ac.kntu.utilities;

import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;

import java.util.List;

public class MenuPrinter {

    private static final int WIDTH = 40;

    public static String printMenu(String title, List<MenuItem> items) {
        StringBuilder sb = new StringBuilder();

        // Use the Enum for the Border Color
        TextColor border = TextColor.CYAN;
        TextColor titleColor = TextColor.YELLOW;
        TextColor reset = TextColor.RESET;

        // 1. Top Border
        sb.append(border).append("╔").append("═".repeat(WIDTH - 2)).append("╗\n");

        // 2. Title
        sb.append(printCenteredLine(title, border, titleColor));

        // 3. Divider
        sb.append(border).append("╠").append("═".repeat(WIDTH - 2)).append("╣\n");

        // 4. Items
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            int index = i + 1;

            // "1. " + [COLOR] + "Text" + [BORDER_COLOR]
            // We switch back to 'border' color at the end so the right wall "║" is Cyan
            String content = index + ". " + item.getColor() + item.getText() + border;

            // Calculate length ignoring color codes for alignment
            int visibleLen = (index + ". ").length() + item.getText().length();

            sb.append(printLine(content, visibleLen, border));
        }

        // 5. Bottom
        sb.append(border).append("╚").append("═".repeat(WIDTH - 2)).append("╝\n");
        sb.append(reset).append("Choose an option: ");

        return sb.toString();
    }

    private static String printLine(String content, int visibleLen, TextColor border) {
        int spaces = WIDTH - 2 - 2 - visibleLen;
        if (spaces < 0) spaces = 0;
        return border + "║ " + TextColor.RESET + content + " ".repeat(spaces) + " ║\n";
    }

    private static String printCenteredLine(String text, TextColor border, TextColor innerColor) {
        int textLen = text.length();
        int totalSpace = WIDTH - 2;
        int leftPad = (totalSpace - textLen) / 2;
        int rightPad = totalSpace - textLen - leftPad;

        return border + "║" + " ".repeat(leftPad) + innerColor + text + border + " ".repeat(rightPad) + "║\n";
    }
}