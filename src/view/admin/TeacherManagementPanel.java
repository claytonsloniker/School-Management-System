package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.entities.Teacher;
import model.table.TeacherTableModel;

public class TeacherManagementPanel extends JPanel {
    
    private JTable teacherTable;
    private TeacherTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    
    public TeacherManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Teacher Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Initialize the table panel
        buildTablePanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Teacher");
        editButton = new JButton("Edit Teacher");
        deleteButton = new JButton("Delete Teacher");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        // The table is now added in buildTablePanel()
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Build the table panel with the teacher table
     */
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new TeacherTableModel();
        
        // Create the table with the model
        teacherTable = new JTable(tableModel);
        
        // Set selection mode
        teacherTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    // Methods to set listeners
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void setEditButtonListener(ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    // Method to update table data
    public void updateTeacherTable(ArrayList<Teacher> teachers) {
        tableModel.updateTeacherList(teachers);
    }
    
    // Get selected teacher
    public Teacher getSelectedTeacher() {
        int selectedRow = teacherTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getTeacherAt(selectedRow);
        }
        return null;
    }
    
    // Get selected row index
    public int getSelectedRowIndex() {
        return teacherTable.getSelectedRow();
    }
}