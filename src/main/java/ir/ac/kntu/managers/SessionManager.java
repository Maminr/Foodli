package ir.ac.kntu.managers;

import ir.ac.kntu.models.User;

public class SessionManager {
    private static final SessionManager INSTANCE = new SessionManager();

    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {
        return INSTANCE;
    }

    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

//    public boolean isLoggedIn() {
//        return currentUser != null;
//    }

    public User getCurrentUser() {
        return currentUser;
    }
}