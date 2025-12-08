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

    private static final List<MenuItem> ITEMS = new ArrayList<>();

    static {
        ITEMS.add(new MenuItem("View Restaurants", TextColor.GREEN));
        ITEMS.add(new MenuItem("Search Food", TextColor.CYAN));
        ITEMS.add(new MenuItem("My Profile", TextColor.YELLOW));
        ITEMS.add(new MenuItem("Order History", TextColor.PURPLE));
        ITEMS.add(new MenuItem("Logout", TextColor.RED));
    }

    public CustomerMenu() {
        super(MenuPrinter.printMenu("CUSTOMER DASHBOARD", ITEMS));
    }

    @Override
    protected boolean handleCommand(String command) {
        switch (command) {
            case "1":
                handleViewRestaurants();
                break;
            case "2":
                handleSearchFood();
                break;
            case "3":
                handleProfile();
                break;
            case "4":
                handleOrderHistory();
                break;
            case "5":
            case "exit":
            case "logout":
                // 1. Clear Session
                SessionManager.getInstance().logout();
                // 2. Log action
                Logger.getInstance().info("Logged out successfully.");
                // 3. Return true to break the loop and go back to FirstMenu
                return true;
            default:
                Logger.getInstance().print("Invalid option!", TextColor.RED);
        }
        return false;
    }

    private void handleViewRestaurants() {
        Logger.getInstance().print("Loading restaurants...", TextColor.CYAN);
        // TODO: Call RestaurantManager to show list
        Logger.getInstance().print("Feature coming soon: List of Restaurants.");
    }

    private void handleSearchFood() {
        Logger.getInstance().print("Search functionality...", TextColor.CYAN);
        // TODO: Add search logic
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
        Logger.getInstance().print("Role: " + currentUser.getRole()); // Assuming you have getRole()
        Logger.getInstance().print("-----------------------------", TextColor.YELLOW);
    }

    private void handleOrderHistory() {
        Logger.getInstance().print("You have no previous orders.", TextColor.PURPLE);
    }
}