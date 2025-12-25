package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.FoodType;
import ir.ac.kntu.models.enums.RestaurantStatus;

import java.util.ArrayList;
import java.util.List;

/*
 * Restaurant Model - Core restaurant entity
 *
 * BONUS FEATURES TODO:
 *
 * Data Persistence:
 * TODO: Implement JPA/Hibernate annotations for database mapping
 * TODO: Add validation constraints for data integrity
 * TODO: Implement lazy loading for menu items and orders
 * TODO: Add indexing for search performance
 *
 * Advanced Features:
 * TODO: Add restaurant image and gallery management
 * TODO: Implement working hours and holiday scheduling
 * TODO: Add delivery zones and radius configuration
 * TODO: Implement capacity management for peak hours
 * TODO: Add integration with external rating systems
 */

public class Restaurant {
    private int id;
    private String name;
    private Manager manager;
    private String address;
    private int zoneNumber;
    private List<FoodType> foodTypes;
    private RestaurantStatus status;
    private String rejectionReason;
    private double rating;
    private int ratingCount;
    private double wallet;
    private List<Food> menu;
    private double baseDeliveryCost; // Base delivery cost
    private double perZoneCost; // Additional cost per zone difference

    public Restaurant(String name, Manager manager, String address, int zoneNumber, List<FoodType> foodTypes) {
        this.name = name;
        this.manager = manager;
        this.address = address;
        this.zoneNumber = zoneNumber;
        this.foodTypes = foodTypes != null ? foodTypes : new ArrayList<>();
        this.status = RestaurantStatus.PENDING_REVIEW;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.wallet = 0.0;
        this.menu = new ArrayList<>();
        this.baseDeliveryCost = 5000.0; // Default base cost
        this.perZoneCost = 1000.0; // Default per-zone cost
    }

    public Restaurant(String name, Manager manager, String address, int zoneNumber, List<FoodType> foodTypes,
                      double baseDeliveryCost, double perZoneCost) {
        this.name = name;
        this.manager = manager;
        this.address = address;
        this.zoneNumber = zoneNumber;
        this.foodTypes = foodTypes != null ? foodTypes : new ArrayList<>();
        this.status = RestaurantStatus.PENDING_REVIEW;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.wallet = 0.0;
        this.menu = new ArrayList<>();
        this.baseDeliveryCost = baseDeliveryCost;
        this.perZoneCost = perZoneCost;
    }

    public Restaurant(int id, String name, Manager manager, String address, int zoneNumber,
                      List<FoodType> foodTypes, RestaurantStatus status) {
        this.id = id;
        this.name = name;
        this.manager = manager;
        this.address = address;
        this.zoneNumber = zoneNumber;
        this.foodTypes = foodTypes != null ? foodTypes : new ArrayList<>();
        this.status = status;
        this.rating = 0.0;
        this.ratingCount = 0;
        this.wallet = 0.0;
        this.menu = new ArrayList<>();
        this.baseDeliveryCost = 5000.0; // Default base cost
        this.perZoneCost = 1000.0; // Default per-zone cost
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public List<FoodType> getFoodTypes() {
        return foodTypes;
    }

    public void setFoodTypes(List<FoodType> foodTypes) {
        this.foodTypes = foodTypes;
    }

    public RestaurantStatus getStatus() {
        return status;
    }

    public void setStatus(RestaurantStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public List<Food> getMenu() {
        return menu;
    }

    public void setMenu(List<Food> menu) {
        this.menu = menu;
    }

    public double getBaseDeliveryCost() {
        return baseDeliveryCost;
    }

    public void setBaseDeliveryCost(double baseDeliveryCost) {
        this.baseDeliveryCost = baseDeliveryCost;
    }

    public double getPerZoneCost() {
        return perZoneCost;
    }

    public void setPerZoneCost(double perZoneCost) {
        this.perZoneCost = perZoneCost;
    }

    public void addFood(Food food) {
        menu.add(food);
    }

    public void removeFood(Food food) {
        menu.remove(food);
    }

    public void addRating(int rating) {
        this.rating = (this.rating * ratingCount + rating) / (ratingCount + 1);
        ratingCount++;
    }

    public double getDeliveryCost(int customerZone) {
        // Delivery cost calculation based on zone difference using custom prices
        int zoneDifference = Math.abs(customerZone - zoneNumber);
        return baseDeliveryCost + (zoneDifference * perZoneCost);
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", zoneNumber=" + zoneNumber +
                ", foodTypes=" + foodTypes +
                ", status=" + status +
                ", rating=" + String.format("%.1f", rating) +
                ", wallet=" + wallet +
                '}';
    }
}
