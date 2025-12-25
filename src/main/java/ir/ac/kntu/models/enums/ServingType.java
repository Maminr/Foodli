package ir.ac.kntu.models.enums;

public enum ServingType {
    PLATED("Plated"),
    SANDWICH("Sandwich");

    private final String displayName;

    ServingType(String displayName) {
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
