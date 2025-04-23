package controller;

import controller.auth.AuthController;
import model.entities.AuthModel;
import view.auth.AuthView;

public class MainController {
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // authentication flow
            AuthView authView = new AuthView();
            AuthModel authModel = new AuthModel();
            AuthController authController = new AuthController(authView, authModel);
            
            // Display the login view
            authView.setVisible(true);
        });
    }
}