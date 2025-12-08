package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public class AboutUsMenu extends Menu {

    private static final List<MenuItem> ITEMS = new ArrayList<>();

    static {
        ITEMS.add(new MenuItem("Who we are", TextColor.CYAN));
        ITEMS.add(new MenuItem("Contact Support", TextColor.PURPLE));
        ITEMS.add(new MenuItem("Back", TextColor.RED)); // 'Back' usually implies Exit for sub-menus
    }

    public AboutUsMenu() {
        super(MenuPrinter.printMenu("ABOUT FOODLI", ITEMS));
    }

    @Override
    protected boolean handleCommand(String command) {
        switch (command) {
            case "1":
                printWhoWeAre();
                break;
            case "2":
                printContactInfo();
                break;
            case "3":
            case "exit":
            case "back":
                // returning TRUE here stops this menu's loop
                // and goes back to FirstMenu
                return true;
            default:
                Logger.getInstance().print("Invalid option!", TextColor.RED);
        }
        return false;
    }

    private void printWhoWeAre() {
        Logger.getInstance().print("----------------------------------------", TextColor.CYAN);
        Logger.getInstance().print("Foodli was created in 2025 by:");
        Logger.getInstance().print("1. Amin");
        Logger.getInstance().print("2. AhmadJoon");
        Logger.getInstance().print("We aim to deliver food fast!", TextColor.GREEN);
        Logger.getInstance().print("----------------------------------------", TextColor.CYAN);
    }

    private void printContactInfo() {
        Logger.getInstance().print("----------------------------------------", TextColor.PURPLE);
        Logger.getInstance().print("Email: support@foodli.i3r");
        Logger.getInstance().print("Phone: +98 21 8888 8888");
        Logger.getInstance().print("Address: KNTU University, Tehran");
        Logger.getInstance().print("----------------------------------------", TextColor.PURPLE);
    }
}