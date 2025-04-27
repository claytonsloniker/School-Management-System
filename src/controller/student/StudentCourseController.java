package controller.student;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

import model.entities.Course;
import view.student.StudentCoursePanel;

public class StudentCourseController {
    
    private StudentCoursePanel view;
    private StudentController parentController;
    
    public StudentCourseController(StudentCoursePanel view, StudentController parentController) {
        this.view = view;
        this.parentController = parentController;
        
        // Set up listeners
        setupListeners();
    }
    
    private void setupListeners() {
        // Set up button listeners
        view.setContactTeacherButtonListener(e -> handleContactTeacher());
        
        // Set up search and filter listeners
        view.setSearchListener(e -> handleSearch(e));
        view.setResetListener(e -> {
            updateCourseTable(
                parentController.getStudentDA().getCoursesForStudent(
                    parentController.getStudent().getId()
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
            
            view.applyFilter(searchText, filterCriteria);
        }
    }
    
    private void handleContactTeacher() {
        Course selectedCourse = view.getSelectedCourse();
        
        if (selectedCourse == null) {
            parentController.showErrorMessage("Please select a course first");
            return;
        }
        
        // Navigate to messages panel
        parentController.getStudentView().showPanel("messages");
        
        //set the course in the messages panel
        ArrayList<Course> selectedCourseList = new ArrayList<>();
        selectedCourseList.add(selectedCourse);
        parentController.getStudentView().getMessagesPanel().updateCourseSelector(selectedCourseList);
        
        //Load teachers for the selected course
        parentController.getMessageController().loadTeachersForCourse(selectedCourse);
    }
}