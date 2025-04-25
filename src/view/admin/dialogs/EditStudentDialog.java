package view.admin.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import model.entities.Student;
import util.security.PasswordUtil;

public class EditStudentDialog extends JDialog {
    
    private JTextField studentIDField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox changePasswordCheckBox;
    private JComboBox<String> statusComboBox;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private boolean studentUpdated = false;
    private Student updatedStudent = null;
    private Student originalStudent;
    
    public EditStudentDialog(JFrame parent, Student student) {
        super(parent, "Edit Student", true);
        this.originalStudent = student;
        
        // Set up dialog properties
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Populate fields with student data
        populateFields();
        
        // Add action listeners
        setupActionListeners();
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Student ID (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Student ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        studentIDField = new JTextField(10);
        studentIDField.setEditable(false); // Make ID field read-only
        panel.add(studentIDField, gbc);
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        firstNameField = new JTextField(20);
        panel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);
        
        // Change Password checkbox
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        changePasswordCheckBox = new JCheckBox("Change Password");
        panel.add(changePasswordCheckBox, gbc);
        
        // Password field
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        passwordField.setEnabled(false); // Disabled by default until checkbox is checked
        panel.add(passwordField, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Status:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] statuses = {"active", "inactive"};
        statusComboBox = new JComboBox<>(statuses);
        panel.add(statusComboBox, gbc);
        
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
        studentIDField.setText(originalStudent.getId());
        firstNameField.setText(originalStudent.getFirstName());
        lastNameField.setText(originalStudent.getLastName());
        emailField.setText(originalStudent.getEmail());
        statusComboBox.setSelectedItem(originalStudent.getStatus());
        
        // Don't populate the password field with the hashed password
        passwordField.setText("");
    }
    
    private void setupActionListeners() {
        changePasswordCheckBox.addActionListener(e -> {
            passwordField.setEnabled(changePasswordCheckBox.isSelected());
            if (changePasswordCheckBox.isSelected()) {
                passwordField.requestFocus();
            }
        });
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveUpdatedStudent();
            }
        });
        
        cancelButton.addActionListener(e -> {
            studentUpdated = false;
            dispose();
        });
    }
    
    private boolean validateInput() {
        // Check if first name is empty
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "First name cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }
        
        // Check if last name is empty
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Last name cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }
        
        // Check if email is empty or invalid
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        // Validate email format
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            emailField.requestFocus();
            return false;
        }
        
        // Check if password is valid when changing it
        if (changePasswordCheckBox.isSelected()) {
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Password cannot be empty when changing password", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.requestFocus();
                return false;
            }
            
            if (password.length() < 8) {
                JOptionPane.showMessageDialog(this, 
                    "Password must be at least 8 characters long", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                passwordField.requestFocus();
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    private void saveUpdatedStudent() {
        String id = studentIDField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        
        // Create updated student with either the new password or the original one
        String password;
        if (changePasswordCheckBox.isSelected()) {
            String newPassword = new String(passwordField.getPassword());
            password = PasswordUtil.hashPassword(newPassword);
            
            // Show dialog with the new password
            showPasswordUpdatedDialog(firstName, lastName, newPassword);
        } else {
            password = originalStudent.getPassword(); // Keep the original hashed password
        }
        
        // Create updated student
        updatedStudent = new Student(id, firstName, lastName, email, password, status);
        studentUpdated = true;
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Show an information dialog with the student's new password
     * @param firstName Student's first name
     * @param lastName Student's last name
     * @param password The new password
     */
    private void showPasswordUpdatedDialog(String firstName, String lastName, String password) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a text area for the password info
        JTextArea infoArea = new JTextArea(6, 30);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setText(
            "Password updated for:\n\n" +
            "Student: " + firstName + " " + lastName + "\n" +
            "New Password: " + password + "\n\n" +
            "Please provide this password to the student."
        );
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add a copy to clipboard button
        JButton copyButton = new JButton("Copy Password to Clipboard");
        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(password);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
            JOptionPane.showMessageDialog(panel, 
                "Password copied to clipboard", 
                "Copied", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(copyButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show the dialog
        JOptionPane.showMessageDialog(
            this, 
            panel, 
            "Password Updated", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Check if a student was updated
     * @return true if a student was updated, false otherwise
     */
    public boolean isStudentUpdated() {
        return studentUpdated;
    }
    
    /**
     * Get the updated student
     * @return the updated student, or null if no student was updated
     */
    public Student getUpdatedStudent() {
        return updatedStudent;
    }
}