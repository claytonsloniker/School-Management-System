package controller.teacher;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;

import model.entities.Course;
import model.entities.Message;
import view.teacher.TeacherMessagesPanel;

public class TeacherMessageController {
    
    private TeacherMessagesPanel view;
    private TeacherController parentController;
    private ArrayList<Message> currentMessages;
    
    public TeacherMessageController(TeacherMessagesPanel view, TeacherController parentController) {
        this.view = view;
        this.parentController = parentController;
        this.currentMessages = new ArrayList<>();
        
        // setup listeners
        setupListeners();
    }
    
    private void setupListeners() {
        // Set up course selection listener
        view.setCourseSelectionListener(e -> {
            Course selectedCourse = view.getSelectedCourse();
            if (selectedCourse != null) {
                loadMessagesForCourse(selectedCourse);
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
        
        // Set up mark as read button listener
        view.setMarkAsReadButtonListener(e -> handleMarkAsRead());
        
        // Set up reply button listener
        view.setReplyButtonListener(e -> handleReplyToMessage());
    }
    
    public void loadMessagesForCourse(Course course) {
        try {
            if (course != null) {
                ArrayList<Message> messages = parentController.getMessageDA().getMessagesForTeacherAndCourse(
                    parentController.getTeacher().getId(), 
                    course.getCode()
                );

                // Store current messages
                this.currentMessages = messages;
                
                //update view
                view.updateMessageTable(messages);
            }
        } catch (Exception e) {
            e.printStackTrace();
            parentController.showErrorMessage("Error loading messages: " + e.getMessage());
        }
    }
    
    private void handleMarkAsRead() {
        Message selectedMessage = view.getSelectedMessage();
        
        if (selectedMessage == null) {
            parentController.showErrorMessage("Please select a message first");
            return;
        }
        
        if ("read".equals(selectedMessage.getStatus())) {
            JOptionPane.showMessageDialog(
                parentController.getTeacherView(), 
                "This message is already marked as read", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        boolean success = parentController.getMessageDA().markMessageAsRead(selectedMessage.getId());
        
        if (success) {
            JOptionPane.showMessageDialog(
                parentController.getTeacherView(), 
                "Message marked as read", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Refresh the message list
            Course selectedCourse = view.getSelectedCourse();
            if (selectedCourse != null) {
                loadMessagesForCourse(selectedCourse);
            }
        } else {
            parentController.showErrorMessage("Failed to mark message as read");
        }
    }
    
    private void handleReplyToMessage() {
        Message selectedMessage = view.getSelectedMessage();
        Course selectedCourse = view.getSelectedCourse();
        
        if (selectedMessage == null || selectedCourse == null) {
            parentController.showErrorMessage("Please select a message to reply to");
            return;
        }
        
        String replyText = view.getReplyText();
        
        if (replyText == null || replyText.trim().isEmpty()) {
            parentController.showErrorMessage("Please enter a reply message");
            return;
        }
        
        // Create reply subject with RE: prefix if not already there
        String subject = selectedMessage.getSubject();
        if (!subject.startsWith("RE:")) {
            subject = "RE: " + subject;
        }
        
        boolean success = parentController.getMessageDA().sendMessage(
            parentController.getTeacher().getId(),
            selectedMessage.getSenderId(),
            selectedCourse.getCode(),
            subject,
            replyText
        );
        
        if (success) {
            JOptionPane.showMessageDialog(
                parentController.getTeacherView(), 
                "Reply sent successfully", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE
            );
            
            // Clear the reply text area
            view.clearReplyArea();
            
            // Also mark the original message as read
            if (!"read".equals(selectedMessage.getStatus())) {
                parentController.getMessageDA().markMessageAsRead(selectedMessage.getId());
            }
            
            // Refresh the message list
            loadMessagesForCourse(selectedCourse);
        } else {
            parentController.showErrorMessage("Failed to send reply");
        }
    }
}