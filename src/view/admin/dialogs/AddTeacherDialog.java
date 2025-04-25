package view.admin.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.UUID;
import model.entities.Teacher;
import util.security.PasswordUtil;

public class AddTeacherDialog extends JDialog {
    
    private JTextField teacherIDField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox generateIDCheckBox;
    private JCheckBox generatePasswordCheckBox;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private boolean teacherCreated = false;
    private Teacher newTeacher = null;
    private String generatedPassword = "";
    
    public AddTeacherDialog(JFrame parent) {
        super(parent, "Add New Teacher", true);
        
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
        
        // Teacher ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Teacher ID:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        teacherIDField = new JTextField(10);
        panel.add(teacherIDField, gbc);
        
        // Auto-generate ID checkbox
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        generateIDCheckBox = new JCheckBox("Auto-generate");
        generateIDCheckBox.setSelected(true);
        panel.add(generateIDCheckBox, gbc);
        
        // First Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("First Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        firstNameField = new JTextField(20);
        panel.add(firstNameField, gbc);
        
        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Last Name:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        lastNameField = new JTextField(20);
        panel.add(lastNameField, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        emailField = new JTextField(20);
        panel.add(emailField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);
        
        // Auto-generate password checkbox
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        generatePasswordCheckBox = new JCheckBox("Auto-generate");
        generatePasswordCheckBox.setSelected(true);
        panel.add(generatePasswordCheckBox, gbc);
        
        // Update fields based on auto-generate checkboxes
        updateFieldsState();
        
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
    
    private void updateFieldsState() {
        teacherIDField.setEditable(!generateIDCheckBox.isSelected());
        passwordField.setEditable(!generatePasswordCheckBox.isSelected());
        
        if (generateIDCheckBox.isSelected()) {
            // Generate a teacher ID (e.g., T followed by 5 digits)
            String generatedID = "T" + (10000 + (int)(Math.random() * 90000));
            teacherIDField.setText(generatedID);
        }
        
        if (generatePasswordCheckBox.isSelected()) {
            // Generate a random password (8 characters)
            generatedPassword = UUID.randomUUID().toString().substring(0, 8);
            passwordField.setText(generatedPassword);
        } else {
            generatedPassword = new String(passwordField.getPassword());
        }
    }
    
    private void setupActionListeners() {
        generateIDCheckBox.addActionListener(e -> updateFieldsState());
        generatePasswordCheckBox.addActionListener(e -> updateFieldsState());
        
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveNewTeacher();
            }
        });
        
        cancelButton.addActionListener(e -> {
            teacherCreated = false;
            dispose();
        });
    }
    
    private boolean validateInput() {
        // Check if teacher ID is empty
        if (teacherIDField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Teacher ID cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            teacherIDField.requestFocus();
            return false;
        }
        
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
        
        // Check if password is empty or too short
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Password cannot be empty", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        // Password validation (only if not auto-generated)
        if (!generatePasswordCheckBox.isSelected() && password.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "Password must be at least 8 characters long", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            passwordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        // Simple email validation using regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    
    private void saveNewTeacher() {
        String id = teacherIDField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Store the password for display purposes if auto-generated
        if (generatePasswordCheckBox.isSelected()) {
            generatedPassword = password;
        }
        
        // Create new teacher with "active" status
        // Hash the password before storing
        String hashedPassword = PasswordUtil.hashPassword(password);
        newTeacher = new Teacher(id, firstName, lastName, email, hashedPassword, "active");
        teacherCreated = true;
        
        // Display password info before closing
        if (generatePasswordCheckBox.isSelected()) {
            showPasswordInfoDialog(firstName, lastName, generatedPassword);
        }
        
        // Close the dialog
        dispose();
    }
    
    /**
     * Show an information dialog with the teacher's generated password
     * @param firstName Teacher's first name
     * @param lastName Teacher's last name
     * @param password The generated password
     */
    private void showPasswordInfoDialog(String firstName, String lastName, String password) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create a text area for the password info
        JTextArea infoArea = new JTextArea(6, 30);
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setText(
            "Teacher account created successfully!\n\n" +
            "Teacher: " + firstName + " " + lastName + "\n" +
            "Generated Password: " + password + "\n\n" +
            "Please provide this password to the teacher. They will be able to change it after logging in."
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
            "Account Created - Password Information", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Check if a teacher was created
     * @return true if a teacher was created, false otherwise
     */
    public boolean isTeacherCreated() {
        return teacherCreated;
    }
    
    /**
     * Get the created teacher
     * @return the created teacher, or null if no teacher was created
     */
    public Teacher getTeacher() {
        return newTeacher;
    }
    
    /**
     * Get the generated password
     * @return the generated password
     */
    public String getGeneratedPassword() {
        return generatedPassword;
    }
}