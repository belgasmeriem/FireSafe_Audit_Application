package org.xproce.firesafe_audit.service.notification;

import jakarta.mail.MessagingException;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.entities.User;
import java.util.List;

public interface IEmailService {

    void sendEmailWithAttachment(
            String to,
            List<String> cc,
            String subject,
            String messageContent,
            byte[] attachment,
            String attachmentName,
            Audit audit) throws MessagingException;

    void sendPasswordResetEmail(String to, String userName, String resetLink);

    void sendWelcomeEmail(User user);
}