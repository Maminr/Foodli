package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.models.enums.UserRole;
import ir.ac.kntu.utilities.MenuPrinter;

import java.util.ArrayList;
import java.util.List;

public class SelectRoleMenu extends Menu {

    private UserRole selectedRole;

    private static final List<MenuItem> ITEMS = new ArrayList<>();


    public SelectRoleMenu() {
        super("SELECT YOUR ROLE");
//        items.add(new MenuItem("1","Customer", TextColor.CYAN,this::handleCommand));
//        items.add(new MenuItem("2","Restaurant Manager", TextColor.PURPLE));
    }

//    @Override
//    protected boolean handleCommand(String command) {
//        switch (command) {
//            case "1":
//                selectedRole = UserRole.CUSTOMER;
//                Logger.getInstance().debug("User selected role: CUSTOMER");
//                return true;
//            case "2":
//                selectedRole = UserRole.RESTAURANT_MANAGER;
//                Logger.getInstance().debug("User selected role: RESTAURANT_MANAGER");
//                return true;
//            default:
//                Logger.getInstance().print("Invalid option! Please select 1 or 2.", TextColor.RED);
//        }
//        return false;
//    }

    /**
     * Call this method after enterMenu() finishes to get the result.
     */
    public UserRole getSelectedRole() {
        return selectedRole;
    }
}