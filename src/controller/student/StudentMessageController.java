package controller.student;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import model.entities.Course;
import model.entities.Message;
import model.entities.Teacher;
import view.student.StudentMessagesPanel;

public class StudentMessageController {
    
    private StudentMessagesPanel view;
    private StudentController parentController;
    private ArrayList<Message> currentMessages;
    
    public StudentMessageController(StudentMessagesPanel view, StudentController parentController) {
        this.view = view;
        this.parentController = parentController;
        this.currentMessages = new ArrayList<>();
        
        // Set up listeners
        setupListeners();
    }
    
    private void setupListeners() {
        // Set up course selection listener
        view.setCourseSelectionListener(e -> {
            Course selectedCourse = view.getSelectedCourse();
            if (selectedCourse != null) {
                loadTeachersForCourse(selectedCourse);
            }
        });
        
        // Set up teacher selection listener
        view.setTeacherSelectionListener(e -> {
            Teacher selectedTeacher = view.getSelectedTeacher();
            Course selectedCourse = view.getSelectedCourse();
            if (selectedTeacher != null && selectedCourse != null) {
                loadMessagesForCourseAndTeacher(selectedCourse, selectedTeacher);
            }
        });
        
        // Set up message selection listener
        view.setMessageTableSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Message selectedMessage = view.getSelectedMessage();
                if (selectedMessage != null) {
                    view.displayMessage(selectedMessage);
                }
            }
        });
        
        // Set up send button listener
        view.setSendButtonListener(e -> handleSendMessage());
        
        // Set up reply button listener
        view.setReplyButtonListener(e -> handleReplyToMessage());
    }
    
    public void loadTeachersForCourse(Course course) {
        try {
            if (course != null) {
                // Get teachers for this course
                ArrayList<Teacher> teachers = parentController.getStudentDA().getTeachersForCourse(course.getCode());
                
                //debug
                //System.out.println("Loaded " + teachers.size() + " teachers for course " + course.getCode());
                
                //Update the view's teacher selector
                view.updateTeacherSelector(teachers);
                
                //if there are teachers, preselect the first one and load messages
                if (!teachers.isEmpty()) {
                    Teacher firstTeacher = teachers.get(0);
                    loadMessagesForCourseAndTeacher(course, firstTeacher);
                } else {
                    //Clear the message table if no teachers
                    view.updateMessageTable(new ArrayList<>());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            parentController.showErrorMessage("Error loading teachers: " + e.getMessage());
        }
    }
    
    private void loadMessagesForCourseAndTeacher(Course course, Teacher teacher) {
        try {
            if (course != null && teacher != null) {
                //debug
            	//System.out.println("Loading messages for course " + course.getCode() + " and teacher " + teacher.getId());
                
                ArrayList<Message> messages = parentController.getMessageDA().getMessagesForStudentAndTeacher(
                    parentController.getStudent().getId(),
                    teacher.getId(),
                    course.getCode()
                );
                
                //System.out.println("Loaded " + messages.size() + " messages");
                
                // Store the current messages
                this.currentMessages = messages;
                
                // Update the view
                view.updateMessageTable(messages);
            }
        } catch (Exception e) {
            e.printStackTrace();
            parentController.showErrorMessage("Error loading messages: " + e.getMessage());
        }
    }
    
    private void handleSendMessage() {
        Teacher selectedTeacher = view.getSelectedTeacher();
        Course selectedCourse = view.getSelectedCourse();
        
        if (selectedTeacher == null || selectedCourse == null) {
            parentController.showErrorMessage("Please select both a course and a teacher");
            return;
        }
        
        String subject = view.getMessageSubject();
        String messageText = view.getMessageText();
        
        if (subject == null || subject.trim().isEmpty()) {
            parentController.showErrorMessage("Please enter a subject for your message");
            return;
        }
        
        if (messageText == null || messageText.trim().isEmpty()) {
            parentController.showErrorMessage("Please enter a message");
            return;
        }
        
        boolean success = parentController.getMessageDA().sendMessage(
            parentController.getStudent().getId(),
            selectedTeacher.getId(),
            selectedCourse.getCode(),
            subject,
            messageText
        );
        
        if (success) {
            JOptionPane.showMessageDialog(
                parentController.getStudentView(),
                "Message sent successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Clear the message fields
            view.clearMessageFields();
            
            // Refresh the message list
            loadMessagesForCourseAndTeacher(selectedCourse, selectedTeacher);
        } else {
            parentController.showErrorMessage("Failed to send message");
        }
    }
    
    private void handleReplyToMessage() {
        Message selectedMessage = view.getSelectedMessage();
        Teacher selectedTeacher = view.getSelectedTeacher();
        Course selectedCourse = view.getSelectedCourse();
        
        if (selectedMessage == null || selectedTeacher == null || selectedCourse == null) {
            parentController.showErrorMessage("Please select a message to reply to");
            return;
        }
        
        String replyText = view.getMessageText();
        
        if (replyText == null || replyText.trim().isEmpty()) {
            parentController.showErrorMessage("Please enter a reply message");
            return;
        }
        
        // Create reply subject with RE: prefix if not already there
        String subject = view.getMessageSubject();
        if (!subject.startsWith("RE:")) {
            subject = "RE: " + subject;
        }
        
        boolean success = parentController.getMessageDA().sendMessage(
            parentController.getStudent().getId(),
            selectedTeacher.getId(),
            selectedCourse.getCode(),
            subject,
            replyText
        );
        
        if (success) {
            JOptionPane.showMessageDialog(
                parentController.getStudentView(),
                "Reply sent successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Clear the message fields
            view.clearMessageFields();
            
            // Refresh the message list
            loadMessagesForCourseAndTeacher(selectedCourse, selectedTeacher);
        } else {
            parentController.showErrorMessage("Failed to send reply");
        }
    }
}