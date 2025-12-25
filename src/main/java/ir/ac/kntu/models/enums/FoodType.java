package ir.ac.kntu.models.enums;

public enum FoodType {
    FAST_FOOD("Fast Food"),
    IRANIAN("Iranian"),
    SEAFOOD("Seafood"),
    INTERNATIONAL("International"),
    CAFE("Cafe"),
    BEVERAGE("Beverage");

    private final String displayName;

    FoodType(String displayName) {
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
