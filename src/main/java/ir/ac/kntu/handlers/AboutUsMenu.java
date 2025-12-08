package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public class AboutUsMenu extends Menu {


    public AboutUsMenu() {
        super("ABOUT FOODLI");
        items.add(new MenuItem("1","Who we are", TextColor.CYAN,this::printWhoWeAre));
        items.add(new MenuItem("2","Contact Support", TextColor.PURPLE,this::printContactInfo));
//        items.add(new MenuItem("3","Back", TextColor.RED,this::handleViewRestaurants));
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