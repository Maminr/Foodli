package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 * Order Model - Complete order lifecycle management
 *
 * BONUS FEATURES TODO:
 *
 * Data Persistence:
 * TODO: Implement complex database relationships and constraints
 * TODO: Add order versioning for modification tracking
 * TODO: Implement order status change auditing
 * TODO: Add geospatial indexing for delivery optimization
 *
 * Advanced Features:
 * TODO: Implement order modification and cancellation policies
 * TODO: Add delivery tracking with real-time GPS updates
 * TODO: Implement order bundling for multi-restaurant orders
 * TODO: Add integration with payment gateways and refunds
 * TODO: Implement automated order routing algorithms
 */

public class Order {
    private int id;
    private final User customer;
    private Restaurant restaurant;
    private List<OrderItem> items;
    private double deliveryCost;
    private Address deliveryAddress;
    private OrderStatus status;
    private final LocalDateTime orderTime;
    private String reviewComment;
    private int reviewRating;

    public Order(User customer, Restaurant restaurant, List<OrderItem> items,
                 double deliveryCost, Address deliveryAddress) {
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items != null ? items : new ArrayList<>();
        this.deliveryCost = deliveryCost;
        this.deliveryAddress = deliveryAddress;
        this.status = OrderStatus.REGISTERED;
        this.orderTime = LocalDateTime.now();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCustomer() {
        return customer;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getItemsTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public double getFinalAmount() {
        return getItemsTotal() + deliveryCost;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public void setReviewRating(int reviewRating) {
        this.reviewRating = reviewRating;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public String getInvoice() {
        StringBuilder invoice = new StringBuilder();
        invoice.append("Restaurant: ").append(restaurant.getName()).append("\n");
        invoice.append("Order Time: ").append(orderTime).append("\n");
        invoice.append("Delivery Address: ").append(deliveryAddress.getDescription()).append("\n");
        invoice.append("Status: ").append(status.getDisplayName()).append("\n\n");

        invoice.append("Items:\n");
        for (OrderItem item : items) {
            invoice.append("- ").append(item.getFood().getName())
                    .append(" x").append(item.getQuantity())
                    .append(" = ").append(item.getTotalPrice()).append(" Toman\n");
        }

        invoice.append("\nItems Total: ").append(getItemsTotal()).append(" Toman\n");
        invoice.append("Delivery Cost: ").append(deliveryCost).append(" Toman\n");
        invoice.append("Total: ").append(getFinalAmount()).append(" Toman\n");

        return invoice.toString();
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", restaurant='" + restaurant.getName() + '\'' +
                ", total=" + getFinalAmount() +
                ", status=" + status +
                ", time=" + orderTime +
                '}';
    }
}
