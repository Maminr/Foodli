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

    /**
     * Saves the user to the session (Logs them in).
     */
    public void login(User user) {
        this.currentUser = user;
    }

    /**
     * Removes the user from the session (Logs them out).
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Checks if anyone is currently logged in.
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Returns the currently logged-in user.
     * Warning: Check isLoggedIn() before calling this, or handle null.
     */
    public User getCurrentUser() {
        return currentUser;
    }
}