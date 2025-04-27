import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;
import java.util.UUID;
import javax.mail.*;
import javax.mail.internet.*;

import com.mysql.cj.Session;

public class EmailUtil {
    
    private static final String FROM_EMAIL = "your_system_email@example.com";
    private static final String EMAIL_PASSWORD = "your_email_password";
    private static final String SMTP_HOST = "smtp.example.com";
    private static final int SMTP_PORT = 587;
    
    public static String sendPasswordResetEmail(String toEmail, String resetToken, String resetUrl) throws MessagingException {
        // Set properties
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
        
        // Create message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Password Reset - School Management System");
        
        String htmlContent = 
            "<html>" +
            "<body>" +
            "<h2>School Management System - Password Reset</h2>" +
            "<p>You have requested to reset your password. Click the link below to reset your password:</p>" +
            "<p><a href='" + resetUrl + "?token=" + resetToken + "'>Reset Password</a></p>" +
            "<p>If you did not request this password reset, please ignore this email.</p>" +
            "<p>This link will expire in 30 minutes.</p>" +
            "</body>" +
            "</html>";
        
        message.setContent(htmlContent, "text/html");
        
        // Send message
        Transport.send(message);
        
        return resetToken;
    }
    
    public static String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}