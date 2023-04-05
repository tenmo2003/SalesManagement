package salesmanagement.salesmanagement;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * MailSender helps system send email including authentic code to users via method send.
 *
 * @author THANH AN
 * @since 1.4
 */
public class MailSender {
    private static final String title = "Verify your email to create your Sales Management account";
    private static String extractUsername(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex >= 0) {
            return email.substring(0, atIndex);
        } else {
            return null;
        }
    }

    public static void send(String emailReceiver, int code) {
        final String username = "smdevelopersse2023@gmail.com";
        final String password = "sjyaadbginfbfpeo";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailReceiver));
            message.setSubject(title);
            message.setText("Dear " + extractUsername(emailReceiver) + ",\n" +
                    "\n" +
                    "We would like to extend a warm welcome to SalesManagement, our sales management application, and express our appreciation for your interest in our platform.\n" +
                    "\n" +
                    "To ensure the security and authenticity of user information, we need to verify your email address. To complete the registration process, please use the verification code below to confirm your account:\n" +
                    "\n" +
                    +code + "\n" +
                    "\n" +
                    "Please enter this code into our application to activate your account. Please note that the verification code will expire after 2 minutes. This is to ensure that the verification process is completed in a timely manner and to maintain the security of your information.\n" +
                    "\n" +
                    "Your account will not be activated if this verification process is not completed within the allotted time. If you require any assistance or have any questions, please do not hesitate to contact us via email or the phone number provided on our website.\n" +
                    "\n" +
                    "Once again, we would like to express our sincere thanks for choosing SalesManagement as your sales management platform. We look forward to supporting your business growth and success.\n" +
                    "\n" +
                    "Warm regards,\n" +
                    "\n" +
                    "SalesManagement Team");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
