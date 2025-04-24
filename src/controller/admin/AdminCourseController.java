package controller.admin;

import java.util.ArrayList;
import javax.swing.*;

import model.dao.CourseDA;
import model.dao.TeacherCourseDA;
import model.entities.Course;
import model.entities.CourseWithTeacher;
import model.entities.Teacher;
import view.admin.CourseManagementPanel;
import view.admin.dialogs.*;

public class AdminCourseController {
    
    private CourseManagementPanel view;
    private CourseDA courseDA;
    private TeacherCourseDA teacherCourseDA;
    private JFrame parentFrame;
    
    public AdminCourseController(CourseManagementPanel view, JFrame parentFrame) {
        this.view = view;
        this.parentFrame = parentFrame;
        this.courseDA = new CourseDA();
        this.teacherCourseDA = new TeacherCourseDA();
        
        // Set up listeners
        setupListeners();
        
        // Load initial data
        loadCourseData();
    }
    
    private void setupListeners() {
        view.setAddButtonListener(e -> handleAddCourse());
        view.setEditButtonListener(e -> handleEditCourse());
        view.setDeleteButtonListener(e -> handleDeleteCourse());
    }
    
    public void loadCourseData() {
        try {
            ArrayList<CourseWithTeacher> coursesWithTeachers = courseDA.getCoursesWithTeachers();
            view.updateCourseTable(coursesWithTeachers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Error loading course data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleAddCourse() {
        AddCourseDialog dialog = new AddCourseDialog(parentFrame);
        dialog.setVisible(true);
        
        if (dialog.isCourseCreated()) {
            Course newCourse = dialog.getCourse();
            boolean success = courseDA.addCourse(newCourse);
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Course added successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadCourseData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to add course", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleEditCourse() {
        // Get the selected course from the table
        CourseWithTeacher selectedCourseWithTeacher = view.getSelectedCourse();
        
        if (selectedCourseWithTeacher == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a course to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the actual course object and teacher
        Course selectedCourse = selectedCourseWithTeacher.getCourse();
        Teacher assignedTeacher = selectedCourseWithTeacher.getTeacher();
        
        // Create and show the edit dialog with the selected course data and teacher
        EditCourseDialog dialog = new EditCourseDialog(parentFrame, selectedCourse, assignedTeacher);
        dialog.setVisible(true);
        
        // If the user completed the dialog and updated the course
        if (dialog.isCourseUpdated()) {
            // Get the updated course from the dialog
            Course updatedCourse = dialog.getUpdatedCourse();
            
            // Update the course in the database
            boolean success = courseDA.editCourse(updatedCourse);
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Course updated successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadCourseData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to update course", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDeleteCourse() {
    	// Get the selected course from the table
        CourseWithTeacher selectedCourseWithTeacher = view.getSelectedCourse();
        
        if (selectedCourseWithTeacher == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a course to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get the actual course object
        Course selectedCourse = selectedCourseWithTeacher.getCourse();
        
        // Show confirmation dialog
        int result = JOptionPane.showConfirmDialog(
            parentFrame,
            "Are you sure you want to delete the course " + selectedCourse.getCode() + 
            ": " + selectedCourse.getName() + "?\n\n" +
            "This action cannot be undone.",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        // If user confirmed deletion
        if (result == JOptionPane.YES_OPTION) {
            boolean success = courseDA.deleteCourse(selectedCourse);
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Course deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadCourseData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to delete course. It may be referenced by other records.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}