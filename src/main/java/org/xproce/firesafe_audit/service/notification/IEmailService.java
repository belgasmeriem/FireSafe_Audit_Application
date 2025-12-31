package org.xproce.firesafe_audit.service.notification;

public interface IEmailService {
    void sendPasswordResetEmail(String to, String userName, String resetLink);
    void sendAuditPlanifiedEmail(String to, String userName, String etablissementNom, String dateAudit);
    void sendAuditReportEmail(String to, String etablissementNom, String tauxConformite, byte[] pdfAttachment);
    void sendNonConformiteCritiqueEmail(String to, String etablissementNom, String critereName);
    void sendWelcomeEmail(String to, String userName, String username, String tempPassword);
}