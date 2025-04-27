package view.student;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import model.entities.Student;
import view.BaseView;

public class StudentView extends JFrame implements BaseView {
    
    private JPanel contentPanel;
    private JPanel navigationPanel;
    private Student currentUser;
    
    // Panels for different features
    private StudentCoursePanel coursesPanel;
    private StudentEnrollPanel enrollPanel;
    private StudentMessagesPanel messagesPanel;
    
    // Menu items
    private JMenuItem coursesMenuItem;
    private JMenuItem enrollMenuItem;
    private JMenuItem messagesMenuItem;
    private JMenuItem logoutMenuItem;
    
    // Action listeners
    private ActionListener logoutListener;
    
    private JLabel profilePictureLabel;
    private PropertyChangeSupport propertyChangeSupport;
    
    public StudentView(Student user) {
        this.currentUser = user;
        
        // Set up the frame
        setTitle("School Management System - Student Panel");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create the menu bar
        setupMenuBar();
        
        // Set up the main layout
        setLayout(new BorderLayout());
        
        // Create navigation panel (left side)
        setupNavigationPanel();
        
        // Create content panel (right side)
        contentPanel = new JPanel(new CardLayout());
        
        // Initialize feature panels
        coursesPanel = new StudentCoursePanel();
        enrollPanel = new StudentEnrollPanel();
        messagesPanel = new StudentMessagesPanel();
        
        // Add panels to content panel
        contentPanel.add(coursesPanel, "courses");
        contentPanel.add(enrollPanel, "enroll");
        contentPanel.add(messagesPanel, "messages");
        
        // Add panels to main frame
        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        
        // Show default panel
        showPanel("courses");
    }
    
    //getter methods for each panel
    public StudentCoursePanel getCoursesPanel() {
        return coursesPanel;
    }
    
    public StudentEnrollPanel getEnrollPanel() {
        return enrollPanel;
    }
    
    public StudentMessagesPanel getMessagesPanel() {
        return messagesPanel;
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // Create file menu
        JMenu fileMenu = new JMenu("File");
        logoutMenuItem = new JMenuItem("Logout");
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        fileMenu.add(logoutMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(exitMenuItem);
        
        // Create student menu
        JMenu studentMenu = new JMenu("Student");
        coursesMenuItem = new JMenuItem("My Courses");
        enrollMenuItem = new JMenuItem("Enroll in Courses");
        messagesMenuItem = new JMenuItem("Messages");
        studentMenu.add(coursesMenuItem);
        studentMenu.add(enrollMenuItem);
        studentMenu.add(messagesMenuItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        
        // Add welcome label to right side of menu bar
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(welcomeLabel);
        
        // Set the menu bar
        setJMenuBar(menuBar);
        
        // Add exit action
        exitMenuItem.addActionListener(e -> System.exit(0));
    }
    
    private void setupNavigationPanel() {
        navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        navigationPanel.setPreferredSize(new Dimension(200, getHeight()));
        navigationPanel.setBackground(new Color(240, 240, 240));
        
        // Create profile section
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.setBackground(new Color(240, 240, 240));
        
        // Configure profile picture
        profilePictureLabel = new JLabel();
        profilePictureLabel.setPreferredSize(new Dimension(80, 80));
        profilePictureLabel.setMaximumSize(new Dimension(80, 80));
        profilePictureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Make the profile picture clickable
        profilePictureLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profilePictureLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleProfilePictureClick();
            }
        });
        
        //wrapper panel to center the profile picture horizontally
        JPanel pictureWrapper = new JPanel();
        pictureWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
        pictureWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        pictureWrapper.setBackground(new Color(240, 240, 240));
        pictureWrapper.add(profilePictureLabel);
        
        // Add username label underneath profile picture
        JLabel usernameLabel = new JLabel(currentUser.getFullName());
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to profile panel with proper spacing
        profilePanel.add(pictureWrapper);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        profilePanel.add(usernameLabel);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Add separator line
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        profilePanel.add(separator);
        profilePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Add profile panel to navigation panel
        navigationPanel.add(profilePanel);
        
        // Create navigation buttons
        JButton coursesButton = createNavButton("My Courses");
        JButton enrollButton = createNavButton("Enroll in Courses");
        JButton messagesButton = createNavButton("Messages");
        
        //navigation buttons with proper spacing
        navigationPanel.add(coursesButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(enrollButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(messagesButton);
        
        // Add vertical glue to push logout to bottom
        navigationPanel.add(Box.createVerticalGlue());
        
        // Add logout button at the bottom
        JButton logoutButton = createNavButton("Logout");
        navigationPanel.add(logoutButton);
        
        // Configure initial profile picture display
        if (currentUser.getProfilePicture() != null && !currentUser.getProfilePicture().isEmpty()) {
            try {
                updateProfilePicture(currentUser.getProfilePicture());
            } catch (Exception e) {
                showUserInitials();
            }
        } else {
            showUserInitials();
        }
        
        // Add action listeners
        coursesButton.addActionListener(e -> showPanel("courses"));
        enrollButton.addActionListener(e -> showPanel("enroll"));
        messagesButton.addActionListener(e -> showPanel("messages"));
        logoutButton.addActionListener(e -> {
            if (logoutListener != null) {
                logoutListener.actionPerformed(e);
            }
        });
    }
    
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, button.getPreferredSize().height));
        return button;
    }
    
    /**
     * Show a specific panel in the content area
     * @param panelName Name of the panel to show
     */
    @Override
    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, panelName);
    }
    
    /**
     * Update the profile picture display
     * @param profilePicturePath Path to the profile picture
     */
    @Override
    public void updateProfilePicture(String profilePicturePath) {
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            try {
                File imageFile = new File(profilePicturePath);
                if (!imageFile.exists() && !profilePicturePath.contains(":")) {
                    imageFile = new File(System.getProperty("user.dir"), profilePicturePath);
                }
                
                if (!imageFile.exists()) {
                    System.out.println("Image file does not exist: " + imageFile.getAbsolutePath());
                    showUserInitials();
                    return;
                }
                
                BufferedImage originalImage = ImageIO.read(imageFile);
                if (originalImage == null) {
                    System.out.println("Failed to read image: " + imageFile.getAbsolutePath());
                    showUserInitials();
                    return;
                }
                
                BufferedImage circleBuffer = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new Ellipse2D.Float(0, 0, 80, 80));
                
                Image scaledImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                g2.drawImage(scaledImage, 0, 0, null);
                g2.dispose();
                
                profilePictureLabel.setIcon(new ImageIcon(circleBuffer));
            } catch (Exception e) {
                System.out.println("Error updating profile picture: " + profilePicturePath);
                e.printStackTrace();
                showUserInitials();
            }
        } else {
            showUserInitials();
        }
    }
    
    /**
     * Display user initials in a circle as the profile picture
     */
    private void showUserInitials() {
        // Create circular label with user's initials
        String initials = String.valueOf(currentUser.getFirstName().charAt(0)) + 
                         String.valueOf(currentUser.getLastName().charAt(0));
        
        BufferedImage circleBuffer = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circleBuffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(70, 130, 180));
        g2d.fillOval(0, 0, 80, 80);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (80 - fm.stringWidth(initials)) / 2;
        int textY = ((80 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initials, textX, textY);
        g2d.dispose();
        
        profilePictureLabel.setIcon(new ImageIcon(circleBuffer));
    }
    
    /**
     * Handle click on profile picture to show upload/remove options
     */
    private void handleProfilePictureClick() {
        // Show options when profile picture is clicked
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem uploadItem = new JMenuItem("Upload Profile Picture");
        JMenuItem removeItem = new JMenuItem("Remove Profile Picture");
        
        uploadItem.addActionListener(e -> uploadProfilePicture());
        removeItem.addActionListener(e -> removeProfilePicture());
        
        popupMenu.add(uploadItem);
        
        // Add remove option if there's a profile picture
        if (currentUser.getProfilePicture() != null && !currentUser.getProfilePicture().isEmpty()) {
            popupMenu.add(removeItem);
        }
        
        popupMenu.show(profilePictureLabel, 0, profilePictureLabel.getHeight());
    }
    
    /**
     * Upload a new profile picture
     */
    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        
        //File filter to only allow images
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // Max 5MB
            long fileSize = selectedFile.length();
            long maxSize = 5 * 1024 * 1024; // 5MB
            
            if (fileSize > maxSize) {
                JOptionPane.showMessageDialog(this, 
                    "File is too large. Maximum size is 5MB.", 
                    "File Size Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create directory for profile pictures if it doesn't exist
            File uploadDir = new File("profile_pictures");
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            try {
                // Generate unique filename
                String extension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
                String newFileName = "profile_student_" + currentUser.getId() + "_" + System.currentTimeMillis() + extension;
                File destFile = new File(uploadDir, newFileName);
                
                // Copy file
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // Request controller to update profile picture in database
                triggerPropertyChange("profilePictureUpdated", null, destFile.getPath());
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error uploading profile picture: " + e.getMessage(), 
                    "Upload Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Remove the profile picture
     */
    private void removeProfilePicture() {
        // Confirm removal
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to remove your profile picture?", 
            "Confirm Removal", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Request controller to remove profile picture
            triggerPropertyChange("profilePictureRemoved", null, currentUser.getProfilePicture());
        }
    }
    
    /**
     * Add a property change listener
     * @param listener The property change listener
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    /**
     * Remove a property change listener
     * @param listener The property change listener
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    
    /**
     * Trigger a property change event
     * @param propertyName Name of the property that changed
     * @param oldValue Old value
     * @param newValue New value
     */
    private void triggerPropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    /**
     * Get the frame
     * @return The frame
     */
    @Override
    public JFrame getFrame() {
        return this;
    }
    
    // Menu item listeners
    /**
     * Set the logout listener
     * @param listener The action listener
     */
    @Override
    public void setLogoutListener(ActionListener listener) {
        this.logoutListener = listener;
        logoutMenuItem.addActionListener(listener);
    }
    
    /**
     * Set the courses menu item listener
     * @param listener The action listener
     */
    public void setCoursesMenuItemListener(ActionListener listener) {
        coursesMenuItem.addActionListener(listener);
    }
    
    /**
     * Set the enroll menu item listener
     * @param listener The action listener
     */
    public void setEnrollMenuItemListener(ActionListener listener) {
        enrollMenuItem.addActionListener(listener);
    }
    
    /**
     * Set the messages menu item listener
     * @param listener The action listener
     */
    public void setMessagesMenuItemListener(ActionListener listener) {
        messagesMenuItem.addActionListener(listener);
    }
}