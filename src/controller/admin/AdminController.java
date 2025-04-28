package controller.admin;

import model.dao.AdminDA;
import model.dao.CourseDA;
import model.dao.TeacherCourseDA;
import model.entities.Admin;
import model.entities.Auth;
import model.entities.AuthModel;
import view.admin.AdminView;
import view.admin.CourseManagementPanel;
import view.auth.AuthView;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import controller.auth.AuthController;
import model.entities.Course;
import model.entities.CourseWithTeacher;
import model.entities.Teacher;

public class AdminController {
    
    private AdminView view;
    private Admin currentUser;
    
    private AdminCourseController courseController;
    private AdminStudentController studentController;
    private AdminTeacherController teacherController;
    
    public AdminController(Admin user) {
        this.currentUser = user;
        
        //initialize admin view
        this.view = new AdminView(user);
        
        // Initialize feature controllers
        this.courseController = new AdminCourseController(view.getCoursePanel(), view);
        this.studentController = new AdminStudentController(view.getStudentPanel(), view);
        this.teacherController = new AdminTeacherController(view.getTeacherPanel(), view);
        
        view.addPropertyChangeListener(this::handlePropertyChange);
        
        setupMenuListeners();
        
        // initial data
        this.studentController.loadStudentData();
        this.teacherController.loadTeacherData();
        loadCourseData();
        
        this.view.setVisible(true);
    }
    
    private void setupMenuListeners() {
        view.setLogoutListener(e -> handleLogout());
        view.setStudentMenuItemListener(e -> view.showPanel("students"));
        view.setTeacherMenuItemListener(e -> view.showPanel("teachers"));
        view.setCourseMenuItemListener(e -> view.showPanel("courses"));
    }
    
    private void loadCourseData() {
        try {
            // Get courses with teachers from the database
            CourseDA courseDA = new CourseDA();
            ArrayList<CourseWithTeacher> coursesWithTeachers = courseDA.getCoursesWithTeachers();
            
            // Update the course table in the view
            view.getCoursePanel().updateCourseTable(coursesWithTeachers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, 
                "Error loading course data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleLogout() {
        // close admin view
        view.dispose();
        
        // Create and show the login view again
        javax.swing.SwingUtilities.invokeLater(() -> {
            AuthView authView = new AuthView();
            AuthModel authModel = new AuthModel();
            authModel.logout();
            AuthController authController = new AuthController(authView, authModel);
            
            authView.setVisible(true);
        });
    }

    private void handleAssignTeacher() {
        // Get the selected course
        CourseWithTeacher selectedCourse = view.getCoursePanel().getSelectedCourse();
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(view, "Please select a course first", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get all teachers
        ArrayList<Teacher> teachers = new TeacherCourseDA().getAllTeachers();
        
        // Show teacher selection dialog
        Teacher selectedTeacher = showTeacherSelectionDialog(teachers);
        if (selectedTeacher != null) {
            // Assign teacher to course
            boolean success = new TeacherCourseDA().assignTeacherToCourse(selectedTeacher.getId(), selectedCourse.getCode());
            if (success) {
                JOptionPane.showMessageDialog(view, "Teacher assigned successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadCourseData();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to assign teacher", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Teacher showTeacherSelectionDialog(ArrayList<Teacher> teachers) {
        // combo box with all teachers
        JComboBox<Teacher> teacherComboBox = new JComboBox<>();
        for (Teacher teacher : teachers) {
            teacherComboBox.addItem(teacher);
        }
        
        int result = JOptionPane.showConfirmDialog(view, teacherComboBox, "Select Teacher", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return (Teacher) teacherComboBox.getSelectedItem();
        }
        return null;
    }
    
    private void handleProfilePictureUpdate(String profilePicturePath) {
        AdminDA adminDA = new AdminDA();
        boolean success = adminDA.updateAdminProfilePicture(currentUser.getId(), profilePicturePath);
        
        if (success) {
            currentUser.setProfilePicture(profilePicturePath);
            view.updateProfilePicture(profilePicturePath);
            JOptionPane.showMessageDialog(view,
                "Profile picture updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view,
                "Failed to update profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleProfilePictureRemoval() {
        String oldProfilePicturePath = currentUser.getProfilePicture();
        AdminDA adminDA = new AdminDA();
        
        // Update db to set profile picture to null
        boolean success = adminDA.updateAdminProfilePicture(currentUser.getId(), null);
        
        if (success) {
            // Delete the old file if it exists
            if (oldProfilePicturePath != null && !oldProfilePicturePath.isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(oldProfilePicturePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // Update user model
            currentUser.setProfilePicture(null);
            
            // Update UI
            view.updateProfilePicture(null);
            
            JOptionPane.showMessageDialog(view, 
                "Profile picture removed successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view, 
                "Failed to remove profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handlePropertyChange(PropertyChangeEvent evt) {
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
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(view, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}