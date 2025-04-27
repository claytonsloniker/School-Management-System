package controller.teacher;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entities.Course;
import view.teacher.TeacherCoursesPanel;

public class TeacherCourseController {
    
    private TeacherCoursesPanel view;
    private TeacherController parentController;
    
    public TeacherCourseController(TeacherCoursesPanel view, TeacherController parentController) {
        this.view = view;
        this.parentController = parentController;
        
        // Set up listeners
        setupListeners();
    }
    
    private void setupListeners() {
        // Set up search and filter listeners
        view.setSearchListener(e -> handleSearch(e));
        view.setResetListener(e -> {
            updateCourseTable(
                parentController.getTeacherDA().getCoursesForTeacher(
                    parentController.getTeacher().getId()
                )
            );
            view.clearFilter();
        });
    }
    
    public void updateCourseTable(ArrayList<Course> courses) {
        view.updateCourseTable(courses);
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
}