package ir.ac.kntu.managers;

import ir.ac.kntu.models.*;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final ShoppingCart currentCart;

    private CartManager() {
        currentCart = new ShoppingCart();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public ShoppingCart getCurrentCart() {
        return currentCart;
    }

    public void addToCart(Restaurant restaurant, Food food, int quantity) {
        // Check if cart belongs to same restaurant
        if (!currentCart.isEmpty() && !currentCart.getRestaurant().equals(restaurant)) {
            throw new IllegalArgumentException("Cannot add items from different restaurants. Current cart will be cleared.");
        }

        currentCart.setRestaurant(restaurant);
        currentCart.addItem(food, quantity);
    }

    public void removeFromCart(OrderItem item) {
        currentCart.removeItem(item);
    }

    public void changeQuantity(OrderItem item, int newQuantity) {
        currentCart.changeQuantity(item, newQuantity);
    }

    public void clearCart() {
        currentCart.clear();
    }

    public boolean canAddToCart(Restaurant restaurant) {
        return !currentCart.isEmpty() && !currentCart.getRestaurant().equals(restaurant);
    }

    public Order checkout(Address deliveryAddress) {
        if (currentCart.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Customer customer = (Customer) SessionManager.getInstance().getCurrentUser();
        Restaurant restaurant = currentCart.getRestaurant();

        double deliveryCost = restaurant.getDeliveryCost(deliveryAddress.getZoneNumber());

        double totalAmount = currentCart.getTotal() + deliveryCost;
        if (customer.getWallet() < totalAmount) {
            throw new IllegalStateException("Insufficient wallet balance");
        }

        customer.setWallet(customer.getWallet() - totalAmount);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItem cartItem : currentCart.getItems()) {
            orderItems.add(new OrderItem(cartItem.getFood(), cartItem.getQuantity(), cartItem.getUnitPrice()));
        }
        
        Order order = OrderManager.getInstance().createOrder(
            customer, restaurant, orderItems, deliveryCost, deliveryAddress);

        clearCart();

        return order;
    }
}
