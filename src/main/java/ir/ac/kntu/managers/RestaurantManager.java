package ir.ac.kntu.managers;

import ir.ac.kntu.models.Restaurant;
import ir.ac.kntu.models.Food;
import ir.ac.kntu.models.Manager;
import ir.ac.kntu.models.enums.FoodType;
import ir.ac.kntu.models.enums.RestaurantStatus;
import ir.ac.kntu.utilities.TextSimilarity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * RestaurantManager - Business logic for restaurant operations
 *
 * BONUS FEATURES TODO:
 *
 * Unit Testing:
 * TODO: Create comprehensive JUnit test suite for RestaurantManager
 * TODO: Test restaurant CRUD operations
 * TODO: Test search and filtering functionality
 * TODO: Test approval workflow and status management
 * TODO: Test food menu management operations
 *
 * Data Persistence:
 * TODO: Implement JDBC-based restaurant data persistence
 * TODO: Create database schema for restaurants and menus
 * TODO: Add caching layer for frequently accessed restaurant data
 * TODO: Implement data validation and integrity constraints
 *
 * Search Optimization:
 * TODO: Implement text similarity algorithms for restaurant/food search
 * TODO: Add full-text search indexing for better performance
 * TODO: Implement location-based search with geospatial queries
 * TODO: Add recommendation engine based on user preferences
 *
 * Advanced Features:
 * TODO: Implement restaurant capacity management and reservation system
 * TODO: Add dynamic pricing based on demand and time
 * TODO: Implement restaurant rating prediction algorithms
 * TODO: Add integration with external review platforms
 * TODO: Implement automated menu translation for international customers
 */

public class RestaurantManager {
    private static RestaurantManager instance;
    private List<Restaurant> restaurants;

    private RestaurantManager() {
        restaurants = new ArrayList<>();
    }

    public static RestaurantManager getInstance() {
        if (instance == null) {
            instance = new RestaurantManager();
        }
        return instance;
    }

    public Restaurant createRestaurant(String name, Manager manager, String address,
                                       int zoneNumber, List<FoodType> foodTypes) {
        Restaurant restaurant = new Restaurant(name, manager, address, zoneNumber, foodTypes);
        restaurant.setId(restaurants.size() + 1);
        restaurants.add(restaurant);
        return restaurant;
    }

    public Restaurant createRestaurant(String name, Manager manager, String address,
                                       int zoneNumber, List<FoodType> foodTypes,
                                       double baseDeliveryCost, double perZoneCost) {
        Restaurant restaurant = new Restaurant(name, manager, address, zoneNumber, foodTypes,
                baseDeliveryCost, perZoneCost);
        restaurant.setId(restaurants.size() + 1);
        restaurants.add(restaurant);
        return restaurant;
    }

    public Restaurant findRestaurantById(int id) {
        return restaurants.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Restaurant findRestaurantByManager(Manager manager) {
        // Prioritize restaurants by status: Approved > Pending > Rejected
        // This ensures that if a manager has multiple restaurants (e.g., one rejected and one approved),
        // we return the most relevant one (approved if available, otherwise pending, otherwise rejected)
        return restaurants.stream()
                .filter(r -> r.getManager().equals(manager))
                .sorted((r1, r2) -> {
                    // Priority: APPROVED (0) > PENDING_REVIEW (1) > REJECTED (2)
                    int priority1 = r1.getStatus() == RestaurantStatus.APPROVED ? 0 :
                            r1.getStatus() == RestaurantStatus.PENDING_REVIEW ? 1 : 2;
                    int priority2 = r2.getStatus() == RestaurantStatus.APPROVED ? 0 :
                            r2.getStatus() == RestaurantStatus.PENDING_REVIEW ? 1 : 2;
                    return Integer.compare(priority1, priority2);
                })
                .findFirst()
                .orElse(null);
    }

    public List<Restaurant> findRestaurantsByName(String name) {
        return restaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Restaurant> findRestaurantsByFoodType(FoodType foodType) {
        return restaurants.stream()
                .filter(r -> r.getFoodTypes().contains(foodType))
                .collect(Collectors.toList());
    }

    public List<Restaurant> findRestaurantsByFoodName(String foodName) {
        return restaurants.stream()
                .filter(r -> r.getMenu().stream()
                        .anyMatch(f -> f.getName().toLowerCase().contains(foodName.toLowerCase())))
                .collect(Collectors.toList());
    }

    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants);
    }

    public List<Restaurant> getApprovedRestaurants() {
        return restaurants.stream()
                .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Restaurant> getPendingRestaurants() {
        return restaurants.stream()
                .filter(r -> r.getStatus() == RestaurantStatus.PENDING_REVIEW)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void approveRestaurant(Restaurant restaurant) {
        restaurant.setStatus(RestaurantStatus.APPROVED);
    }

    public void rejectRestaurant(Restaurant restaurant, String reason) {
        restaurant.setStatus(RestaurantStatus.REJECTED);
        restaurant.setRejectionReason(reason);
    }

    public void addFoodToRestaurant(Restaurant restaurant, Food food) {
        food.setId(restaurant.getMenu().size() + 1);
        restaurant.addFood(food);
    }

    public void removeFoodFromRestaurant(Restaurant restaurant, Food food) {
        restaurant.removeFood(food);
    }

    public List<Restaurant> searchRestaurants(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getApprovedRestaurants();
        }

        List<String> restaurantNames = restaurants.stream()
                .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
                .map(Restaurant::getName)
                .collect(Collectors.toList());

        List<String> foodNames = restaurants.stream()
                .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
                .flatMap(r -> r.getMenu().stream())
                .filter(Food::isAvailable)
                .map(Food::getName)
                .collect(Collectors.toList());

        // Use text similarity for intelligent search
        List<TextSimilarity.SearchResult> restaurantMatches = TextSimilarity.findBestMatches(query, restaurantNames, 10);
        List<TextSimilarity.SearchResult> foodMatches = TextSimilarity.findBestMatches(query, foodNames, 10);

        List<Restaurant> results = new ArrayList<>();

        // Add restaurants that match by name
        for (TextSimilarity.SearchResult match : restaurantMatches) {
            if (match.getScore() > 0.3) { // Minimum similarity threshold
                results.addAll(findRestaurantsByName(match.getText()));
            }
        }

        // Add restaurants that have matching foods
        for (TextSimilarity.SearchResult match : foodMatches) {
            if (match.getScore() > 0.3) { // Minimum similarity threshold
                results.addAll(findRestaurantsByFoodName(match.getText()));
            }
        }

        // Remove duplicates and sort by relevance (approved restaurants first)
        return results.stream()
                .distinct()
                .sorted((r1, r2) -> {
                    // Prioritize restaurants with higher ratings
                    return Double.compare(r2.getRating(), r1.getRating());
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Get autocomplete suggestions for search
     */
    public List<String> getSearchSuggestions(String partial) {
        List<String> suggestions = new ArrayList<>();

        // Restaurant name suggestions
        List<String> restaurantNames = getApprovedRestaurants().stream()
                .map(Restaurant::getName)
                .collect(Collectors.toList());

        suggestions.addAll(TextSimilarity.getAutocompleteSuggestions(partial, restaurantNames, 5));

        // Food name suggestions
        List<String> foodNames = getApprovedRestaurants().stream()
                .flatMap(r -> r.getMenu().stream())
                .filter(Food::isAvailable)
                .map(Food::getName)
                .collect(Collectors.toList());

        suggestions.addAll(TextSimilarity.getAutocompleteSuggestions(partial, foodNames, 5));

        // Remove duplicates and limit
        return suggestions.stream()
                .distinct()
                .limit(8)
                .collect(Collectors.toList());
    }
}
