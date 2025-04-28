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
    
    private TeacherCourseController courseController;
    private TeacherStudentController studentController;
    private TeacherMessageController messageController;
    
    public TeacherController(Teacher teacher) {
        super(teacher, new TeacherView(teacher));
        
        this.teacherView = (TeacherView) view;
        
        // data access objects
        this.teacherDA = new TeacherDA();
        this.courseDA = new CourseDA();
        this.messageDA = new MessageDA();
        
        // feature controllers
        this.courseController = new TeacherCourseController(teacherView.getCoursesPanel(), this);
        this.studentController = new TeacherStudentController(teacherView.getStudentsPanel(), this);
        this.messageController = new TeacherMessageController(teacherView.getMessagesPanel(), this);
        
        setupMenuListeners();
        initialize();
        
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
        
        // cross-panel navigation listeners
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
            
            courseController.updateCourseTable(courses);
            teacherView.getStudentsPanel().updateCourseSelector(courses);
            teacherView.getMessagesPanel().updateCourseSelector(courses);
        } catch (Exception e) {
            showErrorMessage("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void initialize() {
        super.initialize();
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
        String oldProfilePicturePath = currentUser.getProfilePicture();
        
        boolean success = teacherDA.updateTeacherProfilePicture(currentUser.getId(), null);
        
        if (success) {
            // Delete the old file if it exists
            if (oldProfilePicturePath != null && !oldProfilePicturePath.isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(oldProfilePicturePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            currentUser.setProfilePicture(null);
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