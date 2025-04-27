package model.dao;

import java.util.ArrayList;
import java.util.Date;
import model.entities.Message;
import util.database.Database;

public class MessageDA {
    
    /**
     * Get messages for a specific teacher and course
     * @param teacherId The teacher ID
     * @param courseCode The course code
     * @return ArrayList of Message objects
     */
    public ArrayList<Message> getMessagesForTeacherAndCourse(String teacherId, String courseCode) {
        String query = "SELECT m.id, m.subject, m.message, m.timestamp, m.status, " +
                      "s.id AS sender_id, s.first_name AS sender_first_name, s.last_name AS sender_last_name " +
                      "FROM tb_message m " +
                      "JOIN tb_user s ON m.sender_id = s.id " +
                      "WHERE m.recipient_id = ? AND m.code_code = ? " +
                      "ORDER BY m.timestamp DESC";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, teacherId);
            stm.setString(2, courseCode);
        }, results -> {
            ArrayList<Message> messages = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String subject = results.getString("subject");
                String messageText = results.getString("message");
                Date timestamp = results.getTimestamp("timestamp");
                String status = results.getString("status");
                String senderId = results.getString("sender_id");
                String senderFirstName = results.getString("sender_first_name");
                String senderLastName = results.getString("sender_last_name");
                
                Message message = new Message();
                message.setId(id);
                message.setSubject(subject);
                message.setMessage(messageText);
                message.setTimestamp(timestamp);
                message.setStatus(status);
                message.setSenderId(senderId);
                message.setSenderFirstName(senderFirstName);
                message.setSenderLastName(senderLastName);
                message.setRecipientId(teacherId);
                message.setCourseCode(courseCode);
                
                messages.add(message);
            }
            
            return messages;
        });
    }
    
    /**
     * Get messages for a specific student
     * @param studentId The student ID
     * @return ArrayList of Message objects
     */
    public ArrayList<Message> getMessagesForStudent(String studentId) {
        String query = "SELECT m.id, m.subject, m.message, m.timestamp, m.status, " +
                      "m.code_code AS course_code, " +
                      "s.id AS sender_id, s.first_name AS sender_first_name, s.last_name AS sender_last_name " +
                      "FROM tb_message m " +
                      "JOIN tb_user s ON m.sender_id = s.id " +
                      "WHERE m.recipient_id = ? " +
                      "ORDER BY m.timestamp DESC";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, results -> {
            ArrayList<Message> messages = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String subject = results.getString("subject");
                String messageText = results.getString("message");
                Date timestamp = results.getTimestamp("timestamp");
                String status = results.getString("status");
                String courseCode = results.getString("course_code");
                String senderId = results.getString("sender_id");
                String senderFirstName = results.getString("sender_first_name");
                String senderLastName = results.getString("sender_last_name");
                
                Message message = new Message();
                message.setId(id);
                message.setSubject(subject);
                message.setMessage(messageText);
                message.setTimestamp(timestamp);
                message.setStatus(status);
                message.setSenderId(senderId);
                message.setSenderFirstName(senderFirstName);
                message.setSenderLastName(senderLastName);
                message.setRecipientId(studentId);
                message.setCourseCode(courseCode);
                
                messages.add(message);
            }
            
            return messages;
        });
    }
    
    /**
     * Send a message
     * @param senderId The sender ID
     * @param recipientId The recipient ID
     * @param courseCode The course code
     * @param subject The message subject
     * @param messageText The message text
     * @return true if the message was sent successfully, false otherwise
     */
    public boolean sendMessage(String senderId, String recipientId, String courseCode, 
                             String subject, String messageText) {
        String query = "INSERT INTO tb_message (sender_id, recipient_id, code_code, subject, message, status) " +
                      "VALUES (?, ?, ?, ?, ?, 'unread')";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, senderId);
            stm.setString(2, recipientId);
            stm.setString(3, courseCode);
            stm.setString(4, subject);
            stm.setString(5, messageText);
        }, null);
    }
    
    /**
     * Mark a message as read
     * @param messageId The message ID
     * @return true if the message was marked as read successfully, false otherwise
     */
    public boolean markMessageAsRead(String messageId) {
        String query = "UPDATE tb_message SET status = 'read' WHERE id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, messageId);
        }, null);
    }
    
    /**
     * Delete a message
     * @param messageId The message ID
     * @return true if the message was deleted successfully, false otherwise
     */
    public boolean deleteMessage(String messageId) {
        String query = "DELETE FROM tb_message WHERE id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, messageId);
        }, null);
    }
    
    /**
     * Get a count of unread messages for a user
     * @param userId The user ID
     * @return Count of unread messages
     */
    public int getUnreadMessageCount(String userId) {
        String query = "SELECT COUNT(*) AS unread_count FROM tb_message " +
                      "WHERE recipient_id = ? AND status = 'unread'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, userId);
        }, results -> {
            if (results.next()) {
                return results.getInt("unread_count");
            }
            return 0;
        });
    }
    
    /**
     * Get a single message by ID
     * @param messageId The message ID
     * @return Message object or null if not found
     */
    public Message getMessageById(String messageId) {
        String query = "SELECT m.id, m.subject, m.message, m.timestamp, m.status, " +
                      "m.sender_id, m.recipient_id, m.code_code, " +
                      "s.first_name AS sender_first_name, s.last_name AS sender_last_name " +
                      "FROM tb_message m " +
                      "JOIN tb_user s ON m.sender_id = s.id " +
                      "WHERE m.id = ?";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, messageId);
        }, results -> {
            if (results.next()) {
                String id = results.getString("id");
                String subject = results.getString("subject");
                String messageText = results.getString("message");
                Date timestamp = results.getTimestamp("timestamp");
                String status = results.getString("status");
                String senderId = results.getString("sender_id");
                String recipientId = results.getString("recipient_id");
                String courseCode = results.getString("code_code");
                String senderFirstName = results.getString("sender_first_name");
                String senderLastName = results.getString("sender_last_name");
                
                Message message = new Message();
                message.setId(id);
                message.setSubject(subject);
                message.setMessage(messageText);
                message.setTimestamp(timestamp);
                message.setStatus(status);
                message.setSenderId(senderId);
                message.setRecipientId(recipientId);
                message.setCourseCode(courseCode);
                message.setSenderFirstName(senderFirstName);
                message.setSenderLastName(senderLastName);
                
                return message;
            }
            return null;
        });
    }
    
    /**
     * Get messages between a student and a teacher for a specific course
     * @param studentId The student ID
     * @param teacherId The teacher ID
     * @param courseCode The course code
     * @return ArrayList of Message objects
     */
    public ArrayList<Message> getMessagesForStudentAndTeacher(String studentId, String teacherId, String courseCode) {
        String query = "SELECT m.id, m.subject, m.message, m.timestamp, m.status, " +
                      "m.sender_id, m.recipient_id, " +
                      "s.first_name AS sender_first_name, s.last_name AS sender_last_name " +
                      "FROM tb_message m " +
                      "JOIN tb_user s ON m.sender_id = s.id " +
                      "WHERE ((m.sender_id = ? AND m.recipient_id = ?) " +
                      "OR (m.sender_id = ? AND m.recipient_id = ?)) " +
                      "AND m.code_code = ? " +
                      "ORDER BY m.timestamp DESC";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
            stm.setString(2, teacherId);
            stm.setString(3, teacherId);
            stm.setString(4, studentId);
            stm.setString(5, courseCode);
        }, results -> {
            ArrayList<Message> messages = new ArrayList<>();
            
            while (results.next()) {
                String id = results.getString("id");
                String subject = results.getString("subject");
                String messageText = results.getString("message");
                Date timestamp = results.getTimestamp("timestamp");
                String status = results.getString("status");
                String senderId = results.getString("sender_id");
                String recipientId = results.getString("recipient_id");
                String senderFirstName = results.getString("sender_first_name");
                String senderLastName = results.getString("sender_last_name");
                
                Message message = new Message();
                message.setId(id);
                message.setSubject(subject);
                message.setMessage(messageText);
                message.setTimestamp(timestamp);
                message.setStatus(status);
                message.setSenderId(senderId);
                message.setRecipientId(recipientId);
                message.setCourseCode(courseCode);
                message.setSenderFirstName(senderFirstName);
                message.setSenderLastName(senderLastName);
                
                messages.add(message);
            }
            
            return messages;
        });
    }
    
    /**
     * Get unread message count for a specific student
     * @param studentId The student ID
     * @return Count of unread messages
     */
    public int getUnreadMessageCountForStudent(String studentId) {
        String query = "SELECT COUNT(*) AS unread_count FROM tb_message " +
                      "WHERE recipient_id = ? AND status = 'unread'";
        
        return new Database().executeQuery(query, stm -> {
            stm.setString(1, studentId);
        }, results -> {
            if (results.next()) {
                return results.getInt("unread_count");
            }
            return 0;
        });
    }
}