package controller.student;

import controller.BaseController;
import model.dao.CourseDA;
import model.dao.MessageDA;
import model.dao.StudentDA;
import model.entities.Course;
import model.entities.Student;
import view.student.StudentView;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class StudentController extends BaseController {
    
    private StudentView studentView;
    private StudentDA studentDA;
    private CourseDA courseDA;
    private MessageDA messageDA;
    
    // Controllers for specific features
    private StudentCourseController courseController;
    private StudentEnrollController enrollController;
    private StudentMessageController messageController;
    
    public StudentController(Student student) {
        super(student, new StudentView(student));
        
        this.studentView = (StudentView) view;
        
        // data access objects
        this.studentDA = new StudentDA();
        this.courseDA = new CourseDA();
        this.messageDA = new MessageDA();
        
        // feature controllers
        this.courseController = new StudentCourseController(studentView.getCoursesPanel(), this);
        this.enrollController = new StudentEnrollController(studentView.getEnrollPanel(), this);
        this.messageController = new StudentMessageController(studentView.getMessagesPanel(), this);
        
        setupMenuListeners();
        
        initialize();
        
        // Display the view
        this.view.setVisible(true);
    }
    
    private void setupMenuListeners() {
        studentView.setCoursesMenuItemListener(e -> studentView.showPanel("courses"));
        studentView.setEnrollMenuItemListener(e -> studentView.showPanel("enroll"));
        studentView.setMessagesMenuItemListener(e -> studentView.showPanel("messages"));
    }
    
    @Override
    protected void setupSpecificListeners() {
    }
    
    @Override
    protected void loadInitialData() {
        try {
            // student's enrolled courses
            ArrayList<Course> courses = studentDA.getCoursesForStudent(currentUser.getId());
            
            courseController.updateCourseTable(courses);
            
            //update the course selector in messages panel
            studentView.getMessagesPanel().updateCourseSelector(courses);
            
            // Check if there are courses and preselect the first one for messages
            if (!courses.isEmpty()) {
                Course firstCourse = courses.get(0);
                messageController.loadTeachersForCourse(firstCourse);
            }
            
            enrollController.loadAvailableCourses();
        } catch (Exception e) {
            showErrorMessage("Error loading initial data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadStudentCourses() {
        try {
            // Get courses for the current student
            ArrayList<Course> courses = studentDA.getCoursesForStudent(currentUser.getId());
            
            //Update the course panel table
            courseController.updateCourseTable(courses);
            
            //update the course selector in messages panel
            studentView.getMessagesPanel().updateCourseSelector(courses);
        } catch (Exception e) {
            showErrorMessage("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void initialize() {
        super.initialize();
        
        //add to view
        studentView.addPropertyChangeListener(this::handlePropertyChange);
    }

    @Override
    protected void handlePropertyChange(PropertyChangeEvent evt) {
        super.handlePropertyChange(evt);
    }

    public Student getStudent() {
        return (Student) currentUser;
    }
    
    public StudentDA getStudentDA() {
        return studentDA;
    }
    
    public CourseDA getCourseDA() {
        return courseDA;
    }
    
    public MessageDA getMessageDA() {
        return messageDA;
    }
    
    public StudentView getStudentView() {
        return studentView;
    }
    
    public StudentCourseController getCourseController() {
        return courseController;
    }
    
    public StudentEnrollController getEnrollController() {
        return enrollController;
    }
    
    public StudentMessageController getMessageController() {
        return messageController;
    }
}