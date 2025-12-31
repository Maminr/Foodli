package ir.ac.kntu.models;

public class OrderItem {
    private Food food;
    private int quantity;
    private double unitPrice; // Price at time of order

    public OrderItem(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
        this.unitPrice = food.getPrice();
    }

    public OrderItem(Food food, int quantity, double unitPrice) {
        this.food = food;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Getters and setters
    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotalPrice() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return food.getName() + " x" + quantity + " = " + getTotalPrice() + " Toman";
    }
}
