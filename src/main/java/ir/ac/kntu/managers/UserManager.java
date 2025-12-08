package ir.ac.kntu.managers;

import ir.ac.kntu.models.Admin;
import ir.ac.kntu.models.Customer;
import ir.ac.kntu.models.Manager;
import ir.ac.kntu.models.User;

import java.util.ArrayList;

public class UserManager {
    private static UserManager instance = null;
    private ArrayList<User> users;

    private long idCounter = 1;

    
    private UserManager() {
        users = new ArrayList<>();
        users.add(new Admin("Super", "Admin", "admin", "admin"));
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


}
