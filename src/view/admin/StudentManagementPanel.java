package view.admin;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.entities.Student;

class StudentManagementPanel extends JPanel {
    
    private JTable studentTable;
    private JButton addButton, editButton, deleteButton;
    
    public StudentManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Create table
        String[] columnNames = {"ID", "First Name", "Last Name", "Email", "Status"};
        Object[][] data = {}; // This would be populated from your database
        
        studentTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Student");
        editButton = new JButton("Edit Student");
        deleteButton = new JButton("Delete Student");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Methods to set listeners and update table data
    public void setAddButtonListener(java.awt.event.ActionListener listener) {
        addButton.addActionListener(listener);
    }
    
    public void setEditButtonListener(java.awt.event.ActionListener listener) {
        editButton.addActionListener(listener);
    }
    
    public void setDeleteButtonListener(java.awt.event.ActionListener listener) {
        deleteButton.addActionListener(listener);
    }
    
    public void updateStudentTable(ArrayList<Student> students) {
        // Method to update the table with new data
        // Would create a new table model and set it to the table
    }
}