package controller.auth;

import model.entities.Auth;
import model.entities.AuthModel;
import view.auth.AuthView;

public class AuthController {
    
    private AuthView view;
    private AuthModel model;
    
    public AuthController(AuthView view, AuthModel model) {
        this.view = view;
        this.model = model;
        
        // Set up action listeners in the view
        this.view.setLoginButtonListener(e -> handleLogin());
        this.view.setCancelButtonListener(e -> handleCancel());
    }
    
    private void handleLogin() {
        String email = view.getEmail();
        String password = view.getPassword();
        
        if (email.isEmpty() || password.isEmpty()) {
            view.showErrorMessage("Email and password cannot be empty");
            return;
        }
        
        boolean success = model.authenticate(email, password);
        
        if (success) {
            Auth currentUser = model.getCurrentUser();
            view.showSuccessMessage("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
            
            // Open the appropriate view based on user role
            openAppropriateView(currentUser);
            
            // Close the login view
            view.dispose();
        } else {
            view.showErrorMessage("Invalid email or password. Please try again.");
        }
    }
    
    private void handleCancel() {
        System.exit(0);
    }
    
    private void openAppropriateView(Auth user) {
        // Logic to open the appropriate view based on user role
        // For example:
        // if (user.getRoleType().equals("ADMIN")) {
        //     new AdminController();
        // } else if (user.getRoleType().equals("STUDENT")) {
        //     new StudentController(user);
        // }
    }
    
}