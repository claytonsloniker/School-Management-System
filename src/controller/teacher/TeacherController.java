package controller.teacher;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import controller.BaseController;
import model.dao.CourseDA;
import model.dao.MessageDA;
import model.dao.TeacherDA;
import model.entities.Course;
import model.entities.Teacher;
import view.teacher.TeacherView;

public class TeacherController extends BaseController {
    
    private TeacherView teacherView;
    private TeacherDA teacherDA;
    private CourseDA courseDA;
    private MessageDA messageDA;
    
    // Controllers for specific features
    private TeacherCourseController courseController;
    private TeacherStudentController studentController;
    private TeacherMessageController messageController;
    
    public TeacherController(Teacher teacher) {
        // Pass the teacher and view to super
        super(teacher, new TeacherView(teacher));
        
        // Cast the view for easier access
        this.teacherView = (TeacherView) view;
        
        // Initialize data access objects
        this.teacherDA = new TeacherDA();
        this.courseDA = new CourseDA();
        this.messageDA = new MessageDA();
        
        // Initialize feature controllers
        this.courseController = new TeacherCourseController(teacherView.getCoursesPanel(), this);
        this.studentController = new TeacherStudentController(teacherView.getStudentsPanel(), this);
        this.messageController = new TeacherMessageController(teacherView.getMessagesPanel(), this);
        
        // Set up menu listeners
        setupMenuListeners();
        
        // Initialize the teacher data
        initialize();
        
        // Display the view
        this.view.setVisible(true);
    }
    
    private void setupMenuListeners() {
        teacherView.setCoursesMenuItemListener(e -> teacherView.showPanel("courses"));
        teacherView.setStudentsMenuItemListener(e -> teacherView.showPanel("students"));
        teacherView.setMessagesMenuItemListener(e -> teacherView.showPanel("messages"));
    }
    
    @Override
    protected void setupSpecificListeners() {
    	TeacherView teacherView = (TeacherView) view;
        
        // Set up cross-panel navigation listeners
        teacherView.getCoursesPanel().setViewStudentsButtonListener(e -> {
            Course selectedCourse = teacherView.getCoursesPanel().getSelectedCourse();
            if (selectedCourse != null) {
                teacherView.showPanel("students");
                teacherView.getStudentsPanel().updateCourseSelector(
                    new ArrayList<>(java.util.List.of(selectedCourse)));
                studentController.loadStudentsForCourse(selectedCourse);
            } else {
                showErrorMessage("Please select a course first");
            }
        });
        
        teacherView.getCoursesPanel().setViewMessagesButtonListener(e -> {
            Course selectedCourse = teacherView.getCoursesPanel().getSelectedCourse();
            if (selectedCourse != null) {
                teacherView.showPanel("messages");
                teacherView.getMessagesPanel().updateCourseSelector(
                    new ArrayList<>(java.util.List.of(selectedCourse)));
                messageController.loadMessagesForCourse(selectedCourse);
            } else {
                showErrorMessage("Please select a course first");
            }
        });
    }
    
    @Override
    protected void loadInitialData() {
        loadTeacherCourses();
    }
    
    private void loadTeacherCourses() {
        try {
            ArrayList<Course> courses = teacherDA.getCoursesForTeacher(currentUser.getId());
            
            // Update the course panel table
            courseController.updateCourseTable(courses);
            
            // Also update the course selectors in other panels
            teacherView.getStudentsPanel().updateCourseSelector(courses);
            teacherView.getMessagesPanel().updateCourseSelector(courses);
        } catch (Exception e) {
            showErrorMessage("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void initialize() {
        // Call parent initialize
        super.initialize();
        
        // Add property change listener to the view
        teacherView.addPropertyChangeListener(this::handlePropertyChange);
    }

    protected void handlePropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        
        switch (propertyName) {
            case "profilePictureUpdated":
                String newProfilePicturePath = (String) evt.getNewValue();
                handleProfilePictureUpdate(newProfilePicturePath);
                break;
                
            case "profilePictureRemoved":
                handleProfilePictureRemoval();
                break;
        }
    }

    protected void handleProfilePictureUpdate(String profilePicturePath) {
        boolean success = teacherDA.updateTeacherProfilePicture(currentUser.getId(), profilePicturePath);
        
        if (success) {
            currentUser.setProfilePicture(profilePicturePath);
            view.updateProfilePicture(profilePicturePath);
            JOptionPane.showMessageDialog(view.getFrame(),
                "Profile picture updated successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                "Failed to update profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void handleProfilePictureRemoval() {
        // Get the current profile picture path before removing it
        String oldProfilePicturePath = currentUser.getProfilePicture();
        
        boolean success = teacherDA.updateTeacherProfilePicture(currentUser.getId(), null);
        
        if (success) {
            // Delete the old file if it exists
            if (oldProfilePicturePath != null && !oldProfilePicturePath.isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(oldProfilePicturePath));
                } catch (IOException e) {
                    e.printStackTrace();
                    // Continue even if file deletion fails
                }
            }
            
            // Update user model
            currentUser.setProfilePicture(null);
            
            // Update UI
            view.updateProfilePicture(null);
            
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Profile picture removed successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Failed to remove profile picture", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Getter methods for subcontrollers to access shared resources
    public Teacher getTeacher() {
        return (Teacher) currentUser;
    }
    
    public TeacherDA getTeacherDA() {
        return teacherDA;
    }
    
    public CourseDA getCourseDA() {
        return courseDA;
    }
    
    public MessageDA getMessageDA() {
        return messageDA;
    }
    
    public TeacherView getTeacherView() {
        return teacherView;
    }
}