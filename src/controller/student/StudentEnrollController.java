package controller.student;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import model.entities.CourseWithTeacher;
import view.student.StudentEnrollPanel;

public class StudentEnrollController {
    
    private StudentEnrollPanel view;
    private StudentController parentController;
    
    public StudentEnrollController(StudentEnrollPanel view, StudentController parentController) {
        this.view = view;
        this.parentController = parentController;
        
        // Set up listeners
        setupListeners();
        
        // Load initial data
        loadAvailableCourses();
    }
    
    private void setupListeners() {
        // Set up enroll button listener
        view.setEnrollButtonListener(e -> handleEnrollCourse());
        
        // Set up search and filter listeners
        view.setSearchListener(e -> handleSearch(e));
        view.setResetListener(e -> {
            loadAvailableCourses();
            view.clearFilter();
        });
    }
    
    public void loadAvailableCourses() {
        try {
            // Get available courses (not full) from database
            ArrayList<CourseWithTeacher> availableCourses = 
                parentController.getStudentDA().getAvailableCoursesForStudent(
                    parentController.getStudent().getId()
                );
            
            // Update view
            view.updateCourseTable(availableCourses);
        } catch (Exception e) {
            parentController.showErrorMessage("Error loading available courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void handleSearch(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        String[] parts = actionCommand.split("\\|");
        
        if (parts.length == 2) {
            String searchText = parts[0];
            String filterCriteria = parts[1];
            
            // Apply filter directly to the table
            view.applyFilter(searchText, filterCriteria);
        }
    }
    
    private void handleEnrollCourse() {
        CourseWithTeacher selectedCourse = view.getSelectedCourse();
        
        if (selectedCourse == null) {
            parentController.showErrorMessage("Please select a course to enroll in");
            return;
        }
        
        // Check if the student is already enrolled in the course
        boolean isEnrolled = parentController.getStudentDA().isStudentEnrolledInCourse(
            parentController.getStudent().getId(),
            selectedCourse.getCode()
        );
        
        if (isEnrolled) {
            JOptionPane.showMessageDialog(
                parentController.getStudentView(), 
                "You are already enrolled in this course", 
                "Already Enrolled", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        //Confirm enrollment
        int confirm = JOptionPane.showConfirmDialog(
            parentController.getStudentView(),
            "Are you sure you want to enroll in " + selectedCourse.getName() + "?",
            "Confirm Enrollment",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Enroll the student in the course
            boolean success = parentController.getStudentDA().enrollStudentInCourse(
                parentController.getStudent().getId(),
                selectedCourse.getCode()
            );
            
            if (success) {
                JOptionPane.showMessageDialog(
                    parentController.getStudentView(),
                    "Successfully enrolled in " + selectedCourse.getName(),
                    "Enrollment Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                //refresh available courses list and enrolled courses list
                loadAvailableCourses();
                parentController.getCourseController().updateCourseTable(
                    parentController.getStudentDA().getCoursesForStudent(
                        parentController.getStudent().getId()
                    )
                );
            } else {
                JOptionPane.showMessageDialog(
                    parentController.getStudentView(),
                    "Failed to enroll in course. Please try again.",
                    "Enrollment Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}