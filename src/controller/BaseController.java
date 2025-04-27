package controller;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import controller.auth.AuthController;
import model.dao.UserDA;
import model.entities.AuthModel;
import model.entities.User;
import view.BaseView;
import view.auth.AuthView;

public abstract class BaseController {
    
    protected BaseView view;
    protected User currentUser;
    protected UserDA userDA;
    
    /**
     * Constructor for BaseController
     * @param user The authenticated user
     * @param view The user interface view
     */
    protected BaseController(User user, BaseView view) {
        this.currentUser = user;
        this.view = view;
        this.userDA = new UserDA();
        
        // Set up common listeners
        setupBaseListeners();
        
        // Must be implemented by subclasses
        setupSpecificListeners();
    }
    
    protected void initialize() {
        // Load initial data
        loadInitialData();
    }
    
    /**
     * Set up listeners common to all user types
     */
    private void setupBaseListeners() {
        // Set up logout listener
        view.setLogoutListener(e -> handleLogout());
        
        // Set up profile picture listener
        view.addPropertyChangeListener(this::handlePropertyChange);
    }
    
    /**
     * Subclasses must implement this to set up specific listeners
     */
    protected abstract void setupSpecificListeners();
    
    /**
     * Load initial data for this user type
     */
    protected abstract void loadInitialData();
    
    /**
     * Handle logout action
     */
    protected void handleLogout() {
        // Close the current view
        view.dispose();
        
        // Create and show the login view again
        javax.swing.SwingUtilities.invokeLater(() -> {
            AuthView authView = new AuthView();
            AuthModel authModel = new AuthModel();
            authModel.logout();
            AuthController authController = new AuthController(authView, authModel);
            
            // Display the login view
            authView.setVisible(true);
        });
    }
    
    /**
     * Handle property change events from the view
     * @param evt The property change event
     */
    protected void handlePropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        switch (propertyName) {
            case "profilePictureUpdated":
                String newProfilePicturePath = (String) evt.getNewValue();
                handleProfilePictureUpdate(newProfilePicturePath);
                break;
                
            case "profilePictureRemoved":
                handleProfilePictureRemoval();
                break;
        }
    }
    
    /**
     * Handle profile picture update
     * @param profilePicturePath The new profile picture path
     */
    protected void handleProfilePictureUpdate(String profilePicturePath) {
        boolean success = userDA.updateProfilePicture(currentUser.getId(), currentUser.getRoleType(), profilePicturePath);
        
        if (success) {
            currentUser.setProfilePicture(profilePicturePath);
            view.updateProfilePicture(profilePicturePath);
            JOptionPane.showMessageDialog(view.getFrame(),
                "Profile picture updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                "Failed to update profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Handle profile picture removal
     */
    protected void handleProfilePictureRemoval() {
        // Get the current profile picture path before removing it
        String oldProfilePicturePath = currentUser.getProfilePicture();
        
        boolean success = userDA.updateProfilePicture(currentUser.getId(), currentUser.getRoleType(), null);
        
        if (success) {
            // Delete the old file if it exists
            if (oldProfilePicturePath != null && !oldProfilePicturePath.isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(oldProfilePicturePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    // Continue even if file deletion fails
                }
            }
            
            // Update user model
            currentUser.setProfilePicture(null);
            
            // Update UI
            view.updateProfilePicture(null);
            
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Profile picture removed successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Failed to remove profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Show an error message
     * @param message The error message to show
     */
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(view.getFrame(), 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}