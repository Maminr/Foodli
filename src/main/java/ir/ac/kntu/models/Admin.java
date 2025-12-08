package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

public class Admin extends User {

    public Admin(String name, String lastName, String phoneNumber, String password) {
        super(name, lastName, phoneNumber, password, UserRole.ADMIN);
    }

    @Override
    public void showMenu() {
        System.out.println("--- ADMIN MENU ---");
        System.out.println("1. Approve Restaurants");
        System.out.println("2. Ban Users");
    }
}