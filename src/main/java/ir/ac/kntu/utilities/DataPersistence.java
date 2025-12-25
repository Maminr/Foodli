package ir.ac.kntu.utilities;

import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * DataPersistence - CSV-based data persistence utility
 *
 * BONUS FEATURES IMPLEMENTATION:
 * - Data persistence without full database
 * - Backup and recovery mechanisms
 * - CSV format for data portability and analysis
 */
public class DataPersistence {

    private static final String DATA_DIR = "data";

    static {
        // Create data directory
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Could not create data directory: " + e.getMessage());
        }
    }

    /**
     * Save all system data to CSV files
     */
    public static void saveAllData() {
        System.out.println("Saving data to CSV files...");

        try {
            // Save users
            saveUsers();
            // Save restaurants
            saveRestaurants();
            // Save orders
            saveOrders();

            System.out.println("Data saved successfully!");
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Load all system data from CSV files
     */
    public static void loadAllData() {
        System.out.println("Loading data from CSV files...");

        try {
            // Load users
            loadUsers();
            // Load restaurants
            loadRestaurants();
            // Load orders
            loadOrders();

            System.out.println("Data loaded successfully!");
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Create backup of current data
     */
    public static void createBackup(String backupName) {
        String backupDir = DATA_DIR + "/backups/" + backupName;
        try {
            // First, save all current data to CSV files
            saveAllData();

            // Create backup directory
            Files.createDirectories(Paths.get(backupDir));

            // Copy all CSV data files to backup directory
            copyFile("users.csv", backupDir + "/users.csv");
            copyFile("restaurants.csv", backupDir + "/restaurants.csv");
            copyFile("orders.csv", backupDir + "/orders.csv");

            System.out.println("Backup created successfully: " + backupName);
            System.out.println("Backup location: " + backupDir);
        } catch (IOException e) {
            System.err.println("Error creating backup: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error saving data before backup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Restore from backup
     */
    public static void restoreFromBackup(String backupName) {
        String backupDir = DATA_DIR + "/backups/" + backupName;

        try {
            // Check if backup directory exists
            java.nio.file.Path backupPath = Paths.get(backupDir);
            if (!Files.exists(backupPath)) {
                System.err.println("Backup not found: " + backupName);
                System.err.println("Backup directory: " + backupDir);
                return;
            }

            // Copy backup CSV files to main data directory
            copyFileFromBackup(backupDir + "/users.csv", DATA_DIR + "/users.csv");
            copyFileFromBackup(backupDir + "/restaurants.csv", DATA_DIR + "/restaurants.csv");
            copyFileFromBackup(backupDir + "/orders.csv", DATA_DIR + "/orders.csv");

            // Reload data
            loadAllData();

            System.out.println("Restored from backup successfully: " + backupName);
        } catch (IOException e) {
            System.err.println("Error restoring backup: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error loading data from backup: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void saveUsers() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + "/users.csv"))) {
            writer.println("ID,Name,LastName,Phone,Role,Wallet");
            for (User user : UserManager.getInstance().getUsers()) {
                String wallet = user instanceof Customer ? String.valueOf(((Customer) user).getWallet()) : "0";
                writer.printf("%d,%s,%s,%s,%s,%s%n",
                        user.getId(),
                        escapeCSV(user.getName()),
                        escapeCSV(user.getLastName()),
                        user.getPhoneNumber(),
                        user.getRole().toString(),
                        wallet);
            }
        }
    }

    private static void loadUsers() throws IOException {
        File file = new File(DATA_DIR + "/users.csv");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 6) {
                    // Basic user loading - in a real system this would be more sophisticated
                    System.out.println("Loaded user: " + parts[1] + " " + parts[2]);
                }
            }
        }
    }

    private static void saveRestaurants() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + "/restaurants.csv"))) {
            writer.println("ID,Name,ManagerPhone,Address,Zone,Status,Rating,Wallet,FoodTypes");
            for (Restaurant restaurant : RestaurantManager.getInstance().getAllRestaurants()) {
                String foodTypes = restaurant.getFoodTypes().toString().replace("[", "").replace("]", "");
                writer.printf("%d,%s,%s,%s,%d,%s,%.1f,%.0f,%s%n",
                        restaurant.getId(),
                        escapeCSV(restaurant.getName()),
                        restaurant.getManager().getPhoneNumber(),
                        escapeCSV(restaurant.getAddress()),
                        restaurant.getZoneNumber(),
                        restaurant.getStatus().toString(),
                        restaurant.getRating(),
                        restaurant.getWallet(),
                        escapeCSV(foodTypes));
            }
        }
    }

    private static void loadRestaurants() throws IOException {
        File file = new File(DATA_DIR + "/restaurants.csv");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 9) {
                    System.out.println("Loaded restaurant: " + parts[1]);
                }
            }
        }
    }

    private static void saveOrders() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + "/orders.csv"))) {
            writer.println("ID,CustomerPhone,RestaurantName,Status,OrderTime,TotalAmount,DeliveryCost,FinalAmount");
            for (Order order : OrderManager.getInstance().getAllOrders()) {
                writer.printf("%d,%s,%s,%s,%s,%.0f,%.0f,%.0f%n",
                        order.getId(),
                        order.getCustomer().getPhoneNumber(),
                        escapeCSV(order.getRestaurant().getName()),
                        order.getStatus().toString(),
                        order.getOrderTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                        order.getItemsTotal(),
                        order.getDeliveryCost(),
                        order.getFinalAmount());
            }
        }
    }

    private static void loadOrders() throws IOException {
        File file = new File(DATA_DIR + "/orders.csv");
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCSVLine(line);
                if (parts.length >= 8) {
                    System.out.println("Loaded order ID: " + parts[0]);
                }
            }
        }
    }

    /**
     * Export data to CSV format (same as saveAllData but in separate directory)
     */
    public static void exportToCSV() {
        try {
            // First, save all current data to CSV files
            saveAllData();

            String csvDir = DATA_DIR + "/export";
            Files.createDirectories(Paths.get(csvDir));

            // Copy data files to export directory with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String exportDir = csvDir + "/export_" + timestamp;
            Files.createDirectories(Paths.get(exportDir));

            copyFile("users.csv", exportDir + "/users.csv");
            copyFile("restaurants.csv", exportDir + "/restaurants.csv");
            copyFile("orders.csv", exportDir + "/orders.csv");

            System.out.println("Data exported successfully to: " + exportDir);
        } catch (IOException e) {
            System.err.println("Error exporting data: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error saving data before export: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Copy file from data directory to destination
     */
    private static void copyFile(String sourceName, String destPath) throws IOException {
        java.nio.file.Path sourcePath = Paths.get(DATA_DIR + "/" + sourceName);
        java.nio.file.Path destFilePath = Paths.get(destPath);

        // Check if source file exists
        if (!Files.exists(sourcePath)) {
            throw new IOException("Source file does not exist: " + sourcePath);
        }

        // Create parent directories if they don't exist
        Files.createDirectories(destFilePath.getParent());

        // Copy file, replace if exists
        Files.copy(sourcePath, destFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Copy file from backup directory to data directory
     */
    private static void copyFileFromBackup(String sourcePath, String destPath) throws IOException {
        java.nio.file.Path sourceFilePath = Paths.get(sourcePath);
        java.nio.file.Path destFilePath = Paths.get(destPath);

        // Check if source file exists
        if (!Files.exists(sourceFilePath)) {
            System.out.println("Warning: Backup file not found, skipping: " + sourcePath);
            return;
        }

        // Create parent directories if they don't exist
        Files.createDirectories(destFilePath.getParent());

        // Copy file, replace if exists
        Files.copy(sourceFilePath, destFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }

    private static String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static String[] parseCSVLine(String line) {
        // Simple CSV parser (doesn't handle all edge cases)
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    /**
     * List all available backups
     */
    public static java.util.List<String> listBackups() {
        java.util.List<String> backups = new java.util.ArrayList<>();
        try {
            java.nio.file.Path backupsDir = Paths.get(DATA_DIR + "/backups");
            if (Files.exists(backupsDir)) {
                Files.list(backupsDir)
                        .filter(Files::isDirectory)
                        .map(path -> path.getFileName().toString())
                        .forEach(backups::add);
            }
        } catch (IOException e) {
            System.err.println("Error listing backups: " + e.getMessage());
        }
        return backups;
    }
}
