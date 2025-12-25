package ir.ac.kntu.models.enums;

public enum FoodCategory {
    MAIN_DISH("Main Dish"),
    APPETIZER("Appetizer"),
    BEVERAGE("Beverage");

    private final String displayName;

    FoodCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
