package util.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {
    
    private static final String FROM_EMAIL = "c99615435@gmail.com";
    private static final String EMAIL_PASSWORD = "npip gbia vaje wscf";
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    
    /**
     * Send a temporary password email
     * @param toEmail Recipient email address
     * @param tempPassword Temporary password
     * @throws MessagingException if there's an error sending the email
     */
    public static void sendTemporaryPasswordEmail(String toEmail, String tempPassword) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        }); 
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Temporary Password - School Management System");
        
        String htmlContent = 
            "<html>" +
            "<body>" +
            "<h2>School Management System - Temporary Password</h2>" +
            "<p>You have requested a password reset. Here is your temporary password:</p>" +
            "<p style='font-size: 20px; font-weight: bold; background-color: #f0f0f0; padding: 10px;'>" + tempPassword + "</p>" +
            "<p>Please log in with this temporary password and change it immediately for security reasons.</p>" +
            "<p>If you did not request this password reset, please contact the system administrator.</p>" +
            "</body>" +
            "</html>";
        
        message.setContent(htmlContent, "text/html");
        
        Transport.send(message);
    }
}