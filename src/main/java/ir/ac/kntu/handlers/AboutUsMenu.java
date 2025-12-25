package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.MenuItemAction;
import ir.ac.kntu.models.enums.TextColor;

public class AboutUsMenu extends Menu {
    private final Logger logger = Logger.getInstance();
    private final InputManager inputManager = InputManager.getInstance();


    public AboutUsMenu() {
        super("ABOUT FOODLI");
        items.add(new MenuItem("1", "Who we are", TextColor.CYAN, this::printWhoWeAre));
        items.add(new MenuItem("2", "Contact Support", TextColor.PURPLE, this::printContactInfo));
        items.add(new MenuItem("3", "Back", TextColor.RED, MenuItemAction.BACK));
    }

    private void printWhoWeAre() {
        logger.print("----------------------------------------", TextColor.CYAN);
        logger.print("Foodli was created in 2025 by:");
        logger.print("1. Amin");
        logger.print("2. AhmadJoon");
        logger.print("We aim to deliver food fast!", TextColor.GREEN);
        logger.print("----------------------------------------", TextColor.CYAN);
        inputManager.pressEnterToContinue();

    }

    private void printContactInfo() {
        Logger.getInstance().print("----------------------------------------", TextColor.PURPLE);
        Logger.getInstance().print("Email: support@foodli.i3r");
        Logger.getInstance().print("Phone: +98 21 8888 8888");
        Logger.getInstance().print("Address: KNTU University, Tehran");
        Logger.getInstance().print("----------------------------------------", TextColor.PURPLE);
        inputManager.pressEnterToContinue();

    }
}