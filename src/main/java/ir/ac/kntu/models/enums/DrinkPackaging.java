package ir.ac.kntu.models.enums;

public enum DrinkPackaging {
    CAN("Can"),
    BOTTLE("Bottle"),
    CUP("Cup");

    private final String displayName;

    DrinkPackaging(String displayName) {
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
