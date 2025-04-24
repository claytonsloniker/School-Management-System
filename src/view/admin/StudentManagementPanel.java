package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.entities.Student;
import model.table.StudentTableModel;

public class StudentManagementPanel extends JPanel {
    
    private JTable studentTable;
    private StudentTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    
    public StudentManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Initialize the table panel
        buildTablePanel();
        
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
        // The table is now added in buildTablePanel()
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Build the table panel with the student table
     */
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new StudentTableModel();
        
        // Create the table with the model
        studentTable = new JTable(tableModel);
        
        // Set selection mode
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(studentTable);
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
    public void updateStudentTable(ArrayList<Student> students) {
        tableModel.updateStudentList(students);
    }
    
    // Get selected student
    public Student getSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getStudentAt(selectedRow);
        }
        return null;
    }
    
    // Get selected row index
    public int getSelectedRowIndex() {
        return studentTable.getSelectedRow();
    }
}