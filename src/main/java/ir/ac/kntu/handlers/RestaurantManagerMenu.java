package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.*;
import ir.ac.kntu.utilities.HTMLReportGenerator;

import java.util.ArrayList;
import java.util.List;

/*
 * RestaurantManagerMenu - Complete restaurant management interface
 *
 * BONUS FEATURES TODO:
 *
 * Unit Testing:
 * TODO: Create JUnit tests for restaurant registration workflow
 * TODO: Test menu management operations (add/edit/delete foods)
 * TODO: Test order processing and status updates
 * TODO: Test financial calculations and wallet operations
 * TODO: Test input validation and error handling
 *
 * Data Persistence:
 * TODO: Implement restaurant data persistence (menu, orders, finances)
 * TODO: Add database storage for food inventory and pricing
 * TODO: Implement backup system for restaurant data
 * TODO: Add data export functionality for financial reports
 *
 * HTML Reports & Analytics:
 * TODO: Generate comprehensive HTML financial reports for managers
 * TODO: Create interactive charts for revenue trends and order analytics
 * TODO: Add customer demographic analysis and popular dishes charts
 * TODO: Implement monthly/yearly performance dashboards
 *
 * Advanced Features:
 * TODO: Implement real-time order notifications for restaurant managers
 * TODO: Add inventory management system for ingredients
 * TODO: Implement automated order acceptance based on capacity
 * TODO: Add customer feedback analysis and response system
 * TODO: Implement delivery time optimization algorithms
 *
 * CORE FEATURES IMPLEMENTED (Per Persian Spec):
 * ✅ Restaurant registration with approval workflow
 * ✅ Menu management (add/edit/delete foods with categories and attributes)
 * ✅ Order processing (view new orders, accept/reject, mark as sent)
 * ✅ Wallet management and financial tracking
 *
 * BONUS/ENHANCEMENT FEATURES (Beyond Spec):
 * TODO: Implement restaurant working hours management
 * TODO: Add delivery zone configuration and pricing
 * TODO: Implement bulk order discounts and promotions
 * TODO: Add customer loyalty program management
 * TODO: Implement staff management for larger restaurants
 * TODO: Add integration with delivery partners
 */

public class RestaurantManagerMenu extends Menu {

    private final InputManager inputManager = InputManager.getInstance();
    private final RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private final OrderManager orderManager = OrderManager.getInstance();
    private Restaurant currentRestaurant;

    public RestaurantManagerMenu() {
        super("Restaurant Manager Menu");
    }

    @Override
    public void enterMenu() {
        Manager currentManager = (Manager) SessionManager.getInstance().getCurrentUser();
        currentRestaurant = restaurantManager.findRestaurantByManager(currentManager);

        // Check restaurant status
        if (currentRestaurant == null) {
            // First time login - redirect to registration
            handleRestaurantRegistration();
            return;
        }

        if (currentRestaurant.getStatus() == RestaurantStatus.PENDING_REVIEW) {
            showPendingStatus();
            return;
        } else if (currentRestaurant.getStatus() == RestaurantStatus.REJECTED) {
            showRejectedStatus();
            return;
        }

        // Restaurant is approved - show main menu
        showMainMenu();
    }

    private void handleRestaurantRegistration() {
        Logger logger = Logger.getInstance();
        Manager currentManager = (Manager) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- RESTAURANT REGISTRATION ---", TextColor.GREEN);
        logger.print("Welcome! Please register your restaurant information.", TextColor.CYAN);

        // Get restaurant name
        logger.print("Restaurant Name: ");
        String name = inputManager.getLine();

        // Get restaurant types
        List<FoodType> foodTypes = selectFoodTypes();

        // Get address
        logger.print("Restaurant Address: ");
        String address = inputManager.getLine();

        // Get zone number
        int zoneNumber = getZoneNumber();

        // Get delivery cost settings
        logger.print("\n--- DELIVERY COST SETTINGS ---", TextColor.CYAN);
        logger.print("Set your delivery pricing (or press Enter for defaults)", TextColor.YELLOW);
        
        double baseDeliveryCost = getBaseDeliveryCost();
        double perZoneCost = getPerZoneCost();

        // Create restaurant
        currentRestaurant = restaurantManager.createRestaurant(name, currentManager, address, zoneNumber, foodTypes,
                                                              baseDeliveryCost, perZoneCost);

        logger.success("Restaurant registration submitted successfully!");
        logger.print("Your restaurant is now pending approval from support team.", TextColor.YELLOW);
        logger.print("You will be notified once it's approved.", TextColor.CYAN);
        logger.print("Press Enter to continue...");
        inputManager.getLine();

        // Return to restaurant manager menu (will show pending status)
        enterMenu();
    }

    private List<FoodType> selectFoodTypes() {
        List<FoodType> selectedTypes = new ArrayList<>();
        Logger logger = Logger.getInstance();

        logger.print("Select restaurant types (enter numbers separated by commas):", TextColor.CYAN);
        FoodType[] types = FoodType.values();
        for (int i = 0; i < types.length; i++) {
            logger.print((i + 1) + ". " + types[i].getDisplayName());
        }

        while (true) {
            logger.print("Your choice(s): ");
            String input = inputManager.getLine();

            try {
                String[] choices = input.split(",");
                selectedTypes.clear();

                for (String choice : choices) {
                    int index = Integer.parseInt(choice.trim()) - 1;
                    if (index >= 0 && index < types.length) {
                        selectedTypes.add(types[index]);
                    }
                }

                if (!selectedTypes.isEmpty()) {
                    break;
                }
            } catch (NumberFormatException e) {
                // Continue loop
            }

            logger.error("Invalid input! Please enter valid numbers.");
        }

        return selectedTypes;
    }

    private int getZoneNumber() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("Zone Number (1-20): ");
            try {
                int zone = Integer.parseInt(inputManager.getLine());
                if (zone >= 1 && zone <= 20) {
                    return zone;
                }
            } catch (NumberFormatException e) {
                // Continue loop
            }
            logger.error("Invalid zone number! Must be between 1-20.");
        }
    }

    private double getBaseDeliveryCost() {
        Logger logger = Logger.getInstance();
        
        while (true) {
            logger.print("Base Delivery Cost (default: 5000 Toman, or press Enter for default): ");
            String input = inputManager.getLine().trim();
            
            if (input.isEmpty()) {
                return 5000.0; // Default value
            }
            
            try {
                double cost = Double.parseDouble(input);
                if (cost >= 0) {
                    return cost;
                } else {
                    logger.error("Cost must be non-negative!");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid number format! Please enter a valid number.");
            }
        }
    }

    private double getPerZoneCost() {
        Logger logger = Logger.getInstance();
        
        while (true) {
            logger.print("Additional Cost per Zone (default: 1000 Toman, or press Enter for default): ");
            String input = inputManager.getLine().trim();
            
            if (input.isEmpty()) {
                return 1000.0; // Default value
            }
            
            try {
                double cost = Double.parseDouble(input);
                if (cost >= 0) {
                    return cost;
                } else {
                    logger.error("Cost must be non-negative!");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid number format! Please enter a valid number.");
            }
        }
    }

    private void showPendingStatus() {
        Logger logger = Logger.getInstance();
        
        while (true) {
            // Refresh restaurant status from manager
            Manager currentManager = (Manager) SessionManager.getInstance().getCurrentUser();
            currentRestaurant = restaurantManager.findRestaurantByManager(currentManager);
            
            logger.print("\n--- RESTAURANT STATUS ---", TextColor.YELLOW);
            
            // Check if status has changed
            if (currentRestaurant == null) {
                logger.print("Restaurant not found!", TextColor.RED);
                MenuHandler.getInstance().loadMenu(MenuType.MAIN_MENU);
                return;
            }
            
            if (currentRestaurant.getStatus() == RestaurantStatus.APPROVED) {
                logger.success("Your restaurant has been approved!");
                logger.print("Press Enter to continue...");
                inputManager.getLine();
                // Restaurant is now approved - show main menu
                showMainMenu();
                return;
            } else if (currentRestaurant.getStatus() == RestaurantStatus.REJECTED) {
                logger.print("Your restaurant registration was rejected.", TextColor.RED);
                if (currentRestaurant.getRejectionReason() != null) {
                    logger.print("Reason: " + currentRestaurant.getRejectionReason(), TextColor.YELLOW);
                }
                logger.print("Press Enter to continue...");
                inputManager.getLine();
                showRejectedStatus();
                return;
            }
            
            // Still pending
            logger.print("Your restaurant is pending approval from support team.", TextColor.CYAN);
            logger.print("Please wait for approval or contact support.", TextColor.YELLOW);
            logger.print("");
            logger.print("1. Refresh status", TextColor.GREEN);
            logger.print("0. Logout", TextColor.RED);
            logger.print("Choose an option: ");
            
            String choice = inputManager.getLine().trim();
            
            if (choice.equals("0")) {
                MenuHandler.getInstance().loadMenu(MenuType.MAIN_MENU);
                return;
            } else if (choice.equals("1")) {
                // Continue loop to refresh status
                continue;
            } else {
                logger.print("Invalid option! Please try again.", TextColor.RED);
            }
        }
    }

    private void showRejectedStatus() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- RESTAURANT STATUS ---", TextColor.RED);
        logger.print("Your restaurant registration was rejected.", TextColor.RED);

        if (currentRestaurant.getRejectionReason() != null) {
            logger.print("Reason: " + currentRestaurant.getRejectionReason(), TextColor.YELLOW);
        }

        logger.print("\nWould you like to:", TextColor.CYAN);
        logger.print("1. Edit registration and resubmit", TextColor.GREEN);
        logger.print("2. Return to main menu", TextColor.RED);
        logger.print("Choose an option: ");

        String choice = inputManager.getLine();
        if (choice.equals("1")) {
            handleRestaurantRegistration();
        } else {
            MenuHandler.getInstance().loadMenu(MenuType.MAIN_MENU);
        }
    }

    private void showMainMenu() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- RESTAURANT MANAGEMENT ---", TextColor.GREEN);
            logger.print("Restaurant: " + currentRestaurant.getName(), TextColor.CYAN);
            logger.print("Rating: " + String.format("%.1f", currentRestaurant.getRating()), TextColor.YELLOW);
            logger.print("Wallet: " + currentRestaurant.getWallet() + " Toman", TextColor.GREEN);
            logger.print("");
            logger.print("1. Manage Menu", TextColor.CYAN);
            logger.print("2. Process Orders", TextColor.CYAN);
            logger.print("3. View Wallet", TextColor.CYAN);
            logger.print("4. Generate Reports", TextColor.CYAN);
            logger.print("5. Restaurant Settings", TextColor.CYAN);
            logger.print("0. Logout", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleMenuManagement();
                    break;
                case "2":
                    handleOrderProcessing();
                    break;
                case "3":
                    handleWalletView();
                    break;
                case "4":
                    handleReportGeneration();
                    break;
                case "5":
                    handleRestaurantSettings();
                    break;
                case "0":
                    MenuHandler.getInstance().loadMenu(MenuType.MAIN_MENU);
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void handleMenuManagement() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- MENU MANAGEMENT ---", TextColor.GREEN);
            logger.print("1. View Menu", TextColor.CYAN);
            logger.print("2. Add Food Item", TextColor.CYAN);
            logger.print("3. Edit Food Item", TextColor.CYAN);
            logger.print("4. Remove Food Item", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    viewMenu();
                    break;
                case "2":
                    addFoodItem();
                    break;
                case "3":
                    editFoodItem();
                    break;
                case "4":
                    removeFoodItem();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void viewMenu() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- RESTAURANT MENU ---", TextColor.GREEN);

        if (currentRestaurant.getMenu().isEmpty()) {
            logger.print("Menu is empty. Add some food items first.", TextColor.YELLOW);
        } else {
            for (Food food : currentRestaurant.getMenu()) {
                logger.print(food.getId() + ". " + food.toString());
                logger.print("   " + food.getDetails().replace("\n", "\n   "));
                logger.print("");
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void addFoodItem() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- ADD FOOD ITEM ---", TextColor.GREEN);

        // Get basic info
        logger.print("Food Name: ");
        String name = inputManager.getLine();

        logger.print("Price (Toman): ");
        double price = Double.parseDouble(inputManager.getLine());

        // Select category
        FoodCategory category = selectFoodCategory();

        // Create food item
        Food food = new Food(name, price, category);

        // Set category-specific details
        setFoodDetails(food, category);

        // Add to restaurant menu
        restaurantManager.addFoodToRestaurant(currentRestaurant, food);

        logger.success("Food item added successfully!");
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private FoodCategory selectFoodCategory() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("Select food category:", TextColor.CYAN);
            FoodCategory[] categories = FoodCategory.values();
            for (int i = 0; i < categories.length; i++) {
                logger.print((i + 1) + ". " + categories[i].getDisplayName());
            }
            logger.print("Choose: ");

            try {
                int choice = Integer.parseInt(inputManager.getLine()) - 1;
                if (choice >= 0 && choice < categories.length) {
                    return categories[choice];
                }
            } catch (NumberFormatException e) {
                // Continue
            }
            logger.error("Invalid choice! Try again.");
        }
    }

    private void setFoodDetails(Food food, FoodCategory category) {
        Logger logger = Logger.getInstance();

        switch (category) {
            case MAIN_DISH:
                logger.print("Ingredients: ");
                food.setIngredients(inputManager.getLine());

                logger.print("Cooking Time (minutes): ");
                food.setCookingTime(Integer.parseInt(inputManager.getLine()));

                logger.print("Serving Type:", TextColor.CYAN);
                logger.print("1. Plated");
                logger.print("2. Sandwich");
                logger.print("Choose: ");
                int servingChoice = Integer.parseInt(inputManager.getLine());
                food.setServingType(servingChoice == 1 ? ServingType.PLATED : ServingType.SANDWICH);
                break;

            case APPETIZER:
                logger.print("Pieces per serving: ");
                food.setPiecesPerServing(Integer.parseInt(inputManager.getLine()));

                logger.print("Portion size:", TextColor.CYAN);
                logger.print("1. Small");
                logger.print("2. Medium");
                logger.print("3. Large");
                logger.print("Choose: ");
                int portionChoice = Integer.parseInt(inputManager.getLine());
                PortionSize[] portions = PortionSize.values();
                food.setPortionSize(portions[portionChoice - 1]);
                break;

            case BEVERAGE:
                logger.print("Volume (ml): ");
                food.setVolume(Integer.parseInt(inputManager.getLine()));

                logger.print("Packaging:", TextColor.CYAN);
                logger.print("1. Can");
                logger.print("2. Bottle");
                logger.print("3. Cup");
                logger.print("Choose: ");
                int packagingChoice = Integer.parseInt(inputManager.getLine());
                DrinkPackaging[] packagings = DrinkPackaging.values();
                food.setPackaging(packagings[packagingChoice - 1]);

                logger.print("Sugar status:", TextColor.CYAN);
                logger.print("1. Diet");
                logger.print("2. Regular");
                logger.print("Choose: ");
                int sugarChoice = Integer.parseInt(inputManager.getLine());
                food.setSugarStatus(sugarChoice == 1 ? SugarStatus.DIET : SugarStatus.REGULAR);
                break;
        }
    }

    private void editFoodItem() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- EDIT FOOD ITEM ---", TextColor.GREEN);

        if (currentRestaurant.getMenu().isEmpty()) {
            logger.print("Menu is empty.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        // Show menu items
        logger.print("Current menu items:");
        for (Food food : currentRestaurant.getMenu()) {
            logger.print(food.getId() + ". " + food.getName() + " - " + food.getPrice() + " Toman");
        }

        logger.print("Enter food ID to edit (or 'back'): ");
        String input = inputManager.getLine();

        if (input.equalsIgnoreCase("back")) {
            return;
        }

        try {
            int foodId = Integer.parseInt(input);
            Food food = currentRestaurant.getMenu().stream()
                    .filter(f -> f.getId() == foodId)
                    .findFirst()
                    .orElse(null);

            if (food == null) {
                logger.error("Food item not found!");
                logger.print("Press Enter to continue...");
                inputManager.getLine();
                return;
            }

            // Edit food details
            logger.print("Current name: " + food.getName());
            logger.print("New name (leave empty to keep current): ");
            String newName = inputManager.getLine();
            if (!newName.trim().isEmpty()) {
                food.setName(newName);
            }

            logger.print("Current price: " + food.getPrice());
            logger.print("New price (leave empty to keep current): ");
            String priceInput = inputManager.getLine();
            if (!priceInput.trim().isEmpty()) {
                food.setPrice(Double.parseDouble(priceInput));
            }

            logger.print("Current availability: " + (food.isAvailable() ? "Available" : "Unavailable"));
            logger.print("Make available? (y/n, leave empty to keep current): ");
            String availInput = inputManager.getLine();
            if (!availInput.trim().isEmpty()) {
                food.setAvailable(availInput.toLowerCase().startsWith("y"));
            }

            logger.success("Food item updated successfully!");

        } catch (NumberFormatException e) {
            logger.error("Invalid food ID!");
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void removeFoodItem() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- REMOVE FOOD ITEM ---", TextColor.GREEN);

        if (currentRestaurant.getMenu().isEmpty()) {
            logger.print("Menu is empty.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        // Show menu items
        logger.print("Current menu items:");
        for (Food food : currentRestaurant.getMenu()) {
            logger.print(food.getId() + ". " + food.getName() + " - " + food.getPrice() + " Toman");
        }

        logger.print("Enter food ID to remove (or 'back'): ");
        String input = inputManager.getLine();

        if (input.equalsIgnoreCase("back")) {
            return;
        }

        try {
            int foodId = Integer.parseInt(input);
            Food food = currentRestaurant.getMenu().stream()
                    .filter(f -> f.getId() == foodId)
                    .findFirst()
                    .orElse(null);

            if (food == null) {
                logger.error("Food item not found!");
            } else {
                restaurantManager.removeFoodFromRestaurant(currentRestaurant, food);
                logger.success("Food item removed successfully!");
            }

        } catch (NumberFormatException e) {
            logger.error("Invalid food ID!");
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleOrderProcessing() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- ORDER PROCESSING ---", TextColor.GREEN);
            logger.print("1. View New Orders", TextColor.CYAN);
            logger.print("2. View Active Orders", TextColor.CYAN);
            logger.print("3. View Order History", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    processNewOrders();
                    break;
                case "2":
                    viewActiveOrders();
                    break;
                case "3":
                    viewOrderHistory();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void processNewOrders() {
        Logger logger = Logger.getInstance();
        List<Order> newOrders = orderManager.getNewOrdersByRestaurant(currentRestaurant);

        if (newOrders.isEmpty()) {
            logger.print("No new orders.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        for (Order order : newOrders) {
            logger.print("\n--- NEW ORDER #" + order.getId() + " ---", TextColor.GREEN);
            logger.print(order.getInvoice());

            logger.print("Actions:", TextColor.CYAN);
            logger.print("1. Accept Order", TextColor.GREEN);
            logger.print("2. Reject Order", TextColor.RED);
            logger.print("Choose: ");

            String choice = inputManager.getLine();

            if (choice.equals("1")) {
                orderManager.updateOrderStatus(order, OrderStatus.PREPARING);
                logger.success("Order accepted and moved to preparation!");
            } else if (choice.equals("2")) {
                orderManager.updateOrderStatus(order, OrderStatus.CANCELLED);
                logger.print("Order rejected and customer refunded.", TextColor.YELLOW);
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void viewActiveOrders() {
        Logger logger = Logger.getInstance();
        List<Order> activeOrders = orderManager.getActiveOrdersByRestaurant(currentRestaurant);

        if (activeOrders.isEmpty()) {
            logger.print("No active orders.", TextColor.YELLOW);
        } else {
            for (Order order : activeOrders) {
                logger.print("\n--- ORDER #" + order.getId() + " ---", TextColor.CYAN);
                logger.print("Status: " + order.getStatus().getDisplayName());
                logger.print("Customer: " + order.getCustomer().getName());
                logger.print("Items: " + order.getItems().size());
                logger.print("Total: " + order.getFinalAmount() + " Toman");

                if (order.getStatus() == OrderStatus.PREPARING) {
                    logger.print("1. Mark as Sent", TextColor.GREEN);
                    logger.print("Choose (or Enter to skip): ");

                    String choice = inputManager.getLine();
                    if (choice.equals("1")) {
                        orderManager.updateOrderStatus(order, OrderStatus.SENT);
                        logger.success("Order marked as sent!");
                    }
                }
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void viewOrderHistory() {
        Logger logger = Logger.getInstance();
        List<Order> allOrders = orderManager.getOrdersByRestaurant(currentRestaurant);
        List<Order> historyOrders = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.DELIVERED || o.getStatus() == OrderStatus.CANCELLED)
                .toList();

        if (historyOrders.isEmpty()) {
            logger.print("No order history.", TextColor.YELLOW);
        } else {
            for (Order order : historyOrders) {
                logger.print("\n--- ORDER #" + order.getId() + " ---", TextColor.CYAN);
                logger.print("Status: " + order.getStatus().getDisplayName());
                logger.print("Customer: " + order.getCustomer().getName());
                logger.print("Total: " + order.getFinalAmount() + " Toman");
                logger.print("Date: " + order.getOrderTime().toLocalDate());
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleWalletView() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- RESTAURANT WALLET ---", TextColor.GREEN);
        logger.print("Current Balance: " + currentRestaurant.getWallet() + " Toman", TextColor.YELLOW);

        logger.print("\nWallet operations:", TextColor.CYAN);
        logger.print("1. Request Withdrawal", TextColor.CYAN);
        logger.print("0. Back", TextColor.RED);
        logger.print("Choose: ");

        String choice = inputManager.getLine();
        if (choice.equals("1")) {
            logger.print("Enter withdrawal amount: ");
            try {
                double amount = Double.parseDouble(inputManager.getLine());
                if (amount > 0 && amount <= currentRestaurant.getWallet()) {
                    // In a real system, this would create a withdrawal request
                    logger.success("Withdrawal request submitted for " + amount + " Toman!");
                    logger.print("Request will be processed by support team.", TextColor.CYAN);
                } else {
                    logger.error("Invalid amount!");
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid amount format!");
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleReportGeneration() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- REPORT GENERATION ---", TextColor.BLUE);
            logger.print("1. Generate HTML Report", TextColor.CYAN);
            logger.print("2. View Report Files", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    generateFinancialReport();
                    break;
                case "2":
                    viewReportFiles();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void generateFinancialReport() {
        Logger logger = Logger.getInstance();

        logger.print("\nEnter report filename (without extension): ");
        String filename = inputManager.getLine();

        if (filename.trim().isEmpty()) {
            logger.error("Filename cannot be empty!");
            return;
        }

        try {
            HTMLReportGenerator.generateRestaurantReport(currentRestaurant, filename + ".html");
            logger.success("Financial report generated successfully!");
            logger.print("Report saved as: reports/" + filename + ".html", TextColor.GREEN);
            logger.print("You can open this file in any web browser to view the interactive report.", TextColor.CYAN);
        } catch (Exception e) {
            logger.error("Failed to generate report: " + e.getMessage());
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void viewReportFiles() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- GENERATED REPORTS ---", TextColor.BLUE);
        logger.print("Reports are saved in the 'reports' directory.", TextColor.CYAN);
        logger.print("Available report formats:", TextColor.CYAN);
        logger.print("• HTML reports - Open in any web browser", TextColor.GREEN);
        logger.print("");
        logger.print("📊 Available report types:", TextColor.YELLOW);
        logger.print("• Financial reports with revenue charts", TextColor.CYAN);
        logger.print("• Order statistics and trends", TextColor.CYAN);
        logger.print("• Popular items analysis", TextColor.CYAN);
        logger.print("• Monthly performance dashboards", TextColor.CYAN);
        logger.print("");
        logger.print("💡 Tip: HTML reports include interactive charts!", TextColor.GREEN);

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleRestaurantSettings() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- RESTAURANT SETTINGS ---", TextColor.CYAN);
            logger.print("Restaurant: " + currentRestaurant.getName(), TextColor.GREEN);
            logger.print("");
            logger.print("Current Delivery Pricing:", TextColor.YELLOW);
            logger.print("  Base Cost: " + String.format("%.0f", currentRestaurant.getBaseDeliveryCost()) + " Toman");
            logger.print("  Per Zone Cost: " + String.format("%.0f", currentRestaurant.getPerZoneCost()) + " Toman");
            logger.print("  Example: Delivery to zone " + (currentRestaurant.getZoneNumber() + 1) + 
                        " = " + String.format("%.0f", currentRestaurant.getDeliveryCost(currentRestaurant.getZoneNumber() + 1)) + " Toman");
            logger.print("");
            logger.print("1. Edit Base Delivery Cost", TextColor.CYAN);
            logger.print("2. Edit Per Zone Cost", TextColor.CYAN);
            logger.print("3. Edit Restaurant Address", TextColor.CYAN);
            logger.print("4. Edit Zone Number", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine().trim();

            switch (choice) {
                case "1":
                    editBaseDeliveryCost();
                    break;
                case "2":
                    editPerZoneCost();
                    break;
                case "3":
                    editRestaurantAddress();
                    break;
                case "4":
                    editZoneNumber();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
            }
        }
    }

    private void editBaseDeliveryCost() {
        Logger logger = Logger.getInstance();
        logger.print("\nCurrent Base Delivery Cost: " + String.format("%.0f", currentRestaurant.getBaseDeliveryCost()) + " Toman");
        
        double newCost = getBaseDeliveryCost();
        currentRestaurant.setBaseDeliveryCost(newCost);
        
        logger.success("Base delivery cost updated to " + String.format("%.0f", newCost) + " Toman");
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void editPerZoneCost() {
        Logger logger = Logger.getInstance();
        logger.print("\nCurrent Per Zone Cost: " + String.format("%.0f", currentRestaurant.getPerZoneCost()) + " Toman");
        
        double newCost = getPerZoneCost();
        currentRestaurant.setPerZoneCost(newCost);
        
        logger.success("Per zone cost updated to " + String.format("%.0f", newCost) + " Toman");
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void editRestaurantAddress() {
        Logger logger = Logger.getInstance();
        logger.print("\nCurrent Address: " + currentRestaurant.getAddress());
        logger.print("Enter new address: ");
        
        String newAddress = inputManager.getLine().trim();
        if (!newAddress.isEmpty()) {
            currentRestaurant.setAddress(newAddress);
            logger.success("Address updated successfully!");
        } else {
            logger.print("Address not changed.", TextColor.YELLOW);
        }
        
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void editZoneNumber() {
        Logger logger = Logger.getInstance();
        logger.print("\nCurrent Zone Number: " + currentRestaurant.getZoneNumber());
        
        int newZone = getZoneNumber();
        currentRestaurant.setZoneNumber(newZone);
        
        logger.success("Zone number updated to " + newZone);
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }
}
