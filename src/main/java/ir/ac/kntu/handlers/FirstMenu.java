package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.managers.UserManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.User;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.models.enums.UserRole;
import ir.ac.kntu.utilities.MenuPrinter;
import ir.ac.kntu.utilities.PasswordUtils;
import ir.ac.kntu.utilities.StringUtils;

import java.sql.SQLOutput;
import java.util.*;

public class FirstMenu extends Menu {
    // We define the menu appearance in the Constructor

    private static final List<MenuItem> ITEMS = new ArrayList<>();

    static {
        // You can mix and match colors easily now
        ITEMS.add(new MenuItem("Sign In", TextColor.GREEN));
        ITEMS.add(new MenuItem("Sign Up", TextColor.BLUE));
        ITEMS.add(new MenuItem("About Us", TextColor.YELLOW));
        ITEMS.add(new MenuItem("Exit", TextColor.RED));
    }

    public FirstMenu() {
        super(MenuPrinter.printMenu("WELCOME TO FOODLI", ITEMS));
    }

    @Override
    protected boolean handleCommand(String command) {
        switch (command) {
            case "1":
                handleSignIn();
                break;
            case "2":
                handleSignUp();
                break;
            case "3":
                handleAboutUs();
                break;
            case "4":
            case "exit":
                Logger.getInstance().print("Exiting application...", TextColor.RED);
                return true; // This exits the enterMenu() loop
            default:
                Logger.getInstance().print("Invalid option! Please try 1, 2, or 3.", TextColor.RED);
        }
        return false; // Keeps the menu running
    }

    private void handleSignIn() {
        //System.out.println("\n" + YELLOW + "--- SIGN IN PAGE ---" + RESET);
        // Here you would ask for username/password using InputManager
        // Example:
        // System.out.print("Username: ");
        // String user = ir.ac.kntu.managers.InputManager.getInstance().getLine();

        System.out.println("Feature coming soon...\n");
    }

    private void handleSignUp() {
        Logger.getInstance().debug("Entering Sign Up process.");
        Logger.getInstance().print("\n--- SIGN UP PAGE ---", TextColor.YELLOW);

        String name = getName();
        String lastname = getLastname();
        String phoneNumber = getPhoneNumber();
        String password = getPassword();

        if(UserManager.getInstance().findUserByPhoneNumber(phoneNumber) != null) {
            Logger.getInstance().debug("User tried to login with "+phoneNumber+" phone number.");
            Logger.getInstance().print("User already exists with this phone number.", TextColor.RED);
            return;
        }

        SelectRoleMenu roleMenu = new SelectRoleMenu();
        roleMenu.enterMenu();

        UserRole role = roleMenu.getSelectedRole();

        if (role == null) {
            Logger.getInstance().error("No role selected. Sign up cancelled.");
            return;
        }


        Logger.getInstance().debug("New user("+role+") created: " + name + " " + lastname);
        Logger.getInstance().print("Welcome " + name + " " + lastname, TextColor.YELLOW);
    }

    private void handleAboutUs() {
        Logger.getInstance().debug("Opening About Us Menu...");
        AboutUsMenu aboutMenu = new AboutUsMenu();
        aboutMenu.enterMenu();
        Logger.getInstance().debug("Returned to Main Menu.");
    }

    private String getPhoneNumber() {
        while (true) {
            Logger.getInstance().print("Enter your phone number: ");
            String phone = InputManager.getInstance().getLine();

            if (phone.matches("^09\\d{9}$")) {
                return phone;
            } else {
                Logger.getInstance().error("Invalid phone format!");
                Logger.getInstance().print("Phone must start with 09 and be 11 digits (e.g., 09121234567).", TextColor.RED);
            }
        }
    }

    private String getPassword() {
        while (true) {
            Logger.getInstance().print("Enter your password: ");
            String password = InputManager.getInstance().getLine();

            if (PasswordUtils.isStrongPassword(password)) {
                return password;
            } else {
                Logger.getInstance().error("Weak password!");
                Logger.getInstance().print("Password must have 8+ chars, 1 uppercase, 1 lowercase, and 1 number.", TextColor.RED);
                Logger.getInstance().print("Please send stronger password.");
            }
        }
    }

    private String getName() {
        while (true) {
            Logger.getInstance().print("Enter your name: ");
            String name = InputManager.getInstance().getLine();

            if (StringUtils.hasCorrectLength(name, 3, 30)) {
                return name;
            } else {
                Logger.getInstance().error("Invalid name length!");
                Logger.getInstance().print("Name must be between 3 and 20 characters.", TextColor.RED);
            }
        }
    }

    private String getLastname() {
        while (true) {
            Logger.getInstance().print("Enter your lastname: ");
            String lastname = InputManager.getInstance().getLine();

            if (StringUtils.hasCorrectLength(lastname, 3, 50)) {
                return lastname;
            } else {
                Logger.getInstance().error("Invalid lastname length!");
                Logger.getInstance().print("Lastname must be between 3 and 40 characters.", TextColor.RED);
            }
        }
    }
}