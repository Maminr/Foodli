package ir.ac.kntu.handlers;

import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.*;
import ir.ac.kntu.utilities.RandomDataGenerator;
import ir.ac.kntu.utilities.HTMLReportGenerator;
import ir.ac.kntu.utilities.DataPersistence;
import ir.ac.kntu.utilities.PaginationUtility;
import ir.ac.kntu.managers.UserManager;

import java.util.ArrayList;
import java.util.List;

/*
 * SupportMenu - Administrative interface for system support
 *
 * BONUS FEATURES TODO:
 *
 * Unit Testing:
 * TODO: Create JUnit tests for restaurant approval workflow
 * TODO: Test user management operations
 * TODO: Test system statistics calculations
 * TODO: Test input validation and error handling
 *
 * Data Persistence:
 * TODO: Implement audit trail for all support actions
 * TODO: Add database logging for approval/rejection decisions
 * TODO: Implement backup and recovery for system data
 * TODO: Add export functionality for system reports
 *
 * HTML Reports & Analytics:
 * TODO: Generate comprehensive system analytics HTML reports
 * TODO: Create interactive dashboards for platform performance
 * TODO: Add user growth charts and restaurant distribution maps
 * TODO: Implement real-time system monitoring displays
 * TODO: Add automated report generation and email delivery
 *
 * Advanced Administrative Features:
 * TODO: Implement bulk restaurant approval/rejection
 * TODO: Add user behavior analytics and fraud detection
 * TODO: Implement automated restaurant verification processes
 * TODO: Add customer support ticket system integration
 * TODO: Implement A/B testing framework for UI improvements
 *
 * CORE FEATURES IMPLEMENTED (Per Persian Spec):
 * ✅ Restaurant approval/rejection with reason tracking
 * ✅ System statistics and analytics
 * ✅ User management and oversight
 *
 * BONUS/ENHANCEMENT FEATURES (Beyond Spec):
 * TODO: Implement restaurant location verification system
 * TODO: Add automated duplicate restaurant detection
 * TODO: Implement appeal system for rejected restaurants
 * TODO: Add multi-language support for international expansion
 * TODO: Implement API rate limiting and abuse prevention
 */

public class SupportMenu extends Menu {

    private final InputManager inputManager = InputManager.getInstance();
    private final RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private final OrderManager orderManager = OrderManager.getInstance();
    private final UserManager userManager = UserManager.getInstance();
    private final PaginationUtility paginationUtility = new PaginationUtility();

    public SupportMenu() {
        super("Support Menu");
    }

    @Override
    public void enterMenu() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- SUPPORT DASHBOARD ---", TextColor.BLUE);
            logger.print("1. Restaurant Approvals", TextColor.CYAN);
            logger.print("2. System Statistics", TextColor.CYAN);
            logger.print("3. User Management", TextColor.CYAN);
            logger.print("4. Generate Test Data", TextColor.CYAN);
            logger.print("5. Generate Reports", TextColor.CYAN);
            logger.print("6. Data Persistence", TextColor.CYAN);
            logger.print("0. Logout", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleRestaurantApprovals();
                    break;
                case "2":
                    showSystemStatistics();
                    break;
                case "3":
                    handleUserManagement();
                    break;
                case "4":
                    generateTestData();
                    break;
                case "5":
                    generateSystemReport();
                    break;
                case "6":
                    handleDataPersistence();
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

    private void handleRestaurantApprovals() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- RESTAURANT APPROVALS ---", TextColor.BLUE);
            logger.print("1. View Pending Restaurants", TextColor.CYAN);
            logger.print("2. View Approved Restaurants", TextColor.CYAN);
            logger.print("3. View Rejected Restaurants", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    processPendingRestaurants();
                    break;
                case "2":
                    viewApprovedRestaurants();
                    break;
                case "3":
                    viewRejectedRestaurants();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void processPendingRestaurants() {
        Logger logger = Logger.getInstance();
        List<Restaurant> pendingRestaurants = restaurantManager.getPendingRestaurants();

        if (pendingRestaurants.isEmpty()) {
            logger.print("No pending restaurants.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        for (Restaurant restaurant : pendingRestaurants) {
            logger.print("\n--- PENDING RESTAURANT ---", TextColor.CYAN);
            logger.print("Name: " + restaurant.getName());
            logger.print("Manager: " + restaurant.getManager().getName() + " " + restaurant.getManager().getLastName());
            logger.print("Address: " + restaurant.getAddress());
            logger.print("Zone: " + restaurant.getZoneNumber());
            logger.print("Types: " + restaurant.getFoodTypes());

            logger.print("\nActions:", TextColor.GREEN);
            logger.print("1. Approve", TextColor.GREEN);
            logger.print("2. Reject", TextColor.RED);
            logger.print("3. Skip", TextColor.YELLOW);
            logger.print("Choose: ");

            String choice = inputManager.getLine();

            if (choice.equals("1")) {
                restaurantManager.approveRestaurant(restaurant);
                logger.success("Restaurant approved successfully!");
            } else if (choice.equals("2")) {
                logger.print("Rejection reason: ");
                String reason = inputManager.getLine();
                restaurantManager.rejectRestaurant(restaurant, reason);
                logger.print("Restaurant rejected.", TextColor.YELLOW);
            } else if (choice.equals("3")) {
                logger.print("Skipped.", TextColor.YELLOW);
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void viewApprovedRestaurants() {
        Logger logger = Logger.getInstance();
        List<Restaurant> approvedRestaurants = restaurantManager.getApprovedRestaurants();

        logger.print("\n--- APPROVED RESTAURANTS ---", TextColor.GREEN);

        if (approvedRestaurants.isEmpty()) {
            logger.print("No approved restaurants.", TextColor.YELLOW);
        } else {
            for (Restaurant restaurant : approvedRestaurants) {
                logger.print("• " + restaurant.getName() + " (Manager: " +
                           restaurant.getManager().getName() + ", Rating: " +
                           String.format("%.1f", restaurant.getRating()) + ")");
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void viewRejectedRestaurants() {
        Logger logger = Logger.getInstance();
        // Note: We don't have a direct method for rejected restaurants, so we'll filter all
        List<Restaurant> allRestaurants = restaurantManager.getAllRestaurants();
        List<Restaurant> rejectedRestaurants = allRestaurants.stream()
                .filter(r -> r.getStatus() == RestaurantStatus.REJECTED)
                .toList();

        logger.print("\n--- REJECTED RESTAURANTS ---", TextColor.RED);

        if (rejectedRestaurants.isEmpty()) {
            logger.print("No rejected restaurants.", TextColor.YELLOW);
        } else {
            for (Restaurant restaurant : rejectedRestaurants) {
                logger.print("• " + restaurant.getName() + " (Manager: " +
                           restaurant.getManager().getName() + ")");
                if (restaurant.getRejectionReason() != null) {
                    logger.print("  Reason: " + restaurant.getRejectionReason());
                }
            }
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void showSystemStatistics() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- SYSTEM STATISTICS ---", TextColor.BLUE);

        // Restaurant statistics
        List<Restaurant> allRestaurants = restaurantManager.getAllRestaurants();
        long pendingCount = allRestaurants.stream().filter(r -> r.getStatus() == RestaurantStatus.PENDING_REVIEW).count();
        long approvedCount = allRestaurants.stream().filter(r -> r.getStatus() == RestaurantStatus.APPROVED).count();
        long rejectedCount = allRestaurants.stream().filter(r -> r.getStatus() == RestaurantStatus.REJECTED).count();

        logger.print("Restaurants:", TextColor.CYAN);
        logger.print("• Total: " + allRestaurants.size());
        logger.print("• Pending: " + pendingCount);
        logger.print("• Approved: " + approvedCount);
        logger.print("• Rejected: " + rejectedCount);

        // Order statistics
        List<Order> allOrders = orderManager.getAllOrders();
        long totalOrders = allOrders.size();
        long activeOrders = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.DELIVERED && o.getStatus() != OrderStatus.CANCELLED)
                .count();
        long completedOrders = totalOrders - activeOrders;

        logger.print("\nOrders:", TextColor.CYAN);
        logger.print("• Total: " + totalOrders);
        logger.print("• Active: " + activeOrders);
        logger.print("• Completed: " + completedOrders);

        // Calculate total revenue (simplified)
        double totalRevenue = allOrders.stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getFinalAmount)
                .sum();

        logger.print("• Total Revenue: " + totalRevenue + " Toman", TextColor.GREEN);

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleUserManagement() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- USER MANAGEMENT ---", TextColor.BLUE);
            logger.print("1. View All Users", TextColor.CYAN);
            logger.print("2. Search Users", TextColor.CYAN);
            logger.print("3. User Statistics", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    viewAllUsers();
                    break;
                case "2":
                    searchUsers();
                    break;
                case "3":
                    showUserStatistics();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void viewAllUsers() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- ALL USERS ---", TextColor.CYAN);

        List<User> allUsers = userManager.getUsers();

        if (allUsers.isEmpty()) {
            logger.print("No users found in the system.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        // Use pagination to display users
        paginationUtility.displayPaginatedView(
            allUsers,
            (user, index) -> {
                logger.print("ID: " + user.getId());
                logger.print("   Name: " + user.getName() + " " + user.getLastName());
                logger.print("   Phone: " + user.getPhoneNumber());
                logger.print("   Role: " + user.getRole().toString());
                
                // Show role-specific information
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    logger.print("   Wallet: " + customer.getWallet() + " Toman");
                    logger.print("   Addresses: " + customer.getAddresses().size());
                } else if (user instanceof Manager) {
                    Manager manager = (Manager) user;
                    Restaurant restaurant = restaurantManager.findRestaurantByManager(manager);
                    if (restaurant != null) {
                        logger.print("   Restaurant: " + restaurant.getName());
                        logger.print("   Status: " + restaurant.getStatus().toString());
                    } else {
                        logger.print("   Restaurant: Not registered");
                    }
                }
                logger.print("");
            }
        );
    }

    private void searchUsers() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- SEARCH USERS ---", TextColor.CYAN);
        logger.print("Enter search term (name or phone, or 'back' to return): ");
        String searchTerm = inputManager.getLine().trim();

        if (searchTerm.equalsIgnoreCase("back")) {
            return;
        }

        if (searchTerm.isEmpty()) {
            logger.print("Search term cannot be empty!", TextColor.RED);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        // Search users by name or phone number
        List<User> allUsers = userManager.getUsers();
        List<User> matchingUsers = new ArrayList<>();

        String searchLower = searchTerm.toLowerCase();
        for (User user : allUsers) {
            String fullName = (user.getName() + " " + user.getLastName()).toLowerCase();
            String phone = user.getPhoneNumber().toLowerCase();

            if (fullName.contains(searchLower) || phone.contains(searchLower)) {
                matchingUsers.add(user);
            }
        }

        if (matchingUsers.isEmpty()) {
            logger.print("No users found matching: " + searchTerm, TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        logger.print("Found " + matchingUsers.size() + " user(s) matching: " + searchTerm, TextColor.GREEN);
        logger.print("");

        // Use pagination to display search results
        paginationUtility.displayPaginatedView(
            matchingUsers,
            (user, index) -> {
                logger.print("ID: " + user.getId());
                logger.print("   Name: " + user.getName() + " " + user.getLastName());
                logger.print("   Phone: " + user.getPhoneNumber());
                logger.print("   Role: " + user.getRole().toString());
                
                // Show role-specific information
                if (user instanceof Customer) {
                    Customer customer = (Customer) user;
                    logger.print("   Wallet: " + customer.getWallet() + " Toman");
                } else if (user instanceof Manager) {
                    Manager manager = (Manager) user;
                    Restaurant restaurant = restaurantManager.findRestaurantByManager(manager);
                    if (restaurant != null) {
                        logger.print("   Restaurant: " + restaurant.getName());
                        logger.print("   Status: " + restaurant.getStatus().toString());
                    }
                }
                logger.print("");
            }
        );
    }

    private void showUserStatistics() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- USER STATISTICS ---", TextColor.CYAN);

        List<User> allUsers = userManager.getUsers();
        
        // Count users by role
        long totalUsers = allUsers.size();
        long customerCount = allUsers.stream()
                .filter(u -> u instanceof Customer)
                .count();
        long managerCount = allUsers.stream()
                .filter(u -> u instanceof Manager)
                .count();
        long supportCount = allUsers.stream()
                .filter(u -> u instanceof Support)
                .count();

        // Calculate additional statistics
        double totalCustomerWallet = allUsers.stream()
                .filter(u -> u instanceof Customer)
                .mapToDouble(u -> ((Customer) u).getWallet())
                .sum();

        long managersWithRestaurants = allUsers.stream()
                .filter(u -> u instanceof Manager)
                .filter(m -> restaurantManager.findRestaurantByManager((Manager) m) != null)
                .count();

        long customersWithAddresses = allUsers.stream()
                .filter(u -> u instanceof Customer)
                .filter(c -> !((Customer) c).getAddresses().isEmpty())
                .count();

        // Display statistics
        logger.print("User Statistics:", TextColor.YELLOW);
        logger.print("");
        logger.print("Total Users: " + totalUsers, TextColor.CYAN);
        logger.print("  • Customers: " + customerCount, TextColor.GREEN);
        logger.print("  • Restaurant Managers: " + managerCount, TextColor.BLUE);
        logger.print("  • Support Staff: " + supportCount, TextColor.PURPLE);
        logger.print("");

        if (customerCount > 0) {
            logger.print("Customer Statistics:", TextColor.YELLOW);
            logger.print("  • Total Wallet Balance: " + String.format("%,.0f", totalCustomerWallet) + " Toman", TextColor.GREEN);
            logger.print("  • Customers with Addresses: " + customersWithAddresses + " / " + customerCount, TextColor.CYAN);
            logger.print("  • Average Wallet Balance: " + 
                       String.format("%,.0f", totalCustomerWallet / customerCount) + " Toman", TextColor.CYAN);
            logger.print("");
        }

        if (managerCount > 0) {
            logger.print("Manager Statistics:", TextColor.YELLOW);
            logger.print("  • Managers with Restaurants: " + managersWithRestaurants + " / " + managerCount, TextColor.BLUE);
            logger.print("  • Managers without Restaurants: " + (managerCount - managersWithRestaurants), TextColor.YELLOW);
            logger.print("");
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void generateTestData() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- GENERATE TEST DATA ---", TextColor.BLUE);
        logger.print("This will generate random test data for the system.", TextColor.CYAN);
        logger.print("Existing data will be preserved.", TextColor.YELLOW);
        logger.print("");
        logger.print("Enter number of customers to generate: ");

        try {
            int customerCount = Integer.parseInt(inputManager.getLine());
            logger.print("Enter number of restaurants to generate: ");
            int restaurantCount = Integer.parseInt(inputManager.getLine());
            logger.print("Enter number of orders to generate: ");
            int orderCount = Integer.parseInt(inputManager.getLine());

            RandomDataGenerator.generateRandomData(customerCount, restaurantCount, orderCount);

            logger.print("");
            RandomDataGenerator.printStatistics();

        } catch (NumberFormatException e) {
            logger.error("Invalid number format!");
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void generateSystemReport() {
        Logger logger = Logger.getInstance();

        logger.print("\n--- GENERATE SYSTEM REPORT ---", TextColor.BLUE);
        logger.print("Enter report filename (without extension): ");
        String filename = inputManager.getLine();

        if (filename.trim().isEmpty()) {
            logger.error("Filename cannot be empty!");
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }

        try {
            HTMLReportGenerator.generateSystemReport(filename + ".html");
            logger.success("HTML system report generated successfully!");
            logger.print("Report saved as: reports/" + filename + ".html", TextColor.GREEN);
            logger.print("Open this file in any web browser to view.", TextColor.CYAN);
        } catch (Exception e) {
            logger.error("Failed to generate report: " + e.getMessage());
        }

        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleDataPersistence() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- DATA PERSISTENCE ---", TextColor.BLUE);
            logger.print("1. Save Data to Files", TextColor.CYAN);
            logger.print("2. Load Data from Files", TextColor.CYAN);
            logger.print("3. Export Data to CSV", TextColor.CYAN);
            logger.print("4. Create Backup", TextColor.CYAN);
            logger.print("5. Restore from Backup", TextColor.CYAN);
            logger.print("0. Back", TextColor.RED);
            logger.print("Choose an option: ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    DataPersistence.saveAllData();
                    logger.print("Press Enter to continue...");
                    inputManager.getLine();
                    break;
                case "2":
                    DataPersistence.loadAllData();
                    logger.print("Press Enter to continue...");
                    inputManager.getLine();
                    break;
                case "3":
                    DataPersistence.exportToCSV();
                    logger.print("Press Enter to continue...");
                    inputManager.getLine();
                    break;
                case "4":
                    handleCreateBackup();
                    break;
                case "5":
                    handleRestoreBackup();
                    break;
                case "0":
                    return;
                default:
                    logger.print("Invalid option! Please try again.", TextColor.RED);
                    break;
            }
        }
    }

    private void handleCreateBackup() {
        Logger logger = Logger.getInstance();
        
        logger.print("\n--- CREATE BACKUP ---", TextColor.BLUE);
        logger.print("Enter backup name: ");
        String backupName = inputManager.getLine().trim();
        
        if (backupName.isEmpty()) {
            logger.print("Backup name cannot be empty!", TextColor.RED);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }
        
        DataPersistence.createBackup(backupName);
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }

    private void handleRestoreBackup() {
        Logger logger = Logger.getInstance();
        
        logger.print("\n--- RESTORE FROM BACKUP ---", TextColor.BLUE);
        
        // List available backups
        java.util.List<String> backups = DataPersistence.listBackups();
        
        if (backups.isEmpty()) {
            logger.print("No backups found.", TextColor.YELLOW);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }
        
        logger.print("Available backups:", TextColor.CYAN);
        for (int i = 0; i < backups.size(); i++) {
            logger.print((i + 1) + ". " + backups.get(i));
        }
        logger.print("");
        logger.print("Enter backup name to restore (or 'back' to cancel): ");
        String restoreName = inputManager.getLine().trim();
        
        if (restoreName.equalsIgnoreCase("back")) {
            return;
        }
        
        if (!backups.contains(restoreName)) {
            logger.print("Backup not found: " + restoreName, TextColor.RED);
            logger.print("Press Enter to continue...");
            inputManager.getLine();
            return;
        }
        
        logger.print("Are you sure you want to restore from backup '" + restoreName + "'?", TextColor.YELLOW);
        logger.print("This will replace all current data. (yes/no): ");
        String confirm = inputManager.getLine().trim();
        
        if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
            DataPersistence.restoreFromBackup(restoreName);
        } else {
            logger.print("Restore cancelled.", TextColor.YELLOW);
        }
        
        logger.print("Press Enter to continue...");
        inputManager.getLine();
    }
}
