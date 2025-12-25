package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {

    private double wallet;
    private List<Address> addresses;

    public Customer(String name, String lastName, String phoneNumber, String password) {
        super(name, lastName, phoneNumber, password, UserRole.CUSTOMER);
        this.wallet = 0.0;
        this.addresses = new ArrayList<>();
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public void addToWallet(double amount) {
        this.wallet += amount;
    }

    public boolean deductFromWallet(double amount) {
        if (wallet >= amount) {
            wallet -= amount;
            return true;
        }
        return false;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        address.setId(addresses.size() + 1);
        addresses.add(address);
    }

    public void removeAddress(Address address) {
        addresses.remove(address);
    }

    public Address getAddressById(int id) {
        return addresses.stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void showMenu() {
        System.out.println("--- CUSTOMER MENU ---");
        System.out.println("1. Restaurant Search & Selection");
        System.out.println("2. Shopping Cart & Order");
        System.out.println("3. Order Management");
        System.out.println("4. Account Settings");
        System.out.println("0. Logout");
    }

    @Override
    public String toString() {
        return super.toString() + ", wallet=" + wallet + ", addresses=" + addresses.size();
    }
}