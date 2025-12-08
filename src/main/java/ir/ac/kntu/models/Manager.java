package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

public class Manager extends User {

    public Manager(String name, String lastName, String phoneNumber, String password) {
        super(name, lastName, phoneNumber, password, UserRole.RESTAURANT_MANAGER);
    }

    @Override
    public void showMenu() {
        System.out.println("--- Manager MENU ---");
        
        
    }
}