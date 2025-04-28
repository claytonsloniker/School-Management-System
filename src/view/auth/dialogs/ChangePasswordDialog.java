package view.auth.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class ChangePasswordDialog extends JDialog {
    
    private JPasswordField currentPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean passwordChanged = false;
    private String newPassword;
    
    public ChangePasswordDialog(JFrame parent, boolean firstLogin) {
        super(parent, "Change Password", true);
        
        //dialog properties
        setSize(400, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        //create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // components
        JLabel instructionLabel;
        if (firstLogin) {
            instructionLabel = new JLabel("You must change your password before continuing:");
            setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Force user to change password
        } else {
            instructionLabel = new JLabel("Enter your current password and new password:");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }
        
        JLabel currentPasswordLabel = new JLabel("Current Password:");
        JLabel newPasswordLabel = new JLabel("New Password:");
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        
        currentPasswordField = new JPasswordField(20);
        newPasswordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(instructionLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(currentPasswordLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(currentPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(newPasswordLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(newPasswordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(confirmPasswordLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(confirmPasswordField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        
        if (!firstLogin) {
            buttonPanel.add(cancelButton);
        }
        
        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                passwordChanged = true;
                newPassword = new String(newPasswordField.getPassword());
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private boolean validateInput() {
        String currentPwd = new String(currentPasswordField.getPassword());
        String newPwd = new String(newPasswordField.getPassword());
        String confirmPwd = new String(confirmPasswordField.getPassword());
        
        if (currentPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your current password", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            currentPasswordField.requestFocus();
            return false;
        }
        
        if (newPwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a new password", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            newPasswordField.requestFocus();
            return false;
        }
        
        if (newPwd.length() < 8) {
            JOptionPane.showMessageDialog(this, 
                "New password must be at least 8 characters long", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            newPasswordField.requestFocus();
            return false;
        }
        
        if (newPwd.equals(currentPwd)) {
            JOptionPane.showMessageDialog(this, 
                "New password must be different from current password", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            newPasswordField.requestFocus();
            return false;
        }
        
        if (!newPwd.equals(confirmPwd)) {
            JOptionPane.showMessageDialog(this, 
                "New password and confirm password don't match", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public String getCurrentPassword() {
        return new String(currentPasswordField.getPassword());
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public boolean isPasswordChanged() {
        return passwordChanged;
    }
}
