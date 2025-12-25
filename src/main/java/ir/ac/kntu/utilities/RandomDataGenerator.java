package ir.ac.kntu.utilities;

import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 * RandomDataGenerator - Utility class for generating test data
 *
 * BONUS FEATURES TODO:
 * TODO: Implement seeded random generation for reproducible test data
 * TODO: Add data validation to ensure generated data meets business rules
 * TODO: Implement performance optimization for large data generation
 * TODO: Add export functionality to JSON/CSV formats
 */
public class RandomDataGenerator {
    private static final Random random = new Random();
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
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String phoneNumber = "0912" + String.format("%07d", random.nextInt(10000000));

            while (userManager.findUserByPhoneNumber(phoneNumber) != null) {
                phoneNumber = "0912" + String.format("%07d", random.nextInt(10000000));
            }

            String password = "Pass" + random.nextInt(1000) + "!";
            Customer customer = (Customer) userManager.signUpCustomer(firstName, lastName, phoneNumber, password);

            if (customer != null) {
                double walletAmount = random.nextDouble() * 500000;
                customer.setWallet(walletAmount);
                int addressCount = random.nextInt(3) + 1;
                for (int j = 0; j < addressCount; j++) {
                    String addressDesc = ADDRESSES[random.nextInt(ADDRESSES.length)];
                    int zone = random.nextInt(22) + 1; // Zone 1-22
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
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            String phoneNumber = "0913" + String.format("%07d", random.nextInt(10000000));

            while (userManager.findUserByPhoneNumber(phoneNumber) != null) {
                phoneNumber = "0913" + String.format("%07d", random.nextInt(10000000));
            }

            String password = "Manager" + random.nextInt(1000) + "!";
            Manager manager = (Manager) userManager.signUpManager(firstName, lastName, phoneNumber, password);

            if (manager != null) {
                String restaurantName = RESTAURANT_NAMES[random.nextInt(RESTAURANT_NAMES.length)] + " " + (i + 1);
                String address = ADDRESSES[random.nextInt(ADDRESSES.length)];
                int zoneNumber = random.nextInt(20) + 1;

                List<FoodType> foodTypes = new ArrayList<>();
                int typeCount = random.nextInt(3) + 1;
                FoodType[] allTypes = FoodType.values();
                for (int j = 0; j < typeCount; j++) {
                    FoodType type;
                    do {
                        type = allTypes[random.nextInt(allTypes.length)];
                    } while (foodTypes.contains(type));
                    foodTypes.add(type);
                }

                Restaurant restaurant = restaurantManager.createRestaurant(restaurantName, manager, address, zoneNumber, foodTypes);

                restaurantManager.approveRestaurant(restaurant);

                double walletAmount = random.nextDouble() * 1000000;
                restaurant.setWallet(walletAmount);

                double rating = 3.0 + random.nextDouble() * 2.0;
                int ratingCount = random.nextInt(100) + 10;
                restaurant.setRating(rating);
                restaurant.setRatingCount(ratingCount);

                int menuSize = random.nextInt(6) + 3;
                for (int j = 0; j < menuSize; j++) {
                    generateRandomFood(restaurant, foodTypes.get(random.nextInt(foodTypes.size())));
                }
            }
        }
    }

    private static void generateRandomFood(Restaurant restaurant, FoodType foodType) {
        RestaurantManager restaurantManager = RestaurantManager.getInstance();

        String foodName = FOOD_NAMES[random.nextInt(FOOD_NAMES.length)];
        double price = 20000 + random.nextDouble() * 80000;

        Food food = new Food(foodName, price, getRandomFoodCategory(foodType));
        food.setAvailable(random.nextBoolean());

        switch (food.getCategory()) {
            case MAIN_DISH:
                food.setIngredients("Fresh ingredients, traditional spices");
                food.setCookingTime(15 + random.nextInt(30));
                food.setServingType(random.nextBoolean() ? ServingType.PLATED : ServingType.SANDWICH);
                break;
            case APPETIZER:
                food.setPiecesPerServing(2 + random.nextInt(6));
                food.setPortionSize(PortionSize.values()[random.nextInt(PortionSize.values().length)]);
                break;
            case BEVERAGE:
                food.setVolume(200 + random.nextInt(400));
                food.setPackaging(DrinkPackaging.values()[random.nextInt(DrinkPackaging.values().length)]);
                food.setSugarStatus(random.nextBoolean() ? SugarStatus.DIET : SugarStatus.REGULAR);
                break;
            default:
                break;
        }

        restaurantManager.addFoodToRestaurant(restaurant, food);
    }

    private static FoodCategory getRandomFoodCategory(FoodType restaurantType) {

        return switch (restaurantType) {
            case CAFE, BEVERAGE -> random.nextBoolean() ? FoodCategory.BEVERAGE : FoodCategory.APPETIZER;
            case FAST_FOOD -> random.nextInt(3) == 0 ? FoodCategory.APPETIZER : FoodCategory.MAIN_DISH;
            default -> random.nextInt(5) == 0 ? FoodCategory.BEVERAGE :
                    random.nextInt(4) == 0 ? FoodCategory.APPETIZER : FoodCategory.MAIN_DISH;
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
            Customer customer = customers.get(random.nextInt(customers.size()));
            Restaurant restaurant = restaurants.get(random.nextInt(restaurants.size()));

            if (restaurant.getMenu().isEmpty() || customer.getAddresses().isEmpty()) {
                continue;
            }

            List<OrderItem> orderItems = new ArrayList<>();
            int itemCount = random.nextInt(4) + 1;

            for (int j = 0; j < itemCount; j++) {
                List<Food> availableFoods = restaurant.getMenu().stream()
                        .filter(Food::isAvailable)
                        .toList();

                if (!availableFoods.isEmpty()) {
                    Food food = availableFoods.get(random.nextInt(availableFoods.size()));
                    int quantity = random.nextInt(3) + 1;
                    orderItems.add(new OrderItem(food, quantity));
                }
            }

            if (orderItems.isEmpty()) {
                continue;
            }

            Address deliveryAddress = customer.getAddresses().get(random.nextInt(customer.getAddresses().size()));

            double deliveryCost = restaurant.getDeliveryCost(deliveryAddress.getZoneNumber());

            Order order = orderManager.createOrder(customer, restaurant, orderItems, deliveryCost, deliveryAddress);

            int statusRandom = random.nextInt(10);
            if (statusRandom < 3) {
                orderManager.updateOrderStatus(order, OrderStatus.CANCELLED);
            } else if (statusRandom < 7) {
                orderManager.updateOrderStatus(order, OrderStatus.PREPARING);
                if (random.nextBoolean()) {
                    orderManager.updateOrderStatus(order, OrderStatus.SENT);
                    if (random.nextBoolean()) {
                        orderManager.updateOrderStatus(order, OrderStatus.DELIVERED);
                        if (random.nextBoolean()) {
                            int rating = random.nextInt(5) + 1; // 1-5 stars
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
