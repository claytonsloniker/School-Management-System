package view.admin;

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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import model.entities.Admin;
import model.entities.Auth;

public class AdminView extends JFrame {
    
    private JPanel contentPanel;
    private JPanel navigationPanel;
    private Admin currentUser;
    
    // Panels for different features
    private StudentManagementPanel studentPanel;
    private TeacherManagementPanel teacherPanel;
    private CourseManagementPanel coursePanel;
    
    // Menu items
    private JMenuItem studentMenuItem;
    private JMenuItem teacherMenuItem;
    private JMenuItem courseMenuItem;
    private JMenuItem logoutMenuItem;
    
    //action listeners
    private ActionListener logoutListener;
    
    private JLabel profilePictureLabel;
    private PropertyChangeSupport propertyChangeSupport;

    
    public AdminView(Admin user) {
        this.currentUser = user;
        
        // Set up the frame
        setTitle("School Management System - Admin Panel");
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
        studentPanel = new StudentManagementPanel();
        teacherPanel = new TeacherManagementPanel();
        coursePanel = new CourseManagementPanel();
        
        // Add panels to content panel
        contentPanel.add(studentPanel, "students");
        contentPanel.add(teacherPanel, "teachers");
        contentPanel.add(coursePanel, "courses");
        
        // Add panels to main frame
        add(navigationPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        
        // Show default panel
        showPanel("students");
    }
    
    // Add getter methods for each panel
    public StudentManagementPanel getStudentPanel() {
        return studentPanel;
    }
    
    public TeacherManagementPanel getTeacherPanel() {
        return teacherPanel;
    }
    
    public CourseManagementPanel getCoursePanel() {
        return coursePanel;
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
        
        // Create management menu
        JMenu managementMenu = new JMenu("Management");
        studentMenuItem = new JMenuItem("Students");
        teacherMenuItem = new JMenuItem("Teachers");
        courseMenuItem = new JMenuItem("Courses");
        managementMenu.add(studentMenuItem);
        managementMenu.add(teacherMenuItem);
        managementMenu.add(courseMenuItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(managementMenu);
        
        // Add welcome label to right side of menu bar
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFirstName() + " " + currentUser.getLastName());
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(welcomeLabel);
        
        // Set the menu bar
        setJMenuBar(menuBar);
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
        
        // Create a wrapper panel to center the profile picture horizontally
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
        
        // Create management buttons
        JButton studentsButton = createNavButton("Manage Students");
        JButton teachersButton = createNavButton("Manage Teachers");
        JButton coursesButton = createNavButton("Manage Courses");
        
        // Add management buttons with proper spacing
        navigationPanel.add(studentsButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(teachersButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(coursesButton);
        
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
        studentsButton.addActionListener(e -> showPanel("students"));
        teachersButton.addActionListener(e -> showPanel("teachers"));
        coursesButton.addActionListener(e -> showPanel("courses"));
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
    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, panelName);
    }
    
    public void updateProfilePicture(String profilePicturePath) {
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            try {
                // Handle relative paths by converting to absolute paths if needed
                File imageFile = new File(profilePicturePath);
                if (!imageFile.exists() && !profilePicturePath.contains(":")) {
                    // If the file doesn't exist and the path is relative, try to resolve it
                    // relative to the app working directory
                    imageFile = new File(System.getProperty("user.dir"), profilePicturePath);
                }
                
                if (!imageFile.exists()) {
                    System.out.println("Image file does not exist: " + imageFile.getAbsolutePath());
                    showUserInitials();
                    return;
                }
                
                // load and scale the image
                BufferedImage originalImage = ImageIO.read(imageFile);
                if (originalImage == null) {
                    System.out.println("Failed to read image: " + imageFile.getAbsolutePath());
                    showUserInitials();
                    return;
                }
                
                //Create a circular mask
                BufferedImage circleBuffer = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circleBuffer.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                

                g2.setClip(new Ellipse2D.Float(0, 0, 80, 80));
                
                // Scale the image to fit
                Image scaledImage = originalImage.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                g2.drawImage(scaledImage, 0, 0, null);
                g2.dispose();
                
                // Set the image as icon
                profilePictureLabel.setIcon(new ImageIcon(circleBuffer));
                System.out.println("Successfully updated profile picture: " + imageFile.getAbsolutePath());
            } catch (Exception e) {
                // Print the full exception to help with debugging
                System.out.println("Error updating profile picture: " + profilePicturePath);
                e.printStackTrace();
                showUserInitials();
            }
        } else {
            showUserInitials();
        }
    }
    
    private ImageIcon createCircularImage(Image image, int width, int height) {
        BufferedImage circleBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = circleBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw circle background
        g2.setColor(new Color(240, 240, 240));
        g2.fillOval(0, 0, width, height);
        
        // Create circular mask
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, width, height, null);
        g2.dispose();
        
        return new ImageIcon(circleBuffer);
    }
    
    private void showUserInitials() {
        // Create a circular label with user's initials
        String initials = String.valueOf(currentUser.getFirstName().charAt(0)) + 
                         String.valueOf(currentUser.getLastName().charAt(0));
        
        // Create a circular background (no border)
        BufferedImage circleBuffer = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circleBuffer.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(70, 130, 180)); // Steel blue background
        g2d.fillOval(0, 0, 80, 80);
        
        // Add text (initials)
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (80 - fm.stringWidth(initials)) / 2;
        int textY = ((80 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initials, textX, textY);
        g2d.dispose();
        
        profilePictureLabel.setIcon(new ImageIcon(circleBuffer));
    }
    
    private void handleProfilePictureClick() {
        // Show options when profile picture is clicked
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem uploadItem = new JMenuItem("Upload Profile Picture");
        JMenuItem removeItem = new JMenuItem("Remove Profile Picture");
        
        uploadItem.addActionListener(e -> uploadProfilePicture());
        removeItem.addActionListener(e -> removeProfilePicture());
        
        popupMenu.add(uploadItem);
        
        //add remove option if there's a profile picture
        if (currentUser.getProfilePicture() != null && !currentUser.getProfilePicture().isEmpty()) {
            popupMenu.add(removeItem);
        }
        
        popupMenu.show(profilePictureLabel, 0, profilePictureLabel.getHeight());
    }
    
    private void uploadProfilePicture() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Profile Picture");
        
        //file filter to only allow images
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
            // max 5MB
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
                String newFileName = "profile_admin_" + currentUser.getId() + "_" + System.currentTimeMillis() + extension;
                File destFile = new File(uploadDir, newFileName);
                
                // Copy file
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // request controller to update profile picture in database
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
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    // helper method to simplify firing events
    private void triggerPropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
    
    //listeners
    public void setLogoutListener(ActionListener listener) {
        this.logoutListener = listener;
        logoutMenuItem.addActionListener(listener);
    }
    
    public void setStudentMenuItemListener(java.awt.event.ActionListener listener) {
        studentMenuItem.addActionListener(listener);
    }
    
    public void setTeacherMenuItemListener(java.awt.event.ActionListener listener) {
        teacherMenuItem.addActionListener(listener);
    }
    
    public void setCourseMenuItemListener(java.awt.event.ActionListener listener) {
        courseMenuItem.addActionListener(listener);
    }
}