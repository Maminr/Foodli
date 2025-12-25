package ir.ac.kntu.models.enums;

public enum OrderStatus {
    REGISTERED("Registered"),
    PREPARING("Preparing"),
    SENT("Sent"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");

    private final String displayName;

    OrderStatus(String displayName) {
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
