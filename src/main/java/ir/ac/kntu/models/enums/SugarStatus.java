package ir.ac.kntu.models.enums;

public enum SugarStatus {
    DIET("Diet"),
    REGULAR("Regular");

    private final String displayName;

    SugarStatus(String displayName) {
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
