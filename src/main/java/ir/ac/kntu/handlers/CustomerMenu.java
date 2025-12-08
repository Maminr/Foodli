package ir.ac.kntu.handlers;

import ir.ac.kntu.managers.SessionManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.User;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public class CustomerMenu extends Menu {

    public CustomerMenu() {
        super("Customer Menu");
        items.add(new MenuItem("1","View Restaurants", TextColor.GREEN,this::handleViewRestaurants));
        items.add(new MenuItem("2","Search Food", TextColor.CYAN,this::handleSearchFood));
        items.add(new MenuItem("3","My Profile", TextColor.YELLOW,this::handleProfile));
        items.add(new MenuItem("4","Order History", TextColor.PURPLE,this::handleOrderHistory));
//        items.add(new MenuItem("0","Logout", TextColor.RED,this::));
    }



    private void handleViewRestaurants() {
        Logger.getInstance().print("Loading restaurants...", TextColor.CYAN);

        Logger.getInstance().print("Feature coming soon: List of Restaurants.");
    }

    private void handleSearchFood() {
        Logger.getInstance().print("Search functionality...", TextColor.CYAN);

    }

    private void handleProfile() {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            Logger.getInstance().error("No user logged in! (Session Error)");
            return;
        }

        Logger.getInstance().print("-----------------------------", TextColor.YELLOW);
        Logger.getInstance().print("       MY PROFILE", TextColor.YELLOW);
        Logger.getInstance().print("-----------------------------", TextColor.YELLOW);
        Logger.getInstance().print("Name: " + currentUser.getName() + " " + currentUser.getLastName());
        Logger.getInstance().print("Phone: " + currentUser.getPhoneNumber());
        Logger.getInstance().print("Role: " + currentUser.getRole());
        Logger.getInstance().print("-----------------------------", TextColor.YELLOW);
    }

    private void handleOrderHistory() {
        Logger.getInstance().print("You have no previous orders.", TextColor.PURPLE);
    }
}