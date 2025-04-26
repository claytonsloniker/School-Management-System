package controller.student;

import model.dao.StudentDA;
import model.entities.Auth;
import view.admin.AdminView;

public class StudentController {
    
    private AdminView view;
    private Auth currentUser;
    
    public StudentController(Auth user) {
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
    
    private void handleProfilePictureUpdate(String profilePicturePath) {
        StudentDA studentDA = new StudentDA();
        boolean success = studentDA.updateStudentProfilePicture(currentUser.getId(), profilePicturePath);
        
        if (success) {
            currentUser.setProfilePicture(profilePicturePath);
            view.updateProfilePicture(profilePicturePath);
        } else {
            view.showErrorMessage("Failed to update profile picture");
        }
    }
}