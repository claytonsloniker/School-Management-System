package model.entities;

import model.dao.AuthDA;

public class AuthModel {
    
    private Auth currentUser;
    private AuthDA authDA;
    
    public AuthModel() {
        authDA = new AuthDA();
        currentUser = null;
    }
    
    /**
     * Attempt to authenticate a user
     * @param email User's email
     * @param password User's password
     * @return true if authentication successful, false otherwise
     */
    public boolean authenticate(String email, String password) {
        Auth user = authDA.authenticateUser(email, password);
        
        if (user != null) {
            this.currentUser = user;
            return true;
        }
        return false;
    }
    
    /**
     * Check if a user is currently authenticated
     * @return true if a user is logged in, false otherwise
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Get the currently authenticated user
     * @return Auth object for current user or null if no user is authenticated
     */
    public Auth getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Log out the current user
     */
    public void logout() {
        currentUser = null;
    }
}