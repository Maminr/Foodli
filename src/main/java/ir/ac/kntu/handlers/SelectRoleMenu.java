package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.models.enums.UserRole;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public class SelectRoleMenu extends Menu {

    private UserRole selectedRole; // Field to store the result

    private static final List<MenuItem> ITEMS = new ArrayList<>();

    static {
        ITEMS.add(new MenuItem("Customer", TextColor.CYAN));
        ITEMS.add(new MenuItem("Restaurant Manager", TextColor.PURPLE));
    }

    public SelectRoleMenu() {
        super(MenuPrinter.printMenu("SELECT YOUR ROLE", ITEMS));
    }

    @Override
    protected boolean handleCommand(String command) {
        switch (command) {
            case "1":
                selectedRole = UserRole.CUSTOMER;
                Logger.getInstance().debug("User selected role: CUSTOMER");
                return true; // Return true to exit the menu loop
            case "2":
                selectedRole = UserRole.RESTAURANT_MANAGER;
                Logger.getInstance().debug("User selected role: RESTAURANT_MANAGER");
                return true; // Return true to exit the menu loop
            default:
                Logger.getInstance().print("Invalid option! Please select 1 or 2.", TextColor.RED);
        }
        return false; // Keep looping if invalid input
    }

    /**
     * Call this method after enterMenu() finishes to get the result.
     */
    public UserRole getSelectedRole() {
        return selectedRole;
    }
}