package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.entities.CourseWithTeacher;
import model.table.CourseTableModel;

public class CourseManagementPanel extends JPanel {
    
    private JTable courseTable;
    private CourseTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    
    public CourseManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel title
        JLabel titleLabel = new JLabel("Course Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Initialize the table panel
        buildTablePanel();
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addButton = new JButton("Add Course");
        editButton = new JButton("Edit Course");
        deleteButton = new JButton("Delete Course");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Add components to panel
        add(titleLabel, BorderLayout.NORTH);
        //add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
    }
    
    private void buildTablePanel() {
        // Initialize the table model
        tableModel = new CourseTableModel();
        
        // Create the table with the model
        courseTable = new JTable(tableModel);
        
        // Set selection mode
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add the table to a scroll pane and add it to this panel
        JScrollPane scrollPane = new JScrollPane(courseTable);
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
    
    // Method to update table data with CourseWithTeacher objects
    public void updateCourseTable(ArrayList<CourseWithTeacher> courses) {
        tableModel.updateCourseList(courses);
    }
    
    // Get selected course
    public CourseWithTeacher getSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow >= 0) {
            return tableModel.getCourseAt(selectedRow);
        }
        return null;
    }
    
    // Get selected row index
    public int getSelectedRowIndex() {
        return courseTable.getSelectedRow();
    }
}