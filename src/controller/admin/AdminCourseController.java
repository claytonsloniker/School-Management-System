package controller.admin;

import java.util.ArrayList;
import javax.swing.*;

import model.dao.CourseDA;
import model.dao.TeacherCourseDA;
import model.entities.Course;
import model.entities.CourseWithTeacher;
import model.entities.Teacher;
import view.admin.CourseManagementPanel;
import view.admin.dialogs.AddCourseDialog;
import view.admin.dialogs.EditCourseDialog;

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
        view.setAssignTeacherButtonListener(e -> handleAssignTeacher());
        view.setUnassignTeacherButtonListener(e -> handleUnassignTeacher());
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
        // Implementation for adding a course
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
        // Implementation for editing a course
        // Similar to handleAddCourse
    }
    
    private void handleDeleteCourse() {
        // Implementation for deleting a course
    }
    
    private void handleAssignTeacher() {
        // Implementation for assigning a teacher
    }
    
    private void handleUnassignTeacher() {
        // Implementation for unassigning a teacher
    }
}