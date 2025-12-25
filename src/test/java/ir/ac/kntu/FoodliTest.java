package ir.ac.kntu;

import ir.ac.kntu.managers.*;
import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.*;
import ir.ac.kntu.utilities.TextSimilarity;
import ir.ac.kntu.utilities.RandomDataGenerator;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * FoodliTest - Unit tests for core Foodli functionality
 *
 * BONUS FEATURES IMPLEMENTATION:
 * - JUnit test suite for business logic
 * - Test coverage for managers and utilities
 * - Automated testing framework
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FoodliTest {

    private RestaurantManager restaurantManager;
    private OrderManager orderManager;
    private CartManager cartManager;

    @BeforeAll
    void setUp() {
        // Clear any existing data
        RandomDataGenerator.clearAllData();

        restaurantManager = RestaurantManager.getInstance();
        orderManager = OrderManager.getInstance();
        cartManager = CartManager.getInstance();
    }

    @AfterAll
    void tearDown() {
        RandomDataGenerator.clearAllData();
    }

    @Test
    @DisplayName("Restaurant Creation and Management")
    void testRestaurantCreation() {
        // Create a manager
        Manager manager = (Manager) UserManager.getInstance().signUpManager("Test", "Manager", "09120000000", "test123");

        // Create restaurant
        List<FoodType> foodTypes = Arrays.asList(FoodType.IRANIAN, FoodType.FAST_FOOD);
        Restaurant restaurant = restaurantManager.createRestaurant("Test Restaurant", manager, "Test Address", 5, foodTypes);

        assertNotNull(restaurant);
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals(manager, restaurant.getManager());
        assertEquals(5, restaurant.getZoneNumber());
        assertEquals(foodTypes, restaurant.getFoodTypes());
        assertEquals(RestaurantStatus.PENDING_REVIEW, restaurant.getStatus());
    }

    @Test
    @DisplayName("Restaurant Approval Workflow")
    void testRestaurantApproval() {
        // Create and approve restaurant
        Manager manager = (Manager) UserManager.getInstance().signUpManager("Test2", "Manager", "09120000001", "test123");
        Restaurant restaurant = restaurantManager.createRestaurant("Test Restaurant 2", manager, "Address", 3, Arrays.asList(FoodType.IRANIAN));

        restaurantManager.approveRestaurant(restaurant);
        assertEquals(RestaurantStatus.APPROVED, restaurant.getStatus());

        // Check it's in approved list
        List<Restaurant> approved = restaurantManager.getApprovedRestaurants();
        assertTrue(approved.contains(restaurant));
    }

    @Test
    @DisplayName("Food Menu Management")
    void testFoodMenuManagement() {
        Manager manager = (Manager) UserManager.getInstance().signUpManager("Test3", "Manager", "09120000002", "test123");
        Restaurant restaurant = restaurantManager.createRestaurant("Test Restaurant 3", manager, "Address", 2, Arrays.asList(FoodType.IRANIAN));
        restaurantManager.approveRestaurant(restaurant);

        // Add food item
        Food food = new Food("Test Kebab", 25000, FoodCategory.MAIN_DISH);
        food.setIngredients("Fresh meat, rice, spices");
        food.setCookingTime(20);
        food.setServingType(ServingType.PLATED);

        restaurantManager.addFoodToRestaurant(restaurant, food);

        assertEquals(1, restaurant.getMenu().size());
        assertEquals("Test Kebab", restaurant.getMenu().get(0).getName());
        assertEquals(25000, restaurant.getMenu().get(0).getPrice());
    }

    @Test
    @DisplayName("Order Creation and Processing")
    void testOrderProcessing() {
        // Setup test data
        Customer customer = (Customer) UserManager.getInstance().signUpCustomer("Test", "Customer", "09121111111", "test123");
        customer.setWallet(100000); // Set wallet balance

        Manager manager = (Manager) UserManager.getInstance().signUpManager("Test4", "Manager", "09120000003", "test123");
        Restaurant restaurant = restaurantManager.createRestaurant("Test Restaurant 4", manager, "Address", 1, Arrays.asList(FoodType.IRANIAN));
        restaurantManager.approveRestaurant(restaurant);

        // Add food to restaurant
        Food food = new Food("Test Food", 30000, FoodCategory.MAIN_DISH);
        restaurantManager.addFoodToRestaurant(restaurant, food);

        // Create order
        List<OrderItem> items = Arrays.asList(new OrderItem(food, 2));
        ir.ac.kntu.models.Order order = orderManager.createOrder(customer, restaurant, items, 5000, new Address("Test Address", 1));

        assertNotNull(order);
        assertEquals(customer, order.getCustomer());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals(OrderStatus.REGISTERED, order.getStatus());
        assertEquals(65000, order.getFinalAmount()); // 60000 + 5000 delivery
    }

    @Test
    @DisplayName("Text Similarity Algorithms")
    void testTextSimilarity() {
        // Test Levenshtein distance
        int distance = TextSimilarity.levenshteinDistance("kitten", "sitting");
        assertEquals(3, distance);

        // Test similarity score
        double similarity = TextSimilarity.levenshteinSimilarity("kitten", "kitten");
        assertEquals(1.0, similarity, 0.01);

        // Test combined similarity
        double combined = TextSimilarity.combinedSimilarity("pizza", "cheese pizza");
        assertTrue(combined > 0.5);

        // Test search results
        List<String> candidates = Arrays.asList("cheese pizza", "pepperoni pizza", "pasta", "burger");
        List<TextSimilarity.SearchResult> results = TextSimilarity.findBestMatches("pizza", candidates, 2);

        assertEquals(2, results.size());
        assertTrue(results.get(0).text.contains("pizza"));
    }

    @Test
    @DisplayName("Restaurant Search Functionality")
    void testRestaurantSearch() {
        // Create test restaurants
        Manager manager1 = (Manager) UserManager.getInstance().signUpManager("Test5", "Manager", "09120000004", "test123");
        Manager manager2 = (Manager) UserManager.getInstance().signUpManager("Test6", "Manager", "09120000005", "test123");

        Restaurant rest1 = restaurantManager.createRestaurant("Pizza Palace", manager1, "Address 1", 1, Arrays.asList(FoodType.FAST_FOOD));
        Restaurant rest2 = restaurantManager.createRestaurant("Burger King", manager2, "Address 2", 2, Arrays.asList(FoodType.FAST_FOOD));

        restaurantManager.approveRestaurant(rest1);
        restaurantManager.approveRestaurant(rest2);

        // Add food items
        Food pizza = new Food("Margherita Pizza", 45000, FoodCategory.MAIN_DISH);
        Food burger = new Food("Cheese Burger", 35000, FoodCategory.MAIN_DISH);
        restaurantManager.addFoodToRestaurant(rest1, pizza);
        restaurantManager.addFoodToRestaurant(rest2, burger);

        // Test search by restaurant name
        List<Restaurant> results = restaurantManager.searchRestaurants("Pizza");
        assertTrue(results.contains(rest1));

        // Test search by food name
        results = restaurantManager.searchRestaurants("Burger");
        assertTrue(results.contains(rest2));
    }

    @Test
    @DisplayName("Cart Management")
    void testCartManagement() {
        // Setup test data
        Manager manager = (Manager) UserManager.getInstance().signUpManager("Test7", "Manager", "09120000006", "test123");
        Restaurant restaurant = restaurantManager.createRestaurant("Test Restaurant 5", manager, "Address", 1, Arrays.asList(FoodType.FAST_FOOD));
        restaurantManager.approveRestaurant(restaurant);

        Food food1 = new Food("Item 1", 20000, FoodCategory.MAIN_DISH);
        Food food2 = new Food("Item 2", 15000, FoodCategory.APPETIZER);
        restaurantManager.addFoodToRestaurant(restaurant, food1);
        restaurantManager.addFoodToRestaurant(restaurant, food2);

        // Test cart operations
        cartManager.addToCart(restaurant, food1, 2);
        assertEquals(40000, cartManager.getCurrentCart().getTotal());

        cartManager.addToCart(restaurant, food2, 1);
        assertEquals(55000, cartManager.getCurrentCart().getTotal());
        assertEquals(3, cartManager.getCurrentCart().getItemCount());

        // Test quantity change
        OrderItem item1 = cartManager.getCurrentCart().getItems().get(0);
        cartManager.changeQuantity(item1, 1);
        assertEquals(35000, cartManager.getCurrentCart().getTotal());
    }

    @Test
    @DisplayName("User Authentication")
    void testUserAuthentication() {
        // Test customer registration and login
        Customer customer = (Customer) UserManager.getInstance().signUpCustomer("John", "Doe", "09122222222", "password123");

        assertNotNull(customer);
        assertEquals("John", customer.getName());
        assertEquals("Doe", customer.getLastName());

        // Test login
        User loggedIn = UserManager.getInstance().signInUser("09122222222", "password123");
        assertNotNull(loggedIn);
        assertEquals(customer, loggedIn);

        // Test invalid login
        User invalidLogin = UserManager.getInstance().signInUser("09122222222", "wrongpassword");
        assertNull(invalidLogin);
    }

    @Test
    @DisplayName("Address Management")
    void testAddressManagement() {
        Customer customer = (Customer) UserManager.getInstance().signUpCustomer("Jane", "Smith", "09123333333", "password123");

        // Add addresses
        Address address1 = new Address("123 Main St", 5);
        Address address2 = new Address("456 Oak Ave", 3);

        customer.addAddress(address1);
        customer.addAddress(address2);

        assertEquals(2, customer.getAddresses().size());
        assertEquals(address1, customer.getAddressById(1));
        assertEquals(address2, customer.getAddressById(2));

        // Remove address
        customer.removeAddress(address1);
        assertEquals(1, customer.getAddresses().size());
    }
}
