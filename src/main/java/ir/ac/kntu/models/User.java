package ir.ac.kntu.models;

import ir.ac.kntu.models.enums.UserRole;

public abstract class User {
    protected Long id;
    protected String name;
    protected String lastName;
    protected String phoneNumber;
    protected String password;
    protected UserRole role;

    public User(String name, String lastName, String phoneNumber, String password, UserRole role) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    public abstract void showMenu();

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return name + " " + lastName;
    }

    public UserRole getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "User: " + name + " (" + role + ")";
    }
}