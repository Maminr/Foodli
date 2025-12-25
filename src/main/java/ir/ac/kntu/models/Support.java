package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

public class Support extends User {

    public Support(String name, String lastName, String phoneNumber, String password) {
        super(name, lastName, phoneNumber, password, UserRole.SUPPORT);
    }

    @Override
    public void showMenu() {
        System.out.println("--- SUPPORT DASHBOARD ---");
        System.out.println("1. Restaurant Approvals");
        System.out.println("2. System Statistics");
        System.out.println("3. User Management");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return "Support{" +
                "name='" + getName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                '}';
    }
}
