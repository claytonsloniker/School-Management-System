package controller.teacher;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import model.entities.Course;
import model.entities.Student;
import view.teacher.TeacherStudentsPanel;

public class TeacherStudentController {
    
    private TeacherStudentsPanel view;
    private TeacherController parentController;
    
    public TeacherStudentController(TeacherStudentsPanel view, TeacherController parentController) {
        this.view = view;
        this.parentController = parentController;
        
        // Set up listeners
        setupListeners();
    }
    
    private void setupListeners() {
        // Set up course selection listener
        view.setCourseSelectionListener(e -> {
            Course selectedCourse = view.getSelectedCourse();
            if (selectedCourse != null) {
                loadStudentsForCourse(selectedCourse);
            }
        });
        
        // Set up search and filter listeners
        view.setSearchListener(e -> handleSearch(e));
        view.setResetListener(e -> {
            Course selectedCourse = view.getSelectedCourse();
            if (selectedCourse != null) {
                loadStudentsForCourse(selectedCourse);
            }
            view.clearFilter();
        });
        
        // Set up send message button listener
        view.setSendMessageButtonListener(e -> handleSendMessage());
    }
    
    public void loadStudentsForCourse(Course course) {
        try {
            ArrayList<Student> students = parentController.getTeacherDA().getStudentsForCourse(course.getCode());
            view.updateStudentTable(students);
        } catch (Exception e) {
            parentController.showErrorMessage("Error loading students: " + e.getMessage());
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
    
    private void handleSendMessage() {
        Course selectedCourse = view.getSelectedCourse();
        Student selectedStudent = view.getSelectedStudent();
        
        if (selectedCourse == null) {
            parentController.showErrorMessage("Please select a course first");
            return;
        }
        
        if (selectedStudent == null) {
            parentController.showErrorMessage("Please select a student to message");
            return;
        }
        
        // Create and show a message dialog
        String subject = JOptionPane.showInputDialog(
            parentController.getTeacherView(), 
            "Enter message subject:", 
            "Send Message to " + selectedStudent.getFullName(), 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (subject != null && !subject.trim().isEmpty()) {
            String message = JOptionPane.showInputDialog(
                parentController.getTeacherView(), 
                "Enter your message:", 
                "Send Message to " + selectedStudent.getFullName(), 
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (message != null && !message.trim().isEmpty()) {
                boolean success = parentController.getMessageDA().sendMessage(
                    parentController.getTeacher().getId(),
                    selectedStudent.getId(),
                    selectedCourse.getCode(),
                    subject,
                    message
                );
                
                if (success) {
                    JOptionPane.showMessageDialog(
                        parentController.getTeacherView(), 
                        "Message sent successfully", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    parentController.showErrorMessage("Failed to send message");
                }
            }
        }
    }
}