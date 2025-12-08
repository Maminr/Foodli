package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

public class Customer extends User {

    private int wallet; // Specific to Customer

    public Customer(String name, String lastName, String phoneNumber, String password) {
        super(name, lastName, phoneNumber, password, UserRole.CUSTOMER);
        this.wallet = 0;
    }

    @Override
    public void showMenu() {
        System.out.println("--- CUSTOMER MENU ---");
        System.out.println("1. Order Food");
        System.out.println("2. Charge Wallet");
    }
}