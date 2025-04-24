package view.admin.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.entities.Course;

public class AddCourseDialog extends JDialog {
    
    private JTextField codeField;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField maxCapacityField;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private boolean courseCreated = false;
    private Course newCourse = null;
    
    public AddCourseDialog(JFrame parent) {
        super(parent, "Add New Course", true);
        
        // Set up dialog properties
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        setupActionListeners();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Course Code
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Course Code:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        codeField = new JTextField(10);
        panel.add(codeField, gbc);
        
        // Course Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Course Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nameField = new JTextField(20);
        panel.add(nameField, gbc);
        
        // Description
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        panel.add(scrollPane, gbc);
        
        // Max Capacity
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Max Capacity:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        maxCapacityField = new JTextField(5);
        panel.add(maxCapacityField, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        
        panel.add(saveButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void setupActionListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()) {
                    saveNewCourse();
                }
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                courseCreated = false;
                dispose();
            }
        });
    }
    
    private boolean validateInput() {
        // Check if course code is empty
        if (codeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Course code cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            codeField.requestFocus();
            return false;
        }
        
        // Check if course name is empty
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Course name cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            nameField.requestFocus();
            return false;
        }
        
        // Check if max capacity is a valid number
        try {
            int capacity = Integer.parseInt(maxCapacityField.getText().trim());
            if (capacity <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Max capacity must be a positive number", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                maxCapacityField.requestFocus();
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Max capacity must be a valid number", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            maxCapacityField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void saveNewCourse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        int maxCapacity = Integer.parseInt(maxCapacityField.getText().trim());
        
        // Create new course with "active" status
        newCourse = new Course(code, name, description, maxCapacity, "active");
        courseCreated = true;
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Check if a course was created
     * @return true if a course was created, false otherwise
     */
    public boolean isCourseCreated() {
        return courseCreated;
    }
    
    /**
     * Get the created course
     * @return the created course, or null if no course was created
     */
    public Course getCourse() {
        return newCourse;
    }
    
    /**
     * Get the code field
     * @return the code field
     */
    public JTextField getCodeField() {
        return codeField;
    }
    
    /**
     * Get the name field
     * @return the name field
     */
    public JTextField getNameField() {
        return nameField;
    }
    
    /**
     * Get the description area
     * @return the description area
     */
    public JTextArea getDescriptionArea() {
        return descriptionArea;
    }
    
    /**
     * Get the max capacity field
     * @return the max capacity field
     */
    public JTextField getMaxCapacityField() {
        return maxCapacityField;
    }
}