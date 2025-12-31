package org.xproce.firesafe_audit.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Override
    public void sendPasswordResetEmail(String to, String userName, String resetLink) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("resetLink", resetLink);

        sendHtmlEmail(
                to,
                "Réinitialisation de mot de passe - FireSafe Audit",
                "password-reset",
                variables
        );
    }

    @Override
    public void sendAuditPlanifiedEmail(String to, String userName, String etablissementNom, String dateAudit) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("etablissementNom", etablissementNom);
        variables.put("dateAudit", dateAudit);

        sendHtmlEmail(
                to,
                "Audit planifié - " + etablissementNom,
                "audit-planified",
                variables
        );
    }

    @Override
    public void sendAuditReportEmail(String to, String etablissementNom, String tauxConformite, byte[] pdfAttachment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("etablissementNom", etablissementNom);
        variables.put("tauxConformite", tauxConformite);

        sendHtmlEmailWithAttachment(
                to,
                "Rapport d'audit - " + etablissementNom,
                "audit-report",
                variables,
                "rapport-audit.pdf",
                pdfAttachment
        );
    }

    @Override
    public void sendNonConformiteCritiqueEmail(String to, String etablissementNom, String critereName) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("etablissementNom", etablissementNom);
        variables.put("critereName", critereName);

        sendHtmlEmail(
                to,
                "⚠️ Non-conformité critique - " + etablissementNom,
                "non-conformite-critique",
                variables
        );
    }

    @Override
    public void sendWelcomeEmail(String to, String userName, String username, String tempPassword) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", userName);
        variables.put("username", username);
        variables.put("tempPassword", tempPassword);

        sendHtmlEmail(
                to,
                "Bienvenue sur FireSafe Audit",
                "welcome",
                variables
        );
    }

    private void sendHtmlEmail(String to, String subject, String template, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process("emails/" + template, context);

            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

    private void sendHtmlEmailWithAttachment(String to, String subject, String template,
                                             Map<String, Object> variables, String attachmentName,
                                             byte[] attachment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            context.setVariables(variables);
            String html = templateEngine.process("emails/" + template, context);

            helper.setText(html, true);

            helper.addAttachment(attachmentName, () -> new java.io.ByteArrayInputStream(attachment));

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email avec pièce jointe: " + e.getMessage());
        }
    }
}