package ir.ac.kntu.handlers;

import ir.ac.kntu.managers.SessionManager;
import ir.ac.kntu.managers.InputManager;
import ir.ac.kntu.managers.RestaurantManager;
import ir.ac.kntu.managers.CartManager;
import ir.ac.kntu.managers.OrderManager;
import ir.ac.kntu.models.MenuItem;
import ir.ac.kntu.models.User;
import ir.ac.kntu.models.Customer;
import ir.ac.kntu.models.Restaurant;
import ir.ac.kntu.models.Food;
import ir.ac.kntu.models.ShoppingCart;
import ir.ac.kntu.models.OrderItem;
import ir.ac.kntu.models.Address;
import ir.ac.kntu.models.Order;
import ir.ac.kntu.models.enums.MenuType;
import ir.ac.kntu.models.enums.TextColor;
import ir.ac.kntu.models.enums.FoodType;
import ir.ac.kntu.helper.Logger;
import ir.ac.kntu.utilities.PaginationUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public class CustomerMenu extends Menu {

    // Common UI strings to avoid duplicate literals
    private static final String PRESS_ENTER_CONTINUE = "Press Enter to continue...";
    private static final String INVALID_OPTION = "Invalid option! Please try again.";
    private static final String CHOOSE_OPTION = "Choose an option: ";
    private static final String BACK_OPTION = "0. Back";
    private static final String TOMAN_SUFFIX = " Toman";
    private static final String CHOOSE_COMMAND = "Choose: ";
    private static final String BACK_COMMAND = "back";

    private final InputManager inputManager = InputManager.getInstance();
    private final RestaurantManager restaurantManager = RestaurantManager.getInstance();
    private final CartManager cartManager = CartManager.getInstance();
    private final OrderManager orderManager = OrderManager.getInstance();
    private final PaginationUtility paginationUtility = new PaginationUtility();

    public CustomerMenu() {
        super("Customer Menu");
        addItem(new MenuItem("1", "Restaurant Search & Selection", TextColor.GREEN, this::handleRestaurantSearch));
        addItem(new MenuItem("2", "Shopping Cart & Order", TextColor.CYAN, this::handleShoppingCart));
        addItem(new MenuItem("3", "Order Management", TextColor.PURPLE, this::handleOrderManagement));
        addItem(new MenuItem("4", "Account Settings", TextColor.YELLOW, this::handleAccountSettings));
        addItem(new MenuItem("0", "Logout", TextColor.RED, this::handleLogout));
    }


    private void handleRestaurantSearch() {
        Logger logger = Logger.getInstance();
        logger.debug("Entering Restaurant Search & Selection process.");

        while (true) {
            logger.print("\n--- RESTAURANT SEARCH & SELECTION ---", TextColor.GREEN);
            logger.print("1. Search by Name", TextColor.CYAN);
            logger.print("2. Browse by Category", TextColor.CYAN);
            logger.print("3. View All Restaurants", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleTextSearch();
                    break;
                case "2":
                    handleCategoryFilter();
                    break;
                case "3":
                    handleViewAllRestaurants();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleTextSearch() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- TEXT SEARCH ---", TextColor.GREEN);
        logger.print("Enter restaurant name or food name (or 'back' to return): ");

        String searchTerm = inputManager.getLine();
        if (searchTerm.equalsIgnoreCase("back")) {
            return;
        }

        List<Restaurant> results = restaurantManager.searchRestaurants(searchTerm);
        results = results.stream()
                .filter(r -> r.getStatus() == ir.ac.kntu.models.enums.RestaurantStatus.APPROVED)
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

        if (results.isEmpty()) {
            logger.print("No restaurants found for: " + searchTerm, TextColor.YELLOW);
        } else {
            displayRestaurantResults(results);
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleCategoryFilter() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- BROWSE BY CATEGORY ---", TextColor.GREEN);
            logger.print("1. Fast Food", TextColor.CYAN);
            logger.print("2. Iranian", TextColor.CYAN);
            logger.print("3. Seafood", TextColor.CYAN);
            logger.print("4. Beverages", TextColor.CYAN);
            logger.print("5. Other", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print("Choose a category: ");

            String choice = inputManager.getLine();

            if (choice.equals("0")) {
                return;
            }

            if (choice.matches("[1-5]")) {
                FoodType[] foodTypes = {FoodType.FAST_FOOD, FoodType.IRANIAN, FoodType.SEAFOOD,
                    FoodType.BEVERAGE, FoodType.CAFE};
                FoodType selectedType = foodTypes[Integer.parseInt(choice) - 1];

                // Filter restaurants by selected food type
                List<Restaurant> filteredRestaurants = restaurantManager.getApprovedRestaurants().stream()
                        .filter(r -> r.getFoodTypes().contains(selectedType))
                        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));

                if (filteredRestaurants.isEmpty()) {
                    logger.print("No restaurants found in " + selectedType.getDisplayName() + " category.", TextColor.YELLOW);
                } else {
                    logger.print("Found " + filteredRestaurants.size() + " restaurant(s) in " +
                            selectedType.getDisplayName() + " category:", TextColor.CYAN);
                    displayRestaurantResults(filteredRestaurants);
                }

                break;
            } else {
                logger.print("Invalid option! Please try again.", TextColor.RED);
            }
        }
    }

    private void handleViewAllRestaurants() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- ALL RESTAURANTS ---", TextColor.GREEN);

        List<Restaurant> restaurants = new ArrayList<>(restaurantManager.getApprovedRestaurants());
        if (restaurants.isEmpty()) {
            logger.print("No restaurants available.", TextColor.YELLOW);
        } else {
            displayRestaurantResults(restaurants);
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void displayRestaurantResults(List<Restaurant> restaurants) {
        Logger logger = Logger.getInstance();
        Customer currentCustomer = (Customer) SessionManager.getInstance().getCurrentUser();

        if (restaurants.isEmpty()) {
            logger.print("No restaurants found.", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        List<Restaurant> mutableRestaurants = new ArrayList<>(restaurants);

        mutableRestaurants.sort(Comparator.comparing(Restaurant::getRating).reversed());

        logger.print("Sort options:", TextColor.CYAN);
        logger.print("1. By Rating (current)", TextColor.GREEN);
        logger.print("2. By Delivery Cost", TextColor.BLUE);
        logger.print("Choose sort (or Enter for current): ");

        String sortChoice = inputManager.getLine();
        if (sortChoice.equals("2")) {
            // Sort by delivery cost (ascending)
            mutableRestaurants.sort(Comparator.comparing(r -> r.getDeliveryCost(currentCustomer.getAddresses().isEmpty() ?
                    1 : currentCustomer.getAddresses().get(0).getZoneNumber())));
            logger.print("Sorted by delivery cost (low to high)", TextColor.BLUE);
        } else {
            logger.print("Sorted by rating (high to low)", TextColor.GREEN);
        }

        logger.print("Found " + mutableRestaurants.size() + " restaurant(s)", TextColor.CYAN);
        logger.print("");

        // Use pagination to display restaurants
        int selectedIndex = paginationUtility.displayPaginatedList(
                mutableRestaurants,
                (restaurant, index) -> {
                    double deliveryCost = restaurant.getDeliveryCost(currentCustomer.getAddresses().isEmpty() ?
                            1 : currentCustomer.getAddresses().get(0).getZoneNumber());
                    logger.print(restaurant.getName());
                    logger.print("   Rating: " + String.format("%.1f", restaurant.getRating()) + " stars");
                    logger.print("   Delivery: " + deliveryCost + TOMAN_SUFFIX);
                    logger.print("   Types: " + restaurant.getFoodTypes().toString());
                    logger.print("");
                }
        );

        if (selectedIndex >= 0 && selectedIndex < mutableRestaurants.size()) {
            displayRestaurantMenu(mutableRestaurants.get(selectedIndex));
        }
    }

    private void displayRestaurantMenu(Restaurant restaurant) {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- " + restaurant.getName().toUpperCase() + " MENU ---", TextColor.GREEN);
            logger.print("Address: " + restaurant.getAddress(), TextColor.CYAN);
            logger.print("Rating: " + String.format("%.1f", restaurant.getRating()) + " stars", TextColor.CYAN);
            logger.print("");

            if (restaurant.getMenu().isEmpty()) {
                logger.print("Menu is not available yet.", TextColor.YELLOW);
                logger.print("Press Enter to go back...");
                inputManager.getLine();
                return;
            }

            logger.print("Available Items:", TextColor.CYAN);
            List<Food> availableFoods = restaurant.getMenu().stream()
                    .filter(Food::isAvailable)
                    .toList();

            if (availableFoods.isEmpty()) {
                logger.print("No items available at the moment.", TextColor.YELLOW);
                logger.print("Press Enter to go back...");
                inputManager.getLine();
                return;
            }

            for (int i = 0; i < availableFoods.size(); i++) {
                Food food = availableFoods.get(i);
                logger.print((i + 1) + ". " + food.getName() +
                        " - " + food.getPrice() + " Toman (" + food.getCategory().getDisplayName() + ")");
            }

            logger.print("");
            logger.print("Enter item number to view details (or 'back' to return): ");
            String choice = inputManager.getLine();

            if (choice.equalsIgnoreCase("back")) {
                return;
            }

            try {
                int itemNumber = Integer.parseInt(choice);
                if (itemNumber >= 1 && itemNumber <= availableFoods.size()) {
                    Food selectedFood = availableFoods.get(itemNumber - 1);
                    displayFoodDetails(selectedFood, restaurant);
                } else {
                    logger.print("Invalid item number! Please enter a number between 1 and " + availableFoods.size(), TextColor.RED);
                }
            } catch (NumberFormatException e) {
                logger.print("Invalid input!", TextColor.RED);
            }
        }
    }

    private void displayFoodDetails(Food food, Restaurant restaurant) {
        Logger logger = Logger.getInstance();

        logger.print("\n--- " + food.getName().toUpperCase() + " ---", TextColor.GREEN);
        logger.print(food.getDetails());

        logger.print("1. Add to Cart", TextColor.GREEN);
        logger.print("2. Back to Menu", TextColor.YELLOW);
        logger.print(CHOOSE_COMMAND);

        String choice = inputManager.getLine();

        if (choice.equals("1")) {
            addToCart(food, restaurant);
        }
    }

    private void addToCart(Food food, Restaurant restaurant) {
        Logger logger = Logger.getInstance();

        if (cartManager.canAddToCart(restaurant)) {
            logger.print("Cannot add items from different restaurants!", TextColor.RED);
            logger.print("Your cart contains items from another restaurant.", TextColor.YELLOW);
            logger.print("Would you like to clear cart and add this item? (y/n): ");

            String choice = inputManager.getLine();
            if (choice.toLowerCase().startsWith("y")) {
                cartManager.clearCart();
            } else {
                return;
            }
        }

        logger.print("Enter quantity: ");
        try {
            int quantity = Integer.parseInt(inputManager.getLine());
            if (quantity > 0) {
                cartManager.addToCart(restaurant, food, quantity);
                logger.success("Added " + quantity + " x " + food.getName() + " to cart!");
            } else {
                logger.error("Invalid quantity!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid quantity format!");
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleShoppingCart() {
        Logger logger = Logger.getInstance();
        logger.debug("Entering Shopping Cart & Order process.");

        while (true) {
            logger.print("\n--- SHOPPING CART & ORDER PLACEMENT ---", TextColor.CYAN);
            logger.print("1. View Cart", TextColor.CYAN);
            logger.print("2. Checkout", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleViewCart();
                    break;
                case "2":
                    handleCheckout();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleViewCart() {
        Logger logger = Logger.getInstance();
        logger.print("\n--- YOUR CART ---", TextColor.CYAN);

        ShoppingCart cart = cartManager.getCurrentCart();

        if (cart.isEmpty()) {
            logger.print("Your cart is empty.", TextColor.YELLOW);
        } else {
            logger.print(cart.toString());

            // Allow cart modifications
            logger.print("1. Change quantity", TextColor.CYAN);
            logger.print("2. Remove item", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print("Choose action (or Enter to continue): ");

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    modifyCartQuantity();
                    break;
                case "2":
                    removeCartItem();
                    break;
                case "0":
                    return;
                default:
                    break;
            }
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleCheckout() {
        Logger logger = Logger.getInstance();
        ShoppingCart cart = cartManager.getCurrentCart();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- CHECKOUT ---", TextColor.CYAN);

        // Check if cart is empty
        if (cart.isEmpty()) {
            logger.print("Your cart is empty. Add some items first!", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        // Display cart summary
        logger.print("Cart Summary:", TextColor.CYAN);
        logger.print(cart.toString());

        // Address selection
        Address selectedAddress = selectDeliveryAddress(customer);
        if (selectedAddress == null) {
            return; // User cancelled
        }

        // Calculate delivery cost
        Restaurant restaurant = cart.getRestaurant();
        double deliveryCost = restaurant.getDeliveryCost(selectedAddress.getZoneNumber());

        // Display final invoice
        double subtotal = cart.getTotal();
        double total = subtotal + deliveryCost;

        logger.print("\n--- FINAL INVOICE ---", TextColor.GREEN);
        logger.print("Subtotal: " + subtotal + " Toman");
        logger.print("Delivery Cost: " + deliveryCost + " Toman");
        logger.print("Total: " + total + " Toman", TextColor.YELLOW);
        logger.print("Delivery Address: " + selectedAddress.getDescription());
        logger.print("Your Wallet Balance: " + customer.getWallet() + " Toman");

        // Confirm payment
        logger.print("\nConfirm payment? (y/n): ");
        String confirm = inputManager.getLine();

        if (confirm.toLowerCase().startsWith("y")) {
            // Process payment
            if (customer.getWallet() >= total) {
                try {
                    Order order = cartManager.checkout(selectedAddress);
                    logger.success("Order placed successfully!");
                    logger.print("Order ID: " + order.getId());
                    logger.print("Estimated delivery time: 45-60 minutes");
                } catch (Exception e) {
                    logger.error("Checkout failed: " + e.getMessage());
                }
            } else {
                logger.error("Insufficient wallet balance!");
                logger.print("Required: " + total + " Toman, Available: " + customer.getWallet() + " Toman");
            }
        } else {
            logger.print("Checkout cancelled.", TextColor.YELLOW);
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private Address selectDeliveryAddress(Customer customer) {
        Logger logger = Logger.getInstance();

        if (customer.getAddresses().isEmpty()) {
            logger.print("You have no saved addresses. Please add an address first.", TextColor.YELLOW);
            logger.print("Redirecting to address management...");
            inputManager.getLine();
            handleAddAddress();
            return customer.getAddresses().isEmpty() ? null : customer.getAddresses().get(0);
        }

        while (true) {
            logger.print("\nSelect delivery address:", TextColor.CYAN);
            for (int i = 0; i < customer.getAddresses().size(); i++) {
                Address addr = customer.getAddresses().get(i);
                logger.print((i + 1) + ". " + addr.getDescription() + " (Zone: " + addr.getZoneNumber() + ")");
            }
            logger.print("0. Cancel");

            String choice = inputManager.getLine();
            if (choice.equals("0")) {
                return null;
            }

            try {
                int index = Integer.parseInt(choice) - 1;
                if (index >= 0 && index < customer.getAddresses().size()) {
                    return customer.getAddresses().get(index);
                }
            } catch (NumberFormatException e) {
                // Ignored
            }

            logger.error("Invalid selection! Please try again.");
        }
    }

    private void modifyCartQuantity() {
        Logger logger = Logger.getInstance();
        ShoppingCart cart = cartManager.getCurrentCart();

        if (cart.isEmpty()) {
            logger.print("Cart is empty!", TextColor.YELLOW);
            return;
        }

        OrderItem itemToModify;

        // If only one item, skip selection step. Important
        if (cart.getItems().size() == 1) {
            itemToModify = cart.getItems().get(0);
            logger.print("\nModifying: " + itemToModify.getFood().getName() + " (current quantity: " + itemToModify.getQuantity() + ")");
        } else {
            // Multiple items - ask user to select
            logger.print("\nSelect item to modify:", TextColor.CYAN);
            for (int i = 0; i < cart.getItems().size(); i++) {
                OrderItem item = cart.getItems().get(i);
                logger.print((i + 1) + ". " + item.getFood().getName() + " (current quantity: " + item.getQuantity() + ")");
            }
            logger.print("0. Cancel", TextColor.RED);
            logger.print("Enter item number (1-" + cart.getItems().size() + "): ");

            try {
                String input = inputManager.getLine().trim();

                if (input.equals("0")) {
                    return;
                }

                int itemIndex = Integer.parseInt(input) - 1;
                if (itemIndex >= 0 && itemIndex < cart.getItems().size()) {
                    itemToModify = cart.getItems().get(itemIndex);
                } else {
                    logger.error("Invalid item number! Please enter a number between 1 and " + cart.getItems().size() + ".");
                    return;
                }
            } catch (NumberFormatException e) {
                logger.error("Invalid input! Please enter a valid number.");
                return;
            }
        }

        logger.print("\nEnter new quantity for " + itemToModify.getFood().getName() + " (0 to remove, or 'back' to cancel): ");

        try {
            String quantityInput = inputManager.getLine().trim();

            if (quantityInput.equalsIgnoreCase("back")) {
                return;
            }

            int newQuantity = Integer.parseInt(quantityInput);

            if (newQuantity < 0) {
                logger.error("Quantity cannot be negative!");
                return;
            }

            cartManager.changeQuantity(itemToModify, newQuantity);

            if (newQuantity == 0) {
                logger.success("Item removed from cart!");
            } else {
                logger.success("Quantity updated to " + newQuantity + "!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid input! Please enter a valid number.");
        }
    }

    private void removeCartItem() {
        Logger logger = Logger.getInstance();
        ShoppingCart cart = cartManager.getCurrentCart();

        if (cart.isEmpty()) {
            logger.print("Cart is empty!", TextColor.YELLOW);
            return;
        }

        logger.print("\nSelect item to remove:", TextColor.CYAN);
        for (int i = 0; i < cart.getItems().size(); i++) {
            OrderItem item = cart.getItems().get(i);
            logger.print((i + 1) + ". " + item.getFood().getName() + " (quantity: " + item.getQuantity() + ")");
        }
        logger.print("0. Cancel", TextColor.RED);
        logger.print("Enter item number (1-" + cart.getItems().size() + "): ");

        try {
            String input = inputManager.getLine().trim();

            if (input.equals("0")) {
                return;
            }

            int itemIndex = Integer.parseInt(input) - 1;
            if (itemIndex >= 0 && itemIndex < cart.getItems().size()) {
                OrderItem item = cart.getItems().get(itemIndex);
                cartManager.removeFromCart(item);
                logger.success("Item removed from cart!");
            } else {
                logger.error("Invalid item number! Please enter a number between 1 and " + cart.getItems().size() + ".");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid input! Please enter a valid number.");
        }
    }

    private void handleAccountSettings() {
        Logger logger = Logger.getInstance();
        logger.debug("Entering Account Settings process.");

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            logger.error("No user logged in! (Session Error)");
            return;
        }

        while (true) {
            logger.print("\n--- ACCOUNT SETTINGS ---", TextColor.YELLOW);
            logger.print("1. Address Management", TextColor.CYAN);
            logger.print("2. Wallet Management", TextColor.CYAN);
            logger.print("3. Profile Information", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleAddressManagement();
                    break;
                case "2":
                    handleWalletManagement();
                    break;
                case "3":
                    displayProfileInfo();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleAddressManagement() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- ADDRESS MANAGEMENT ---", TextColor.YELLOW);
            logger.print("1. View Saved Addresses", TextColor.CYAN);
            logger.print("2. Add New Address", TextColor.CYAN);
            logger.print("3. Edit Address", TextColor.CYAN);
            logger.print("4. Delete Address", TextColor.CYAN);
            logger.print("0. Back to Account Settings", TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleViewAddresses();
                    break;
                case "2":
                    handleAddAddress();
                    break;
                case "3":
                    handleEditAddress();
                    break;
                case "4":
                    handleDeleteAddress();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleViewAddresses() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- SAVED ADDRESSES ---", TextColor.YELLOW);

        if (customer.getAddresses().isEmpty()) {
            logger.print("No saved addresses.", TextColor.YELLOW);
        } else {
            for (int i = 0; i < customer.getAddresses().size(); i++) {
                Address addr = customer.getAddresses().get(i);
                logger.print((i + 1) + ". " + addr.getDescription());
                logger.print("   Zone: " + addr.getZoneNumber());
            }
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleAddAddress() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- ADD NEW ADDRESS ---", TextColor.YELLOW);

        logger.print("Address description (e.g., 'Tehran, Mirzaye Shirazi St, 22nd Alley'): ");
        String description = inputManager.getLine();

        if (description.equalsIgnoreCase("back")) {
            return;
        }

        if (description.trim().isEmpty()) {
            logger.error("Address description cannot be empty!");
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        logger.print("Zone number (1-22): ");
        String zoneInput = inputManager.getLine();

        if (zoneInput.equalsIgnoreCase("back")) {
            return;
        }

        try {
            int zoneNumber = Integer.parseInt(zoneInput);
            if (zoneNumber >= 1 && zoneNumber <= 22) {
                Address newAddress = new Address(description.trim(), zoneNumber);
                customer.addAddress(newAddress);

                logger.success("Address added successfully!");
            } else {
                logger.error("Zone number must be between 1 and 22!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid zone number format!");
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleEditAddress() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- EDIT ADDRESS ---", TextColor.YELLOW);

        if (customer.getAddresses().isEmpty()) {
            logger.print("No addresses to edit.", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        logger.print("Select address to edit:");
        for (int i = 0; i < customer.getAddresses().size(); i++) {
            Address addr = customer.getAddresses().get(i);
            logger.print((i + 1) + ". " + addr.getDescription() + " (Zone: " + addr.getZoneNumber() + ")");
        }
        logger.print("0. Cancel");

        String choice = inputManager.getLine();
        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < customer.getAddresses().size()) {
                Address addressToEdit = customer.getAddresses().get(index);

                logger.print("Current description: " + addressToEdit.getDescription());
                logger.print("New description (leave empty to keep current): ");
                String newDescription = inputManager.getLine();

                if (!newDescription.trim().isEmpty()) {
                    addressToEdit.setDescription(newDescription.trim());
                }

                logger.print("Current zone: " + addressToEdit.getZoneNumber());
                logger.print("New zone (1-22, leave empty to keep current): ");
                String zoneInput = inputManager.getLine();

                if (!zoneInput.trim().isEmpty()) {
                    try {
                        int newZone = Integer.parseInt(zoneInput);
                        if (newZone >= 1 && newZone <= 22) {
                            addressToEdit.setZoneNumber(newZone);
                        } else {
                            logger.error("Zone number must be between 1 and 22!");
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Invalid zone number format!");
                    }
                }

                logger.success("Address updated successfully!");
            } else {
                logger.error("Invalid address selection!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid input!");
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleDeleteAddress() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- DELETE ADDRESS ---", TextColor.YELLOW);

        if (customer.getAddresses().isEmpty()) {
            logger.print("No addresses to delete.", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }


        logger.print("Select address to delete:");
        for (int i = 0; i < customer.getAddresses().size(); i++) {
            Address addr = customer.getAddresses().get(i);
            logger.print((i + 1) + ". " + addr.getDescription() + " (Zone: " + addr.getZoneNumber() + ")");
        }
        logger.print("0. Cancel");

        String choice = inputManager.getLine();
        if (choice.equals("0")) {
            return;
        }

        try {
            int index = Integer.parseInt(choice) - 1;
            if (index >= 0 && index < customer.getAddresses().size()) {
                Address addressToDelete = customer.getAddresses().get(index);

                logger.print("Are you sure you want to delete this address?", TextColor.RED);
                logger.print(addressToDelete.getDescription());
                logger.print("(y/n): ");

                String confirm = inputManager.getLine();
                if (confirm.toLowerCase().startsWith("y")) {
                    customer.removeAddress(addressToDelete);
                    logger.success("Address deleted successfully!");
                } else {
                    logger.print("Deletion cancelled.", TextColor.YELLOW);
                }
            } else {
                logger.error("Invalid address selection!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid input!");
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleWalletManagement() {
        Logger logger = Logger.getInstance();

        while (true) {
            logger.print("\n--- WALLET MANAGEMENT ---", TextColor.YELLOW);
            logger.print("1. View Balance", TextColor.CYAN);
            logger.print("2. Charge Wallet", TextColor.CYAN);
            logger.print("0. Back to Account Settings", TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleViewBalance();
                    break;
                case "2":
                    handleChargeWallet();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleViewBalance() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- WALLET BALANCE ---", TextColor.YELLOW);
        logger.print("Current Balance: " + customer.getWallet() + " Toman", TextColor.GREEN);

        // FEATURE: Transaction history - Not implemented yet
        // TODO: Implement wallet transaction history tracking
        logger.print("Transaction history feature coming soon...", TextColor.CYAN);

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleChargeWallet() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- CHARGE WALLET ---", TextColor.YELLOW);

        logger.print("Enter amount to charge (in Toman, or 'back' to cancel): ");
        String amountInput = inputManager.getLine();

        if (amountInput.equalsIgnoreCase("back")) {
            return;
        }

        try {
            double amount = Double.parseDouble(amountInput);
            if (amount > 0) {
                // simulate successful payment

                customer.addToWallet(amount);
                logger.success("Wallet topped up successfully!");
                logger.print("Added: " + amount + " Toman");
                logger.print("New Balance: " + customer.getWallet() + " Toman", TextColor.GREEN);
            } else {
                logger.error("Amount must be greater than 0!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid amount format!");
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void displayProfileInfo() {
        Logger logger = Logger.getInstance();
        User currentUser = SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- PROFILE INFORMATION ---", TextColor.YELLOW);
        logger.print("=================================", TextColor.YELLOW);
        logger.print("Name: " + currentUser.getName() + " " + currentUser.getLastName());
        logger.print("Phone: " + currentUser.getPhoneNumber());
        logger.print("Role: " + currentUser.getRole());
        logger.print("=================================", TextColor.YELLOW);

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleOrderManagement() {
        Logger logger = Logger.getInstance();
        logger.debug("Entering Order Management process.");

        while (true) {
            logger.print("\n--- ORDER MANAGEMENT ---", TextColor.PURPLE);
            logger.print("1. Active Orders", TextColor.CYAN);
            logger.print("2. Order History", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_OPTION);

            String choice = inputManager.getLine();

            switch (choice) {
                case "1":
                    handleActiveOrders();
                    break;
                case "2":
                    handleOrderHistory();
                    break;
                case "0":
                    return;
                default:
                    logger.print(INVALID_OPTION, TextColor.RED);
                    break;
            }
        }
    }

    private void handleActiveOrders() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- ACTIVE ORDERS ---", TextColor.PURPLE);

        List<Order> activeOrders = orderManager.getOrdersByCustomer(customer).stream()
                .filter(o -> o.getStatus() != ir.ac.kntu.models.enums.OrderStatus.DELIVERED &&
                        o.getStatus() != ir.ac.kntu.models.enums.OrderStatus.CANCELLED)
                .toList();

        if (activeOrders.isEmpty()) {
            logger.print("No active orders at the moment.", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        // Use pagination to display orders and allow selection
        int selectedIndex = paginationUtility.displayPaginatedList(
                activeOrders,
                (order, index) -> {
                    logger.print("Order #" + order.getId() + " - " + order.getRestaurant().getName());
                    logger.print("   Status: " + order.getStatus().getDisplayName());
                    logger.print("   Total: " + order.getFinalAmount() + " Toman");
                    logger.print("   Time: " + order.getOrderTime().toLocalDate() + " " +
                            order.getOrderTime().toLocalTime().toString().substring(0, 5));
                    logger.print("");
                }
        );

        if (selectedIndex >= 0 && selectedIndex < activeOrders.size()) {
            interactWithActiveOrder(activeOrders.get(selectedIndex));
        }
    }

    private void interactWithActiveOrder(Order order) {
        Logger logger = Logger.getInstance();

        logger.print("\n--- ORDER #" + order.getId() + " ---", TextColor.CYAN);
        logger.print("Restaurant: " + order.getRestaurant().getName());
        logger.print("Status: " + order.getStatus().getDisplayName());
        logger.print("Total: " + order.getFinalAmount() + " Toman");
        logger.print("");

        // Allow status change from 'sent' to 'delivered'
        if (order.getStatus() == ir.ac.kntu.models.enums.OrderStatus.SENT) {
            logger.print("1. Mark as Delivered", TextColor.GREEN);
            logger.print("2. View Invoice", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_COMMAND);

            String choice = inputManager.getLine();
            if (choice.equals("1")) {
                orderManager.updateOrderStatus(order, ir.ac.kntu.models.enums.OrderStatus.DELIVERED);
                logger.success("Order marked as delivered!");
            } else if (choice.equals("2")) {
                logger.print("\n" + order.getInvoice());
            }
        } else {
            logger.print("1. View Invoice", TextColor.CYAN);
            logger.print(BACK_OPTION, TextColor.RED);
            logger.print(CHOOSE_COMMAND);

            String choice = inputManager.getLine();
            if (choice.equals("1")) {
                logger.print("\n" + order.getInvoice());
            }
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleOrderHistory() {
        Logger logger = Logger.getInstance();
        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();

        logger.print("\n--- ORDER HISTORY ---", TextColor.PURPLE);

        List<Order> orderHistory = orderManager.getOrdersByCustomer(customer).stream()
                .filter(o -> o.getStatus() == ir.ac.kntu.models.enums.OrderStatus.DELIVERED ||
                        o.getStatus() == ir.ac.kntu.models.enums.OrderStatus.CANCELLED)
                .toList();

        if (orderHistory.isEmpty()) {
            logger.print("No previous orders.", TextColor.YELLOW);
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
            return;
        }

        // Use pagination to display order history and allow selection
        int selectedIndex = paginationUtility.displayPaginatedList(
                orderHistory,
                (order, index) -> {
                    logger.print("Order #" + order.getId() + " - " + order.getRestaurant().getName());
                    logger.print("   Status: " + order.getStatus().getDisplayName());
                    logger.print("   Total: " + order.getFinalAmount() + " Toman");
                    logger.print("   Date: " + order.getOrderTime().toLocalDate());

                    // Show review status for delivered orders
                    if (order.getStatus() == ir.ac.kntu.models.enums.OrderStatus.DELIVERED
                            && order.getReviewRating() > 0) {
                        logger.print("   Rating: " + order.getReviewRating() + "/5 stars");
                        if (order.getReviewComment() != null) {
                            logger.print("   Comment: " + order.getReviewComment());
                        }
                    }
                    logger.print("");
                }
        );

        // If user selected an order, interact with it
        if (selectedIndex >= 0 && selectedIndex < orderHistory.size()) {
            interactWithOrderHistory(orderHistory.get(selectedIndex));
        }
    }

    private void interactWithOrderHistory(Order order) {
        Logger logger = Logger.getInstance();

        logger.print("\n--- ORDER #" + order.getId() + " ---", TextColor.CYAN);
        logger.print("Restaurant: " + order.getRestaurant().getName());
        logger.print("Status: " + order.getStatus().getDisplayName());
        logger.print("Total: " + order.getFinalAmount() + " Toman");
        logger.print("Date: " + order.getOrderTime().toLocalDate());
        logger.print("");
        logger.print("1. View Invoice", TextColor.CYAN);

        // Review functionality for delivered orders
        if (order.getStatus() == ir.ac.kntu.models.enums.OrderStatus.DELIVERED) {
            if (order.getReviewRating() == 0) {
                logger.print("2. Leave Review", TextColor.GREEN);
            } else {
                logger.print("Rating: " + order.getReviewRating() + "/5 stars");
                if (order.getReviewComment() != null) {
                    logger.print("Comment: " + order.getReviewComment());
                }
            }
            logger.print("3. Reorder", TextColor.BLUE);
        }


        logger.print("0. Back", TextColor.RED);
        logger.print(CHOOSE_COMMAND);

        String choice = inputManager.getLine();
        if (choice.equals("2") && order.getStatus() == ir.ac.kntu.models.enums.OrderStatus.DELIVERED &&
                order.getReviewRating() == 0) {
            leaveReview(order);
        } else if (choice.equals("1")) {
            logger.print("\n" + order.getInvoice());
            logger.print(PRESS_ENTER_CONTINUE);
            inputManager.getLine();
        } else if (choice.equals("3") && order.getStatus() == ir.ac.kntu.models.enums.OrderStatus.DELIVERED) {
            reorderFromHistory(order);
        }
    }

    private void leaveReview(Order order) {
        Logger logger = Logger.getInstance();

        logger.print("Rate your experience (1-5 stars): ");
        try {
            int rating = Integer.parseInt(inputManager.getLine());
            if (rating >= 1 && rating <= 5) {
                logger.print("Leave a comment (optional, or 'skip'): ");
                String comment = inputManager.getLine();

                if (comment.equalsIgnoreCase("skip")) {
                    comment = null;
                }

                orderManager.addOrderReview(order, rating, comment);
                logger.success("Review submitted successfully!");
            } else {
                logger.error("Rating must be between 1 and 5!");
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid rating format!");
        }
    }

    private void reorderFromHistory(Order previousOrder) {
        Logger logger = Logger.getInstance();

        logger.print("\n--- REORDER FROM HISTORY ---", TextColor.BLUE);
        logger.print("Reordering from: " + previousOrder.getRestaurant().getName());
        logger.print("Original order items:");
        for (OrderItem item : previousOrder.getItems()) {
            logger.print("- " + item.getFood().getName() + " x" + item.getQuantity());
        }

        logger.print("\nThis will add the same items to your cart.");
        logger.print("Continue? (y/n): ");

        String confirm = inputManager.getLine();
        if (confirm.toLowerCase().startsWith("y")) {
            // Check if we can add to cart (same restaurant or empty cart). Important
            if (cartManager.canAddToCart(previousOrder.getRestaurant())) {
                logger.print("Cannot reorder: different restaurant in cart.");
                logger.print("Clear cart first? (y/n): ");
                String clearCart = inputManager.getLine();
                if (clearCart.toLowerCase().startsWith("y")) {
                    cartManager.clearCart();
                } else {
                    return;
                }
            }

            for (OrderItem item : previousOrder.getItems()) {
                try {
                    cartManager.addToCart(previousOrder.getRestaurant(), item.getFood(), item.getQuantity());
                } catch (Exception e) {
                    logger.error("Could not add item: " + e.getMessage());
                }
            }

            logger.success("Items added to cart successfully!");
            logger.print("Proceed to checkout? (y/n): ");

            String checkout = inputManager.getLine();
            if (checkout.toLowerCase().startsWith("y")) {
                handleShoppingCart();
            }
        } else {
            logger.print("Reorder cancelled.", TextColor.YELLOW);
        }

        logger.print(PRESS_ENTER_CONTINUE);
        inputManager.getLine();
    }

    private void handleLogout() {
        Logger logger = Logger.getInstance();
        logger.debug("Entering Logout process.");

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            logger.error("No user logged in! (Session Error)");
            return;
        }

        SessionManager.getInstance().logout();

        logger.success("Successfully logged out, " + currentUser.getName() + "!");
        logger.print("Returning to Main Menu...", TextColor.CYAN);

        MenuHandler.getInstance().loadMenu(MenuType.MAIN_MENU);
    }
}