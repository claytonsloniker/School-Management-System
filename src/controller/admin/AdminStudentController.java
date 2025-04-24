package controller.admin;

import java.util.ArrayList;
import javax.swing.*;

import model.dao.StudentDA;
import model.entities.Student;
import view.admin.StudentManagementPanel;
import view.admin.dialogs.AddStudentDialog;
import view.admin.dialogs.EditStudentDialog;

public class AdminStudentController {
    
    private StudentManagementPanel view;
    private StudentDA studentDA;
    private JFrame parentFrame;
    
    public AdminStudentController(StudentManagementPanel view, JFrame parentFrame) {
        this.view = view;
        this.parentFrame = parentFrame;
        this.studentDA = new StudentDA();
        
        // Set up listeners
        setupListeners();
        
        // Load initial data
        loadStudentData();
    }
    
    private void setupListeners() {
        view.setAddButtonListener(e -> handleAddStudent());
        view.setEditButtonListener(e -> handleEditStudent());
        view.setDeleteButtonListener(e -> handleDeleteStudent());
    }
    
    public void loadStudentData() {
        try {
            ArrayList<Student> students = studentDA.getStudentList();
            view.updateStudentTable(students);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Error loading student data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleAddStudent() {
        AddStudentDialog dialog = new AddStudentDialog(parentFrame);
        dialog.setVisible(true);
        
        if (dialog.isStudentCreated()) {
            Student newStudent = dialog.getStudent();
            boolean success = studentDA.addStudent(newStudent);
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Student added successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to add student", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleEditStudent() {
        Student selectedStudent = view.getSelectedStudent();
        
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a student to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        EditStudentDialog dialog = new EditStudentDialog(parentFrame, selectedStudent);
        dialog.setVisible(true);
        
        if (dialog.isStudentUpdated()) {
            Student updatedStudent = dialog.getUpdatedStudent();
            boolean success = studentDA.updateStudent(updatedStudent);
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Student updated successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to update student", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDeleteStudent() {
        Student selectedStudent = view.getSelectedStudent();
        
        if (selectedStudent == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a student to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            parentFrame,
            "Are you sure you want to delete student " + selectedStudent.getFullName() + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = studentDA.deleteStudent(selectedStudent.getId());
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Student deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadStudentData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to delete student", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}