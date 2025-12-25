package ir.ac.kntu.managers;

import ir.ac.kntu.models.Customer;
import ir.ac.kntu.models.Manager;
import ir.ac.kntu.models.Support;
import ir.ac.kntu.models.User;

/*
 * UserManager - User authentication and management
 *
 * BONUS FEATURES TODO:
 *
 * Unit Testing:
 * TODO: Create comprehensive JUnit tests for user management
 * TODO: Test authentication flows for all user types
 * TODO: Test user registration and validation
 * TODO: Test role-based access control
 *
 * Data Persistence:
 * TODO: Implement secure user data storage with encryption
 * TODO: Add user session management and JWT tokens
 * TODO: Implement password reset and account recovery
 * TODO: Add two-factor authentication support
 *
 * Security Enhancements:
 * TODO: Implement password strength requirements and validation
 * TODO: Add brute force protection and rate limiting
 * TODO: Implement secure password hashing (bcrypt/PBKDF2)
 * TODO: Add audit logging for security events
 *
 * Advanced User Features:
 * TODO: Implement user profile management and customization
 * TODO: Add social login integration (Google, Facebook)
 * TODO: Implement user referral and rewards system
 * TODO: Add user behavior analytics and personalization
 */

import java.util.ArrayList;

public class UserManager {
    private static UserManager instance = null;
    private ArrayList<User> users;

    private long idCounter = 1;

    
    private UserManager() {
        users = new ArrayList<>();

        // Create multiple support accounts
        users.add(new Support("Support", "Team", "support", "support"));
        users.add(new Support("Admin", "Support", "09123456789", "support123"));
        users.add(new Support("Technical", "Support", "09129876543", "tech456"));
        users.add(new Support("Customer", "Care", "09121234567", "care789"));
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User signUpCustomer(String name, String lastName, String phone, String password) {
        if (findUserByPhoneNumber(phone) != null) {
            return null ;
        }

        Customer newCustomer = new Customer(name, lastName, phone, password);
        newCustomer.setId(idCounter++);
        users.add(newCustomer);

        SessionManager.getInstance().login(newCustomer);

        return newCustomer;
    }

    public User signInUser(String phone, String password) {
        User user = findUserByPhoneNumber(phone);
        if (user != null && user.getPassword().equals(password)) {
            SessionManager.getInstance().login(user);
            return user;
        }
        return null;
    }

    public User signUpManager(String name, String lastName, String phone, String password) {
        if (findUserByPhoneNumber(phone) != null) return null;

        Manager newManager = new Manager(name, lastName, phone, password);
        newManager.setId(idCounter++);
        users.add(newManager);

        SessionManager.getInstance().login(newManager);

        return newManager;
    }

    public User findUserByPhoneNumber(String phoneNumber) {
        for (User user : users) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getUsersByRole(ir.ac.kntu.models.enums.UserRole role) {
        ArrayList<User> usersByRole = new ArrayList<>();
        for (User user : users) {
            if (user.getRole() == role) {
                usersByRole.add(user);
            }
        }
        return usersByRole;
    }
}
