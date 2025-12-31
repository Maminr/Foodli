package ir.ac.kntu.models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {
    private Restaurant restaurant;
    private List<OrderItem> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
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

    public void addItem(Food food, int quantity) {
        if (restaurant == null) {
            throw new IllegalStateException("Restaurant not set for cart");
        }

        // Don't add items with zero or negative quantity
        if (quantity <= 0) {
            return;
        }

        for (OrderItem item : items) {
            if (item.getFood().getId() == food.getId()) {
                int newQuantity = item.getQuantity() + quantity;
                if (newQuantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(newQuantity);
                }
                return;
            }
        }

        items.add(new OrderItem(food, quantity));
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public void clear() {
        items.clear();
        restaurant = null;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
    }

    public int getItemCount() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public void changeQuantity(OrderItem item, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(item);
        } else {
            item.setQuantity(newQuantity);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "Cart is empty";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Restaurant: ").append(restaurant.getName()).append("\n");
        sb.append("Items (").append(getItemCount()).append(" total):\n");

        for (OrderItem item : items) {
            sb.append("- ").append(item.toString()).append("\n");
        }

        sb.append("Total: ").append(getTotal()).append(" Toman\n");
        return sb.toString();
    }
}
