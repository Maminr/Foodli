package ir.ac.kntu.models;

public class Address {
    private int id;
    private String description;
    private int zoneNumber; // 1-20

    public Address(String description, int zoneNumber) {
        this.description = description;
        this.zoneNumber = zoneNumber;
    }

    public Address(int id, String description, int zoneNumber) {
        this.id = id;
        this.description = description;
        this.zoneNumber = zoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getZoneNumber() {
        return zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", zoneNumber=" + zoneNumber +
                '}';
    }
}
