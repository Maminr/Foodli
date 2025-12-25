package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.*;

public class Food {
    private int id;
    private String name;
    private double price;
    private boolean available;
    private FoodCategory category;

    // Main dish specific fields
    private String ingredients;
    private int cookingTime; // in minutes
    private ServingType servingType;

    // Appetizer specific fields
    private int piecesPerServing;
    private PortionSize portionSize;

    // Beverage specific fields
    private int volume; // in ml
    private DrinkPackaging packaging;
    private SugarStatus sugarStatus;

    public Food(String name, double price, FoodCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.available = true;
    }

    public Food(int id, String name, double price, boolean available, FoodCategory category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.category = category;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    // Main dish methods
    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public ServingType getServingType() {
        return servingType;
    }

    public void setServingType(ServingType servingType) {
        this.servingType = servingType;
    }

    // Appetizer methods
    public int getPiecesPerServing() {
        return piecesPerServing;
    }

    public void setPiecesPerServing(int piecesPerServing) {
        this.piecesPerServing = piecesPerServing;
    }

    public PortionSize getPortionSize() {
        return portionSize;
    }

    public void setPortionSize(PortionSize portionSize) {
        this.portionSize = portionSize;
    }

    // Beverage methods
    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public DrinkPackaging getPackaging() {
        return packaging;
    }

    public void setPackaging(DrinkPackaging packaging) {
        this.packaging = packaging;
    }

    public SugarStatus getSugarStatus() {
        return sugarStatus;
    }

    public void setSugarStatus(SugarStatus sugarStatus) {
        this.sugarStatus = sugarStatus;
    }

    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Name: ").append(name).append("\n");
        details.append("Price: ").append(price).append(" Toman\n");
        details.append("Category: ").append(category.getDisplayName()).append("\n");
        details.append("Available: ").append(available ? "Yes" : "No").append("\n");

        switch (category) {
            case MAIN_DISH:
                if (ingredients != null) {
                    details.append("Ingredients: ").append(ingredients).append("\n");
                }
                if (cookingTime > 0) {
                    details.append("Cooking Time: ").append(cookingTime).append(" minutes\n");
                }
                if (servingType != null) {
                    details.append("Serving Type: ").append(servingType.getDisplayName()).append("\n");
                }
                break;
            case APPETIZER:
                if (piecesPerServing > 0) {
                    details.append("Pieces per Serving: ").append(piecesPerServing).append("\n");
                }
                if (portionSize != null) {
                    details.append("Portion Size: ").append(portionSize.getDisplayName()).append("\n");
                }
                break;
            case BEVERAGE:
                if (volume > 0) {
                    details.append("Volume: ").append(volume).append(" ml\n");
                }
                if (packaging != null) {
                    details.append("Packaging: ").append(packaging.getDisplayName()).append("\n");
                }
                if (sugarStatus != null) {
                    details.append("Sugar Status: ").append(sugarStatus.getDisplayName()).append("\n");
                }
                break;
            default:
                break;
        }

        return details.toString();
    }

    @Override
    public String toString() {
        return name + " - " + price + " Toman (" + (available ? "Available" : "Unavailable") + ")";
    }
}
