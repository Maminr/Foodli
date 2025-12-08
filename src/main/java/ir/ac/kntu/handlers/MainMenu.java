package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.managers.UserManager;
import ir.ac.kntu.models.Customer;
import ir.ac.kntu.models.Manager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.User;
import ir.ac.kntu.models.enums.MenuItemAction;
import ir.ac.kntu.models.enums.MenuType;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.models.enums.UserRole;
import ir.ac.kntu.utilities.PasswordUtils;
import ir.ac.kntu.utilities.StringUtils;

public class MainMenu extends Menu {
    private final UserManager userManager = UserManager.getInstance();
    private final Logger logger = Logger.getInstance();
    private final InputManager inputManager = InputManager.getInstance();

    public MainMenu() {
        super("Main Menu");
        items.add(new MenuItem("1", "Sign In", TextColor.GREEN, this::handleSignIn));
        items.add(new MenuItem("2", "Sign Up", TextColor.BLUE, this::handleSignUp));
        items.add(new MenuItem("3", "About Us", TextColor.YELLOW, this::handleAboutUs));
        items.add(new MenuItem("0", "Exit", TextColor.RED, MenuItemAction.EXIT));
    }

    private void handleSignIn() {
        logger.debug("Entering Sign In process.");
        logger.print("\n--- SIGN IN PAGE ---", TextColor.YELLOW);
        String phoneNumber = getPhoneNumber();

        logger.print("Enter your password: ");
        String password = inputManager.getLine();

        User loggedInUser = userManager.signInUser(phoneNumber, password);

        if (loggedInUser == null) {
            logger.print("Phone number or Password is incorrect!", TextColor.RED);
        } else {
            logger.success("Login successful! Welcome " + loggedInUser.getName());

            if (loggedInUser instanceof Customer) {
                MenuHandler.getInstance().loadMenu(MenuType.CUSTOMER_MENU);
            } else if (loggedInUser instanceof Manager) {
                logger.print("Manager menu coming soon...");
            } else {
                logger.print("Admin menu coming soon...");
            }
        }
    }

    private void handleSignUp() {
        logger.debug("Entering Sign Up process.");
        logger.print("\n--- SIGN UP PAGE ---", TextColor.YELLOW);

        String name = getName();
        String lastname = getLastname();
        String phoneNumber = getPhoneNumber();

        if (userManager.findUserByPhoneNumber(phoneNumber) != null) {
            logger.debug("User tried to login with " + phoneNumber + " phone number.");
            logger.print("User already exists with this phone number.", TextColor.RED);
            return;
        }

        String password = getPassword();


        //todo: get role from input
        UserRole role = UserRole.CUSTOMER;


        if (role == null) {
            logger.error("No role selected. Sign up cancelled.");
            return;
        }

        if (role == UserRole.CUSTOMER) {
            if (userManager.signUpCustomer(name, lastname, phoneNumber, password) == null) {
                logger.error("Failed to sign up customer.");
                return;
            }
        } else if (role == UserRole.RESTAURANT_MANAGER) {
            if (userManager.signUpManager(name, lastname, phoneNumber, password) == null) {
                logger.error("Failed to sign up manager.");
                return;
            }
        }


        logger.debug("New user(" + role + ") created: " + name + " " + lastname);
        logger.print("Welcome " + name + " " + lastname, TextColor.YELLOW);

        if (role == UserRole.CUSTOMER) {
            logger.debug("Opening " + role + " Menu...");
            MenuHandler.getInstance().loadMenu(MenuType.CUSTOMER_MENU);
        }

    }

    private void handleAboutUs() {
        logger.debug("Opening About Us Menu...");
        MenuHandler.getInstance().loadMenu(MenuType.ABOUT_US_MENU);
        logger.debug("Returned to Main Menu.");
    }

    private String getPhoneNumber() {
        while (true) {
            logger.print("Enter your phone number: ");
            String phone = inputManager.getLine();

            if (phone.matches("^09\\d{9}$")) {
                return phone;
            } else {
                logger.error("Invalid phone format!");
                logger.print("Phone must start with 09 and be 11 digits (e.g., 09121234567).", TextColor.RED);
            }
        }
    }

    private String getPassword() {
        while (true) {
            logger.print("Enter your password: ");
            String password = inputManager.getLine();

            if (PasswordUtils.isStrongPassword(password)) {
                return password;
            } else {
                logger.error("Weak password!");
                logger.print("Password must have 8+ chars, 1 uppercase, 1 lowercase, and 1 number.", TextColor.RED);
                logger.print("Please send stronger password.");
            }
        }
    }

    private String getName() {
        while (true) {
            logger.print("Enter your name: ");
            String name = inputManager.getLine();

            if (StringUtils.hasCorrectLength(name, 3, 30)) {
                return name;
            } else {
                logger.error("Invalid name length!");
                logger.print("Name must be between 3 and 20 characters.", TextColor.RED);
            }
        }
    }

    private String getLastname() {
        while (true) {
            logger.print("Enter your lastname: ");
            String lastname = inputManager.getLine();

            if (StringUtils.hasCorrectLength(lastname, 3, 50)) {
                return lastname;
            } else {
                logger.error("Invalid lastname length!");
                logger.print("Lastname must be between 3 and 40 characters.", TextColor.RED);
            }
        }
    }
}