package controller.student;

import java.util.ArrayList;
import javax.swing.*;

import model.dao.CourseDA;
import model.dao.EnrollmentDA;
import model.entities.Auth;
import model.entities.CourseWithTeacher;
import view.student.CourseEnrollmentPanel;

public class StudentCourseController {
    
    private CourseEnrollmentPanel view;
    private CourseDA courseDA;
    private EnrollmentDA enrollmentDA;
    private Auth currentUser;
    private JFrame parentFrame;
    
    public StudentCourseController(CourseEnrollmentPanel view, Auth currentUser, JFrame parentFrame) {
        this.view = view;
        this.currentUser = currentUser;
        this.parentFrame = parentFrame;
        this.courseDA = new CourseDA();
        this.enrollmentDA = new EnrollmentDA();
        
        // Set up listeners
        setupListeners();
        
        // Load initial data
        loadAvailableCourses();
        loadEnrolledCourses();
    }
    
    private void setupListeners() {
        view.setEnrollButtonListener(e -> handleEnrollCourse());
        view.setDropButtonListener(e -> handleDropCourse());
        view.setViewDetailsButtonListener(e -> handleViewCourseDetails());
    }
    
    public void loadAvailableCourses() {
        try {
            ArrayList<CourseWithTeacher> availableCourses = courseDA.getAvailableCoursesForStudent(currentUser.getId());
            view.updateAvailableCourseTable(availableCourses);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Error loading available courses: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    public void loadEnrolledCourses() {
        try {
            ArrayList<CourseWithTeacher> enrolledCourses = courseDA.getEnrolledCoursesForStudent(currentUser.getId());
            view.updateEnrolledCourseTable(enrolledCourses);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Error loading enrolled courses: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void handleEnrollCourse() {
        // Implementation for enrolling in a course
        CourseWithTeacher selectedCourse = view.getSelectedAvailableCourse();
        
        if (selectedCourse == null) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Please select a course to enroll in", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        boolean success = enrollmentDA.enrollStudent(currentUser.getId(), selectedCourse.getCode());
        
        if (success) {
            JOptionPane.showMessageDialog(parentFrame, 
                "Successfully enrolled in " + selectedCourse.getName(), 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            loadAvailableCourses();
            loadEnrolledCourses();
        } else {
            JOptionPane.showMessageDialog(parentFrame, 
                "Failed to enroll in course", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleDropCourse() {
        // Implementation for dropping a course
    }
    
    private void handleViewCourseDetails() {
        // Implementation for viewing course details
    }
}