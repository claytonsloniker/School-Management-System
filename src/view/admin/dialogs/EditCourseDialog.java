package view.admin.dialogs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.entities.Course;
import model.entities.Teacher;
import model.dao.TeacherCourseDA;

public class EditCourseDialog extends JDialog {
    
    private JTextField codeField;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField maxCapacityField;
    private JComboBox<String> statusComboBox;
    
    // Teacher assignment UI components
    private JLabel currentTeacherLabel;
    private JButton assignTeacherButton;
    private JButton unassignTeacherButton;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private boolean courseUpdated = false;
    private Course updatedCourse = null;
    private Course originalCourse;
    private Teacher assignedTeacher;
    
    private TeacherCourseDA teacherCourseDA;
    
    public EditCourseDialog(JFrame parent, Course course, Teacher teacher) {
        super(parent, "Edit Course", true);
        this.originalCourse = course;
        this.assignedTeacher = teacher;
        this.teacherCourseDA = new TeacherCourseDA();
        
        // Set up dialog properties
        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate fields with course data
        populateFields();
        
        // Update teacher assignment display
        updateTeacherDisplay();
        
        // Add action listeners
        setupActionListeners();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Course Code (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Course Code:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        codeField = new JTextField(10);
        codeField.setEditable(false); // Make code field read-only
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
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] statuses = {"active", "inactive"};
        statusComboBox = new JComboBox<>(statuses);
        panel.add(statusComboBox, gbc);
        
        // Teacher Assignment Section
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        
        gbc.gridy = 6;
        JLabel teacherSectionLabel = new JLabel("Teacher Assignment");
        teacherSectionLabel.setFont(new Font(teacherSectionLabel.getFont().getName(), Font.BOLD, 14));
        panel.add(teacherSectionLabel, gbc);
        
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Assigned Teacher:"), gbc);
        
        gbc.gridx = 1;
        currentTeacherLabel = new JLabel("Not Assigned");
        currentTeacherLabel.setFont(new Font(currentTeacherLabel.getFont().getName(), Font.BOLD, 12));
        panel.add(currentTeacherLabel, gbc);
        
        // Teacher assignment buttons
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JPanel teacherButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignTeacherButton = new JButton("Assign Teacher");
        unassignTeacherButton = new JButton("Unassign Teacher");
        teacherButtonPanel.add(assignTeacherButton);
        teacherButtonPanel.add(unassignTeacherButton);
        panel.add(teacherButtonPanel, gbc);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        saveButton = new JButton("Save Changes");
        cancelButton = new JButton("Cancel");
        
        panel.add(saveButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void populateFields() {
        codeField.setText(originalCourse.getCode());
        nameField.setText(originalCourse.getName());
        descriptionArea.setText(originalCourse.getDescription());
        maxCapacityField.setText(String.valueOf(originalCourse.getMax_capacity()));
        statusComboBox.setSelectedItem(originalCourse.getStatus());
    }
    
    private void updateTeacherDisplay() {
        if (assignedTeacher != null) {
            currentTeacherLabel.setText(assignedTeacher.getFullName());
            unassignTeacherButton.setEnabled(true);
        } else {
            currentTeacherLabel.setText("Not Assigned");
            unassignTeacherButton.setEnabled(false);
        }
    }
    
    private void setupActionListeners() {
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveUpdatedCourse();
            }
        });
        
        cancelButton.addActionListener(e -> {
            courseUpdated = false;
            dispose();
        });
        
        assignTeacherButton.addActionListener(e -> {
            handleAssignTeacher();
        });
        
        unassignTeacherButton.addActionListener(e -> {
            handleUnassignTeacher();
        });
    }
    
    private void handleAssignTeacher() {
        // Get all teachers from the database
        ArrayList<Teacher> teachers = teacherCourseDA.getAllTeachers();
        
        if (teachers.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No teachers available to assign.", 
                "No Teachers", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create a combo box with all teachers
        JComboBox<Teacher> teacherComboBox = new JComboBox<>();
        for (Teacher teacher : teachers) {
            teacherComboBox.addItem(teacher);
        }
        
        // Show dialog
        int result = JOptionPane.showConfirmDialog(this, 
            teacherComboBox, 
            "Select Teacher", 
            JOptionPane.OK_CANCEL_OPTION);
        
        if (result == JOptionPane.OK_OPTION) {
            Teacher selectedTeacher = (Teacher) teacherComboBox.getSelectedItem();
            
            // If a teacher is already assigned, unassign first
            if (assignedTeacher != null) {
                boolean unassignSuccess = teacherCourseDA.unassignTeacherFromCourse(
                    assignedTeacher.getId(), originalCourse.getCode());
                
                if (!unassignSuccess) {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to unassign current teacher.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // Assign the new teacher
            boolean assignSuccess = teacherCourseDA.assignTeacherToCourse(
                selectedTeacher.getId(), originalCourse.getCode());
            
            if (assignSuccess) {
                assignedTeacher = selectedTeacher;
                updateTeacherDisplay();
                JOptionPane.showMessageDialog(this, 
                    "Teacher assigned successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to assign teacher.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void handleUnassignTeacher() {
        if (assignedTeacher == null) {
            JOptionPane.showMessageDialog(this, 
                "No teacher is currently assigned to this course.", 
                "No Assignment", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to unassign " + assignedTeacher.getFullName() + 
            " from this course?", 
            "Confirm Unassignment", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = teacherCourseDA.unassignTeacherFromCourse(
                assignedTeacher.getId(), originalCourse.getCode());
            
            if (success) {
                assignedTeacher = null;
                updateTeacherDisplay();
                JOptionPane.showMessageDialog(this, 
                    "Teacher unassigned successfully.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to unassign teacher.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private boolean validateInput() {
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
    
    private void saveUpdatedCourse() {
        String code = codeField.getText().trim();
        String name = nameField.getText().trim();
        String description = descriptionArea.getText().trim();
        int maxCapacity = Integer.parseInt(maxCapacityField.getText().trim());
        String status = (String) statusComboBox.getSelectedItem();
        
        // Create updated course
        updatedCourse = new Course(code, name, description, maxCapacity, status);
        courseUpdated = true;
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Check if a course was updated
     * @return true if a course was updated, false otherwise
     */
    public boolean isCourseUpdated() {
        return courseUpdated;
    }
    
    /**
     * Get the updated course
     * @return the updated course, or null if no course was updated
     */
    public Course getUpdatedCourse() {
        return updatedCourse;
    }
    
    /**
     * Get the assigned teacher
     * @return the assigned teacher, or null if no teacher is assigned
     */
    public Teacher getAssignedTeacher() {
        return assignedTeacher;
    }
}