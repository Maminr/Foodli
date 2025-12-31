package ir.ac.kntu.utilities;

import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {
    private static final Random RANDOM = new Random();
    private static final String[] FIRST_NAMES = {"Ali", "Reza", "Mohammad", "Ahmad", "Hossein", "Mehdi", "Saeed", "Hamed", "Amir", "Kaveh"};
    private static final String[] LAST_NAMES = {"Ahmadi", "Hosseini", "Karimi", "Rahmani", "Hashemi", "Ebrahimi", "Moradi", "Mohammadi", "Rostami", "Fazeli"};
    private static final String[] RESTAURANT_NAMES = {"Delicious Pizza", "Iranian Kebab", "Fast Burger", "Seafood Palace", "Cafe Delight", "Persian Grill", "Healthy Bowl", "Sweet Sweets", "Coffee Corner", "Grill Master"};
    private static final String[] FOOD_NAMES = {"Cheese Pizza", "Chicken Kebab", "Beef Burger", "Grilled Salmon", "Cappuccino", "Chelo Kebab", "Caesar Salad", "Chocolate Cake", "Green Tea", "Fried Chicken"};
    private static final String[] ADDRESSES = {"Tehran, Azadi St", "Tehran, Vali Asr Ave", "Tehran, Enghelab St", "Tehran, Keshavarz Blvd", "Tehran, Hafez St", "Karaj, Azadegan St", "Isfahan, Chahar Bagh", "Shiraz, Zand Blvd", "Tabriz, Tabriz St", "Mashhad, Ferdowsi Sq"};

    public static void generateRandomData(int customerCount, int restaurantCount, int orderCount) {
        System.out.println("Generating random test data...");
        System.out.println("Customers: " + customerCount + ", Restaurants: " + restaurantCount + ", Orders: " + orderCount);

        generateCustomers(customerCount);
        generateRestaurants(restaurantCount);
        generateOrders(orderCount);

        System.out.println("Random data generation completed!");
    }

    private static void generateCustomers(int count) {
        UserManager userManager = UserManager.getInstance();

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
            String phoneNumber = "0912" + String.format("%07d", RANDOM.nextInt(10_000_000));

            while (userManager.findUserByPhoneNumber(phoneNumber) != null) {
                phoneNumber = "0912" + String.format("%07d", RANDOM.nextInt(10_000_000));
            }

            String password = "Pass" + RANDOM.nextInt(1000) + "!";
            Customer customer = (Customer) userManager.signUpCustomer(firstName, lastName, phoneNumber, password);

            if (customer != null) {
                double walletAmount = RANDOM.nextDouble() * 500_000;
                customer.setWallet(walletAmount);
                int addressCount = RANDOM.nextInt(3) + 1;
                for (int j = 0; j < addressCount; j++) {
                    String addressDesc = ADDRESSES[RANDOM.nextInt(ADDRESSES.length)];
                    int zone = RANDOM.nextInt(22) + 1; // Zone 1-22
                    Address address = new Address(addressDesc, zone);
                    customer.addAddress(address);
                }
            }
        }
    }

    private static void generateRestaurants(int count) {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        UserManager userManager = UserManager.getInstance();

        for (int i = 0; i < count; i++) {
            String firstName = FIRST_NAMES[RANDOM.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
            String phoneNumber = "0913" + String.format("%07d", RANDOM.nextInt(10_000_000));

            while (userManager.findUserByPhoneNumber(phoneNumber) != null) {
                phoneNumber = "0913" + String.format("%07d", RANDOM.nextInt(10_000_000));
            }

            String password = "Manager" + RANDOM.nextInt(1000) + "!";
            Manager manager = (Manager) userManager.signUpManager(firstName, lastName, phoneNumber, password);

            if (manager != null) {
                String restaurantName = RESTAURANT_NAMES[RANDOM.nextInt(RESTAURANT_NAMES.length)] + " " + (i + 1);
                String address = ADDRESSES[RANDOM.nextInt(ADDRESSES.length)];
                int zoneNumber = RANDOM.nextInt(20) + 1;

                List<FoodType> foodTypes = new ArrayList<>();
                int typeCount = RANDOM.nextInt(3) + 1;
                FoodType[] allTypes = FoodType.values();
                for (int j = 0; j < typeCount; j++) {
                    FoodType type;
                    do {
                        type = allTypes[RANDOM.nextInt(allTypes.length)];
                    } while (foodTypes.contains(type));
                    foodTypes.add(type);
                }

                Restaurant restaurant = restaurantManager.createRestaurant(restaurantName, manager, address, zoneNumber, foodTypes);

                restaurantManager.approveRestaurant(restaurant);

                double walletAmount = RANDOM.nextDouble() * 1_000_000;
                restaurant.setWallet(walletAmount);

                double rating = 3.0 + RANDOM.nextDouble() * 2.0;
                int ratingCount = RANDOM.nextInt(100) + 10;
                restaurant.setRating(rating);
                restaurant.setRatingCount(ratingCount);

                int menuSize = RANDOM.nextInt(6) + 3;
                for (int j = 0; j < menuSize; j++) {
                    generateRandomFood(restaurant, foodTypes.get(RANDOM.nextInt(foodTypes.size())));
                }
            }
        }
    }

    private static void generateRandomFood(Restaurant restaurant, FoodType foodType) {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        String foodName = FOOD_NAMES[RANDOM.nextInt(FOOD_NAMES.length)];
        double price = 20_000 + RANDOM.nextDouble() * 80_000;

        Food food = new Food(foodName, price, getRandomFoodCategory(foodType));
        food.setAvailable(RANDOM.nextBoolean());

        switch (food.getCategory()) {
            case MAIN_DISH:
                food.setIngredients("Fresh ingredients, traditional spices");
                food.setCookingTime(15 + RANDOM.nextInt(30));
                food.setServingType(RANDOM.nextBoolean() ? ServingType.PLATED : ServingType.SANDWICH);
                break;
            case APPETIZER:
                food.setPiecesPerServing(2 + RANDOM.nextInt(6));
                food.setPortionSize(PortionSize.values()[RANDOM.nextInt(PortionSize.values().length)]);
                break;
            case BEVERAGE:
                food.setVolume(200 + RANDOM.nextInt(400));
                food.setPackaging(DrinkPackaging.values()[RANDOM.nextInt(DrinkPackaging.values().length)]);
                food.setSugarStatus(RANDOM.nextBoolean() ? SugarStatus.DIET : SugarStatus.REGULAR);
                break;
            default:
                break;
        }

        restaurantManager.addFoodToRestaurant(restaurant, food);
    }

    private static FoodCategory getRandomFoodCategory(FoodType restaurantType) {

        return switch (restaurantType) {
            case CAFE, BEVERAGE -> RANDOM.nextBoolean() ? FoodCategory.BEVERAGE : FoodCategory.APPETIZER;
            case FAST_FOOD -> RANDOM.nextInt(3) == 0 ? FoodCategory.APPETIZER : FoodCategory.MAIN_DISH;
            default -> RANDOM.nextInt(5) == 0 ? FoodCategory.BEVERAGE :
                    RANDOM.nextInt(4) == 0 ? FoodCategory.APPETIZER : FoodCategory.MAIN_DISH;
        };
    }

    private static void generateOrders(int count) {
        OrderManager orderManager = OrderManager.getInstance();
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        UserManager userManager = UserManager.getInstance();

        List<Customer> customers = userManager.getUsersByRole(UserRole.CUSTOMER).stream()
                .filter(user -> user instanceof Customer)
                .map(user -> (Customer) user)
                .toList();
        List<Restaurant> restaurants = restaurantManager.getApprovedRestaurants();

        if (customers.isEmpty() || restaurants.isEmpty()) {
            System.out.println("Cannot generate orders: no customers or restaurants available");
            return;
        }

        for (int i = 0; i < count; i++) {
            Customer customer = customers.get(RANDOM.nextInt(customers.size()));
            Restaurant restaurant = restaurants.get(RANDOM.nextInt(restaurants.size()));

            if (restaurant.getMenu().isEmpty() || customer.getAddresses().isEmpty()) {
                continue;
            }

            List<OrderItem> orderItems = new ArrayList<>();
            int itemCount = RANDOM.nextInt(4) + 1;

            for (int j = 0; j < itemCount; j++) {
                List<Food> availableFoods = restaurant.getMenu().stream()
                        .filter(Food::isAvailable)
                        .toList();

                if (!availableFoods.isEmpty()) {
                    Food food = availableFoods.get(RANDOM.nextInt(availableFoods.size()));
                    int quantity = RANDOM.nextInt(3) + 1;
                    orderItems.add(new OrderItem(food, quantity));
                }
            }

            if (orderItems.isEmpty()) {
                continue;
            }

            Address deliveryAddress = customer.getAddresses().get(RANDOM.nextInt(customer.getAddresses().size()));

            double deliveryCost = restaurant.getDeliveryCost(deliveryAddress.getZoneNumber());

            Order order = orderManager.createOrder(customer, restaurant, orderItems, deliveryCost, deliveryAddress);

            int statusRandom = RANDOM.nextInt(10);
            if (statusRandom < 3) {
                orderManager.updateOrderStatus(order, OrderStatus.CANCELLED);
            } else if (statusRandom < 7) {
                orderManager.updateOrderStatus(order, OrderStatus.PREPARING);
                if (RANDOM.nextBoolean()) {
                    orderManager.updateOrderStatus(order, OrderStatus.SENT);
                    if (RANDOM.nextBoolean()) {
                        orderManager.updateOrderStatus(order, OrderStatus.DELIVERED);
                        if (RANDOM.nextBoolean()) {
                            int rating = RANDOM.nextInt(5) + 1; // 1-5 stars
                            String comment = "Good experience with the food!";
                            orderManager.addOrderReview(order, rating, comment);
                            restaurant.addRating(rating);
                        }
                    }
                }
            }
        }
    }

    public static void clearAllData() {
        System.out.println("Clearing all generated test data...");

        RestaurantManager.getInstance().getAllRestaurants().clear();
        OrderManager.getInstance().getAllOrders().clear();
        CartManager.getInstance().clearCart();

        UserManager userManager = UserManager.getInstance();
        userManager.getUsers().removeIf(user ->
                !(user instanceof Support));

        System.out.println("All test data cleared!");
    }

    public static void printStatistics() {
        UserManager userManager = UserManager.getInstance();
        RestaurantManager restaurantManager = RestaurantManager.getInstance();
        OrderManager orderManager = OrderManager.getInstance();

        System.out.println("\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Users: " + userManager.getUsers().size());
        System.out.println("Customers: " + userManager.getUsersByRole(UserRole.CUSTOMER).size());
        System.out.println("Restaurant Managers: " + userManager.getUsersByRole(UserRole.RESTAURANT_MANAGER).size());
        System.out.println("Restaurants: " + restaurantManager.getAllRestaurants().size());
        System.out.println("Approved Restaurants: " + restaurantManager.getApprovedRestaurants().size());
        System.out.println("Total Orders: " + orderManager.getAllOrders().size());

        double totalRevenue = orderManager.getAllOrders().stream()
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToDouble(Order::getFinalAmount)
                .sum();
        System.out.println("Total Revenue: " + String.format("%.0f", totalRevenue) + " Toman");
    }
}
