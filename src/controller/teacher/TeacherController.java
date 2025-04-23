package controller.teacher;

import model.entities.Auth;
import view.admin.AdminView;

public class TeacherController {
    
    private AdminView view;
    private Auth currentUser;
    
    public TeacherController(Auth user) {
        this.currentUser = user;
        
        // Initialize the admin view
        this.view = new AdminView(user);
        
        // Set up any listeners or additional initialization
        setupListeners();
        
        // Display the view
        this.view.setVisible(true);
    }
    
    private void setupListeners() {
        // Set up action listeners for the admin view
        // Example: this.view.setLogoutButtonListener(e -> handleLogout());
    }
    
    // Additional controller methods
}