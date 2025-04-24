package controller.admin;

import model.dao.CourseDA;
import model.dao.TeacherCourseDA;
import model.entities.Auth;
import view.admin.AdminView;
import view.admin.CourseManagementPanel;

import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import model.entities.Course;
import model.entities.CourseWithTeacher;
import model.entities.Teacher;

public class AdminController {
    
    private AdminView view;
    private Auth currentUser;
    
    // Controllers for specific features
    private AdminCourseController courseController;
    private AdminStudentController studentController;
    //private TeacherController teacherController;
    
    public AdminController(Auth user) {
        this.currentUser = user;
        
        // Initialize the admin view
        this.view = new AdminView(user);
        
        // Initialize feature controllers
        this.courseController = new AdminCourseController(view.getCoursePanel(), view);
        this.studentController = new AdminStudentController(view.getStudentPanel(), view);
        // this.teacherController = new TeacherController(view.getTeacherPanel(), view);
        
        // Set up menu listeners
        setupMenuListeners();

        
        // Load initial data
        this.studentController.loadStudentData();
        //loadTeacherData();
        loadCourseData();
        
        // Display the view
        this.view.setVisible(true);
    }
    
    private void setupListeners() {
        // Set up action listeners for the admin view
        // Example: this.view.setLogoutButtonListener(e -> handleLogout());
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
        // Implementation for logging out
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
                // Reload course data
                loadCourseData();
            } else {
                JOptionPane.showMessageDialog(view, "Failed to assign teacher", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Teacher showTeacherSelectionDialog(ArrayList<Teacher> teachers) {
        // Create a combo box with all teachers
        JComboBox<Teacher> teacherComboBox = new JComboBox<>();
        for (Teacher teacher : teachers) {
            teacherComboBox.addItem(teacher);
        }
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(view, teacherComboBox, "Select Teacher", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return (Teacher) teacherComboBox.getSelectedItem();
        }
        return null;
    }
}