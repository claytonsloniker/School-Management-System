package view.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import model.entities.Auth;

public class AdminView extends JFrame {
    
    private JPanel contentPanel;
    private JPanel navigationPanel;
    private Auth currentUser;
    
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

    
    public AdminView(Auth user) {
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
        
        // Create navigation buttons
        JButton studentsButton = createNavButton("Manage Students");
        JButton teachersButton = createNavButton("Manage Teachers");
        JButton coursesButton = createNavButton("Manage Courses");
        JButton logoutButton = createNavButton("Logout");
        
        // Add buttons to panel
        navigationPanel.add(studentsButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(teachersButton);
        navigationPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        navigationPanel.add(coursesButton);
        navigationPanel.add(Box.createVerticalGlue());
        navigationPanel.add(logoutButton);
        
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
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
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
    
    // Methods for controller to set listeners
    
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