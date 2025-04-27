package controller.auth;

import model.dao.AdminDA;
import model.dao.StudentDA;
import model.dao.TeacherDA;
import model.dao.UserDA;
import model.entities.Admin;
import model.entities.Auth;
import model.entities.AuthModel;
import model.entities.Student;
import model.entities.Teacher;
import util.security.PasswordUtil;
import util.email.*;
import view.auth.AuthView;
import view.auth.dialogs.ForgotPasswordDialog;

import javax.swing.JOptionPane;

import controller.admin.AdminController;
import controller.student.StudentController;
import controller.teacher.TeacherController;

public class AuthController {
    
    private AuthView view;
    private AuthModel model;
    
    public AuthController(AuthView view, AuthModel model) {
        this.view = view;
        this.model = model;
        
        // Set up action listeners in the view
        this.view.setLoginButtonListener(e -> handleLogin());
        this.view.setCancelButtonListener(e -> handleCancel());
        this.view.setForgotPasswordButtonListener(e -> handleForgotPassword());
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
            
            // Navigate to the appropriate view based on user role
            navigateByRole(currentUser);
            
            // Close the login view
            view.dispose();
        } else {
            view.showErrorMessage("Invalid email or password. Please try again.");
        }
    }
    
    private void handleForgotPassword() {
        ForgotPasswordDialog dialog = new ForgotPasswordDialog(view);
        dialog.setVisible(true);
        
        if (dialog.isRecoveryRequested()) {
            String email = dialog.getEmail();
            processForgotPassword(email);
        }
    }

    private void processForgotPassword(String email) {
        UserDA userDA = new UserDA();
        
        // Check if email exists
        boolean emailExists = userDA.doesEmailExist(email);
        
        System.out.println("DEBUG: Email '" + email + "' exists in database: " + emailExists);
        
        if (!emailExists) {
            // Don't reveal if email exists or not for security
            JOptionPane.showMessageDialog(view, 
                "If your email is registered in our system, you will receive a temporary password shortly.",
                "Password Reset",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            // Generate temporary password
            String tempPassword = PasswordUtil.generateTemporaryPassword(10);
            String hashedTempPassword = PasswordUtil.hashPassword(tempPassword);
            
            System.out.println("DEBUG: Generated temporary password: " + tempPassword);
            System.out.println("DEBUG: Hashed password: " + hashedTempPassword);
            
            // Update user password in database
            boolean passwordUpdated = userDA.updatePasswordByEmail(email, hashedTempPassword);
            System.out.println("DEBUG: Password updated in database: " + passwordUpdated);
            
            if (passwordUpdated) {
                // Send email with temporary password
                EmailUtil.sendTemporaryPasswordEmail(email, tempPassword);
                
                JOptionPane.showMessageDialog(view, 
                    "A temporary password has been sent to your email.",
                    "Password Reset",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(view, 
                    "There was an error processing your request. Please try again later.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "There was an error sending the email. Please try again later.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCancel() {
        System.exit(0);
    }
    
    /**
     * Navigate to the appropriate view based on user role
     * @param user The authenticated user
     */
    private void navigateByRole(Auth user) {
        switch (user.getRoleType()) {
            case "admin":
                // Get full Admin object from database
                AdminDA adminDA = new AdminDA();
                Admin adminUser = adminDA.getAdminById(user.getId());
                
                if (adminUser == null) {
                    view.showErrorMessage("Failed to load admin user data");
                    return;
                }
                
                // Initialize admin controller
                new AdminController(adminUser);
                break;
                
            case "student":
                // Get full Student object from database
                StudentDA studentDA = new StudentDA();
                Student studentUser = studentDA.getStudentById(user.getId());
                
                if (studentUser == null) {
                    view.showErrorMessage("Failed to load student user data");
                    return;
                }
                
                // Initialize student controller
                new StudentController(studentUser);
                break;
                
            case "teacher":
                // Get full Teacher object from database
                TeacherDA teacherDA = new TeacherDA();
                Teacher teacherUser = teacherDA.getTeacherById(user.getId());
                
                if (teacherUser == null) {
                    view.showErrorMessage("Failed to load teacher user data");
                    return;
                }
                
                // Initialize teacher controller
                new TeacherController(teacherUser);
                break;
                
            default:
                view.showErrorMessage("Unknown role type: " + user.getRoleType());
                break;
        }
    }
}