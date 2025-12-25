package ir.ac.kntu.managers;

import ir.ac.kntu.models.*;
import ir.ac.kntu.models.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * OrderManager - Business logic for order processing and management
 *
 * BONUS FEATURES TODO:
 *
 * Unit Testing:
 * TODO: Create comprehensive JUnit test suite for OrderManager
 * TODO: Test order creation and validation
 * TODO: Test order status transitions and business rules
 * TODO: Test financial calculations and refunds
 * TODO: Test review and rating system
 *
 * Data Persistence:
 * TODO: Implement robust order data persistence with transactions
 * TODO: Add order history archiving and partitioning
 * TODO: Implement audit trail for all order modifications
 * TODO: Add data backup and disaster recovery procedures
 *
 * Advanced Order Processing:
 * TODO: Implement real-time order routing algorithms
 * TODO: Add automated order assignment to delivery personnel
 * TODO: Implement order preparation time prediction
 * TODO: Add integration with GPS tracking for deliveries
 * TODO: Implement order bundling for efficiency optimization
 *
 * Analytics & Reporting:
 * TODO: Generate HTML reports with order analytics and trends
 * TODO: Create customer behavior analysis and segmentation
 * TODO: Implement demand forecasting for restaurants
 * TODO: Add performance metrics for delivery times and satisfaction
 */

public class OrderManager {
    private static OrderManager instance;
    private List<Order> orders;

    private OrderManager() {
        orders = new ArrayList<>();
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public Order createOrder(User customer, Restaurant restaurant, List<OrderItem> items,
                             double deliveryCost, Address deliveryAddress) {
        Order order = new Order(customer, restaurant, items, deliveryCost, deliveryAddress);
        order.setId(orders.size() + 1);
        orders.add(order);
        return order;
    }

    public Order findOrderById(int id) {
        return orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Order> getOrdersByCustomer(User customer) {
        return orders.stream()
                .filter(o -> o.getCustomer().equals(customer))
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersByRestaurant(Restaurant restaurant) {
        return orders.stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .collect(Collectors.toList());
    }

    public List<Order> getActiveOrdersByRestaurant(Restaurant restaurant) {
        return orders.stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .filter(o -> o.getStatus() != OrderStatus.DELIVERED && o.getStatus() != OrderStatus.CANCELLED)
                .collect(Collectors.toList());
    }

    public List<Order> getNewOrdersByRestaurant(Restaurant restaurant) {
        return orders.stream()
                .filter(o -> o.getRestaurant().equals(restaurant))
                .filter(o -> o.getStatus() == OrderStatus.REGISTERED)
                .collect(Collectors.toList());
    }

    public void updateOrderStatus(Order order, OrderStatus newStatus) {
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(newStatus);

        if (newStatus == OrderStatus.CANCELLED && oldStatus == OrderStatus.REGISTERED) {
            // Refund customer
            Customer customer = (Customer) order.getCustomer();
            customer.setWallet(customer.getWallet() + order.getFinalAmount());
        }
    }

    public void addOrderReview(Order order, int rating, String comment) {
        if (order.getStatus() == OrderStatus.DELIVERED) {
            order.setReviewRating(rating);
            order.setReviewComment(comment);

            order.getRestaurant().addRating(rating);
        }
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orders);
    }
}
