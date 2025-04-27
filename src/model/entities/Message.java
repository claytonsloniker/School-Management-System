package model.entities;

import java.util.Date;

public class Message {
    private String id;
    private String senderId;
    private String senderFirstName;
    private String senderLastName;
    private String recipientId;
    private String courseCode;
    private String subject;
    private String message;
    private Date timestamp;
    private String status;  // "read" or "unread"
    
    /**
     * Default constructor
     */
    public Message() {
    }
    
    /**
     * Full constructor
     * @param id Message ID
     * @param senderId Sender ID
     * @param senderFirstName Sender first name
     * @param senderLastName Sender last name
     * @param recipientId Recipient ID
     * @param courseCode Course code
     * @param subject Message subject
     * @param message Message text
     * @param timestamp Message timestamp
     * @param status Message status (read/unread)
     */
    public Message(String id, String senderId, String senderFirstName, String senderLastName,
                  String recipientId, String courseCode, String subject, String message,
                  Date timestamp, String status) {
        this.id = id;
        this.senderId = senderId;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.recipientId = recipientId;
        this.courseCode = courseCode;
        this.subject = subject;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }
    
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * @return the senderId
     */
    public String getSenderId() {
        return senderId;
    }
    
    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    
    /**
     * @return the senderFirstName
     */
    public String getSenderFirstName() {
        return senderFirstName;
    }
    
    /**
     * @param senderFirstName the senderFirstName to set
     */
    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }
    
    /**
     * @return the senderLastName
     */
    public String getSenderLastName() {
        return senderLastName;
    }
    
    /**
     * @param senderLastName the senderLastName to set
     */
    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }
    
    /**
     * @return the sender's full name
     */
    public String getSenderName() {
        return senderFirstName + " " + senderLastName;
    }
    
    /**
     * @return the recipientId
     */
    public String getRecipientId() {
        return recipientId;
    }
    
    /**
     * @param recipientId the recipientId to set
     */
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }
    
    /**
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }
    
    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }
    
    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }
    
    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Message [id=" + id + ", subject=" + subject + ", from=" + getSenderName() +
               ", timestamp=" + timestamp + ", status=" + status + "]";
    }
}