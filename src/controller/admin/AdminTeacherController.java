package controller.admin;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

import model.dao.TeacherDA;
import model.entities.Teacher;
import view.admin.TeacherManagementPanel;
import view.admin.dialogs.AddTeacherDialog;
import view.admin.dialogs.EditTeacherDialog;

public class AdminTeacherController {
    
    private TeacherManagementPanel view;
    private TeacherDA teacherDA;
    private JFrame parentFrame;
    
    public AdminTeacherController(TeacherManagementPanel view, JFrame parentFrame) {
        this.view = view;
        this.parentFrame = parentFrame;
        this.teacherDA = new TeacherDA();
        
        // Set up listeners
        setupListeners();
        
        // Load initial data
        loadTeacherData();
    }
    
    private void setupListeners() {
        view.setAddButtonListener(e -> handleAddTeacher());
        view.setEditButtonListener(e -> handleEditTeacher());
        view.setDeleteButtonListener(e -> handleDeleteTeacher());
        
        view.setSearchListener(e -> handleSearch(e));
        view.setResetListener(e -> {
            loadTeacherData();
            view.clearFilter();
        });
    }
    
    public void loadTeacherData() {
        try {
            ArrayList<Teacher> teachers = teacherDA.getTeacherList();
            view.updateTeacherTable(teachers);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Error loading teacher data: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleAddTeacher() {
        AddTeacherDialog dialog = new AddTeacherDialog(parentFrame);
        dialog.setVisible(true);
        
        if (dialog.isTeacherCreated()) {
            Teacher newTeacher = dialog.getTeacher();
            boolean success = teacherDA.addTeacher(newTeacher);
            
            if (success) {
                loadTeacherData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to add teacher", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleEditTeacher() {
        Teacher selectedTeacher = view.getSelectedTeacher();
        
        if (selectedTeacher == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a teacher to edit", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        EditTeacherDialog dialog = new EditTeacherDialog(parentFrame, selectedTeacher);
        dialog.setVisible(true);
        
        if (dialog.isTeacherUpdated()) {
            Teacher updatedTeacher = dialog.getUpdatedTeacher();
            boolean success = teacherDA.updateTeacher(updatedTeacher);
            
            if (success) {
                loadTeacherData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to update teacher", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleDeleteTeacher() {
        Teacher selectedTeacher = view.getSelectedTeacher();
        
        if (selectedTeacher == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a teacher to delete", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(
            parentFrame,
            "Are you sure you want to delete teacher " + selectedTeacher.getFullName() + "?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            boolean success = teacherDA.deleteTeacher(selectedTeacher.getId());
            
            if (success) {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Teacher deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTeacherData();
            } else {
                JOptionPane.showMessageDialog(parentFrame, 
                    "Failed to delete teacher", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
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
}