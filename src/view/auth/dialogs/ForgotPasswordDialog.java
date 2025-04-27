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
import javax.swing.JTextField;

public class ForgotPasswordDialog extends JDialog {
    
    private JTextField emailField;
    private JButton submitButton;
    private JButton cancelButton;
    private boolean recoveryRequested = false;
    
    public ForgotPasswordDialog(JFrame parent) {
        super(parent, "Password Recovery", true);
        
        // Set up dialog properties
        setSize(400, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        // Create main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Create components
        JLabel instructionLabel = new JLabel("Enter your email address to receive a temporary password:");
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        
        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(instructionLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Add action listeners
        submitButton.addActionListener(e -> {
            if (validateEmail()) {
                recoveryRequested = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }
    
    private boolean validateEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter your email address", 
                "Email Required", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Simple validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!email.matches(emailRegex)) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a valid email address", 
                "Invalid Email", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    public boolean isRecoveryRequested() {
        return recoveryRequested;
    }
    
    public String getEmail() {
        return emailField.getText().trim();
    }
}