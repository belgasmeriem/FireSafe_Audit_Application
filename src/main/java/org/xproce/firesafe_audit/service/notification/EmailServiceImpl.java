package org.xproce.firesafe_audit.service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:contact@firesafe.com}")
    private String fromEmail;

    @Value("${app.mail.from-name:FireSafe Audit}")
    private String fromName;

    @Value("${app.url:http://localhost:8090}")
    private String appUrl;

    @Override
    public void sendEmailWithAttachment(
            String to,
            List<String> cc,
            String subject,
            String messageContent,
            byte[] attachment,
            String attachmentName,
            Audit audit) throws MessagingException {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setReplyTo(fromEmail, fromName);
            helper.setTo(to);

            if (cc != null && !cc.isEmpty()) {
                helper.setCc(cc.toArray(new String[0]));
            }

            helper.setSubject(subject);

            String htmlContent = buildAuditReportEmailContent(audit, messageContent);
            helper.setText(htmlContent, true);

            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));

            mailSender.send(message);
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("Erreur d'encodage lors de la préparation de l'email", e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String email, String nomComplet, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setReplyTo(fromEmail, fromName);
            helper.setTo(email);
            helper.setSubject("Réinitialisation de votre mot de passe - FireSafety Audit");

            String htmlContent = buildPasswordResetEmailContent(nomComplet, resetLink);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de réinitialisation", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erreur d'encodage lors de la préparation de l'email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setReplyTo(fromEmail, fromName);
            helper.setTo(user.getEmail());
            helper.setSubject("Bienvenue sur FireSafety Audit");

            String htmlContent = buildWelcomeEmailContent(user);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'email de bienvenue", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Erreur d'encodage lors de la préparation de l'email", e);
        }
    }

    private String buildWelcomeEmailContent(User user) {
        String loginUrl = appUrl + "/auth/login";
        String roleLabel = user.getRoles().stream()
                .findFirst()
                .map(role -> role.getName().getLibelle())
                .orElse("Utilisateur");

        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <style>
                                body {
                                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                                    background-color: #f5f7fb;
                                    margin: 0;
                                    padding: 20px;
                                    line-height: 1.6;
                                }
                                .email-container {
                                    max-width: 650px;
                                    margin: 0 auto;
                                    background: white;
                                    border-radius: 12px;
                                    overflow: hidden;
                                    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
                                }
                                .header {
                                    background: linear-gradient(135deg, #006666 0%%, #008080 100%%);
                                    color: white;
                                    padding: 40px 30px;
                                    text-align: center;
                                    position: relative;
                                }
                                .header::after {
                                    content: '';
                                    position: absolute;
                                    bottom: -20px;
                                    left: 0;
                                    right: 0;
                                    height: 20px;
                                    background: linear-gradient(135deg, #006666 0%%, #008080 100%%);
                                    clip-path: polygon(0 0, 100%% 0, 100%% 0, 0 100%%);
                                }
                                .header-icon {
                                    font-size: 64px;
                                    margin-bottom: 15px;
                                    animation: fire 2s ease-in-out infinite;
                                }
                                @keyframes fire {
                                    0%%, 100%% { transform: scale(1); }
                                    50%% { transform: scale(1.1); }
                                }
                                .header h1 {
                                    margin: 0;
                                    font-size: 32px;
                                    font-weight: 700;
                                    text-shadow: 0 2px 4px rgba(0,0,0,0.2);
                                }
                                .header p {
                                    margin: 10px 0 0;
                                    font-size: 16px;
                                    opacity: 0.95;
                                }
                                .content {
                                    padding: 40px 30px;
                                }
                                .greeting {
                                    font-size: 24px;
                                    color: #006666;
                                    margin-bottom: 20px;
                                    font-weight: 700;
                                }
                                .intro-text {
                                    font-size: 16px;
                                    color: #424242;
                                    margin-bottom: 30px;
                                    line-height: 1.8;
                                }
                                .role-badge {
                                    display: inline-block;
                                    background: #10b981;
                                    color: white;
                                    padding: 8px 20px;
                                    border-radius: 20px;
                                    font-size: 14px;
                                    font-weight: 600;
                                    margin-top: 10px;
                                }
                                .info-box {
                                    background: linear-gradient(135deg, #f0f9ff 0%%, #e0f2fe 100%%);
                                    border: 2px solid #0ea5e9;
                                    border-left: 6px solid #0284c7;
                                    border-radius: 12px;
                                    padding: 25px;
                                    margin: 30px 0;
                                }
                                .info-box-title {
                                    font-size: 18px;
                                    font-weight: 700;
                                    color: #0c4a6e;
                                    margin-bottom: 15px;
                                    display: flex;
                                    align-items: center;
                                    gap: 10px;
                                }
                                .info-box-content {
                                    color: #424242;
                                    font-size: 15px;
                                    line-height: 1.8;
                                }
                                .button-container {
                                    text-align: center;
                                    margin: 35px 0;
                                }
                                .login-button {
                                    display: inline-block;
                                    background: linear-gradient(135deg, #006666, #008080);
                                    color: white;
                                    padding: 16px 45px;
                                    text-decoration: none;
                                    border-radius: 10px;
                                    font-weight: 700;
                                    font-size: 18px;
                                    box-shadow: 0 4px 15px rgba(0, 102, 102, 0.3);
                                    transition: all 0.3s ease;
                                }
                                .login-button:hover {
                                    transform: translateY(-2px);
                                    box-shadow: 0 6px 20px rgba(0, 102, 102, 0.4);
                                }
                                .features-grid {
                                    display: grid;
                                    grid-template-columns: repeat(2, 1fr);
                                    gap: 15px;
                                    margin: 30px 0;
                                }
                                .feature-item {
                                    background: #f8f9fa;
                                    padding: 20px;
                                    border-radius: 10px;
                                    text-align: center;
                                }
                                .feature-icon {
                                    font-size: 32px;
                                    margin-bottom: 10px;
                                }
                                .feature-title {
                                    font-weight: 700;
                                    color: #006666;
                                    font-size: 14px;
                                    margin-bottom: 5px;
                                }
                                .feature-desc {
                                    font-size: 13px;
                                    color: #666;
                                }
                                .footer {
                                    background: linear-gradient(135deg, #2c3e50 0%%, #34495e 100%%);
                                    color: white;
                                    padding: 30px;
                                    text-align: center;
                                }
                                .footer-logo {
                                    font-size: 40px;
                                    margin-bottom: 10px;
                                }
                                .footer h4 {
                                    margin: 0 0 5px;
                                    font-size: 20px;
                                    font-weight: 600;
                                }
                                .footer p {
                                    margin: 5px 0;
                                    font-size: 13px;
                                    opacity: 0.9;
                                }
                                .divider {
                                    height: 1px;
                                    background: rgba(255,255,255,0.2);
                                    margin: 20px 0;
                                }
                                @media only screen and (max-width: 600px) {
                                    .features-grid {
                                        grid-template-columns: 1fr;
                                    }
                                }
                            </style>
                        </head>
                        <body>
                            <div class="email-container">
                                <div class="header">
                                    <div class="header-icon">🔥</div>
                                    <h1>Bienvenue sur FireSafety Audit</h1>
                                    <p>Votre compte a été créé avec succès</p>
                                </div>
                        
                                <div class="content">
                                    <div class="greeting">
                                        Bonjour %s,
                                    </div>
                        
                                    <div class="intro-text">
                                        Nous sommes ravis de vous accueillir sur <strong>FireSafety Audit</strong>, 
                                        la plateforme de gestion des audits de sécurité incendie. 
                                        Un compte a été créé pour vous avec le rôle suivant :
                                        <div class="role-badge">👤 %s</div>
                                    </div>
                        
                                    <div class="info-box">
                                        <div class="info-box-title">
                                            <span style="font-size: 24px;">✨</span>
                                            <span>Votre compte est prêt !</span>
                                        </div>
                                        <div class="info-box-content">
                                            Vous pouvez maintenant vous connecter à la plateforme avec les identifiants 
                                            qui vous ont été communiqués par votre administrateur. Vous aurez accès à 
                                            toutes les fonctionnalités correspondant à votre rôle.
                                        </div>
                                    </div>
                        
                                    <div class="features-grid">
                                        <div class="feature-item">
                                            <div class="feature-icon">📋</div>
                                            <div class="feature-title">Gestion d'audits</div>
                                            <div class="feature-desc">Créez et gérez vos audits facilement</div>
                                        </div>
                                        <div class="feature-item">
                                            <div class="feature-icon">📊</div>
                                            <div class="feature-title">Rapports détaillés</div>
                                            <div class="feature-desc">Générez des rapports professionnels</div>
                                        </div>
                                        <div class="feature-item">
                                            <div class="feature-icon">📸</div>
                                            <div class="feature-title">Documentation photo</div>
                                            <div class="feature-desc">Ajoutez des photos aux observations</div>
                                        </div>
                                        <div class="feature-item">
                                            <div class="feature-icon">✅</div>
                                            <div class="feature-title">Suivi en temps réel</div>
                                            <div class="feature-desc">Suivez l'avancement des audits</div>
                                        </div>
                                    </div>
                        
                                    <div class="button-container">
                                        <a href="%s" class="login-button">
                                            🚀 Accéder à la plateforme
                                        </a>
                                    </div>
                        
                                    <div class="info-box" style="background: #fff3cd; border-color: #ffc107; border-left-color: #ff9800;">
                                        <div class="info-box-title" style="color: #856404;">
                                            <span style="font-size: 24px;">💡</span>
                                            <span>Besoin d'aide ?</span>
                                        </div>
                                        <div class="info-box-content" style="color: #856404;">
                                            Si vous avez des questions ou rencontrez des difficultés, 
                                            n'hésitez pas à contacter votre administrateur ou le support technique.
                                        </div>
                                    </div>
                                </div>
                        
                                <div class="footer">
                                    <div class="footer-logo">🔥</div>
                                    <h4>FireSafety Audit</h4>
                                    <p>Plateforme de gestion des audits de sécurité incendie</p>
                                    <div class="divider"></div>
                                    <p style="font-size: 12px; opacity: 0.8;">
                                        Cet email a été envoyé automatiquement suite à la création de votre compte.<br>
                                        Pour toute question, contactez votre administrateur.
                                    </p>
                                    <p style="margin-top: 15px; font-size: 11px;">
                                        © 2026 FireSafety Audit - Tous droits réservés
                                    </p>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                user.getNomComplet(),
                roleLabel,
                loginUrl
        );
    }

    private String buildAuditReportEmailContent(Audit audit, String messageContent) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateAudit = audit.getDateAudit() != null ?
                audit.getDateAudit().format(formatter) : "N/A";

        String etablissementNom = audit.getEtablissement() != null ?
                audit.getEtablissement().getNom() : "N/A";

        String auditeurNom = audit.getAuditeur() != null ?
                audit.getAuditeur().getNomComplet() : "N/A";

        String typeEtab = audit.getEtablissement() != null && audit.getEtablissement().getType() != null ?
                audit.getEtablissement().getType().name() : "N/A";

        String adresseEtab = audit.getEtablissement() != null ?
                audit.getEtablissement().getAdresseComplete() : "N/A";

        String normeCode = audit.getEtablissement() != null && audit.getEtablissement().getNorme() != null ?
                audit.getEtablissement().getNorme().getCode() : "N/A";

        String tauxConformite = audit.getTauxConformite() != null ?
                audit.getTauxConformite() + "%%" : "N/A";

        return String.format("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                    line-height: 1.6;
                    color: #333;
                    margin: 0;
                    padding: 0;
                    background-color: #f5f7fb;
                }
                .email-container {
                    max-width: 650px;
                    margin: 20px auto;
                    background: white;
                    border-radius: 12px;
                    overflow: hidden;
                    box-shadow: 0 4px 20px rgba(0,0,0,0.1);
                }
                .header {
                    background: linear-gradient(135deg, #006666 0%%, #008080 100%%);
                    color: white;
                    padding: 40px 30px;
                    text-align: center;
                    position: relative;
                }
                .header::after {
                    content: '';
                    position: absolute;
                    bottom: -20px;
                    left: 0;
                    right: 0;
                    height: 20px;
                    background: linear-gradient(135deg, #006666 0%%, #008080 100%%);
                    clip-path: polygon(0 0, 100%% 0, 100%% 0, 0 100%%);
                }
                .header-icon {
                    font-size: 48px;
                    margin-bottom: 10px;
                }
                .header h1 {
                    margin: 0;
                    font-size: 28px;
                    font-weight: 700;
                    text-shadow: 0 2px 4px rgba(0,0,0,0.2);
                }
                .header p {
                    margin: 10px 0 0;
                    font-size: 16px;
                    opacity: 0.95;
                }
                .content {
                    padding: 40px 30px;
                }
                .greeting {
                    font-size: 18px;
                    color: #006666;
                    margin-bottom: 20px;
                    font-weight: 600;
                }
                
                /* MESSAGE PERSONNALISÉ - STYLE AMÉLIORÉ */
                .message-personnalise {
                    background: linear-gradient(135deg, #fff8e1 0%%, #fffbf0 100%%);
                    border: 2px solid #ffc107;
                    border-left: 6px solid #ff9800;
                    border-radius: 10px;
                    padding: 25px;
                    margin: 30px 0;
                    box-shadow: 0 2px 8px rgba(255, 152, 0, 0.1);
                }
                .message-personnalise-header {
                    display: flex;
                    align-items: center;
                    gap: 10px;
                    margin-bottom: 15px;
                    font-weight: 700;
                    font-size: 16px;
                    color: #e65100;
                }
                .message-personnalise-icon {
                    font-size: 24px;
                }
                .message-personnalise-content {
                    white-space: pre-wrap;
                    line-height: 1.8;
                    font-size: 15px;
                    color: #424242;
                    background: white;
                    padding: 20px;
                    border-radius: 6px;
                    border: 1px solid rgba(255, 152, 0, 0.2);
                }
                
                .audit-card {
                    background: linear-gradient(135deg, #f8f9fa 0%%, #ffffff 100%%);
                    border-radius: 10px;
                    padding: 25px;
                    margin: 25px 0;
                    border-left: 5px solid #006666;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.05);
                }
                .audit-card h3 {
                    margin-top: 0;
                    color: #006666;
                    font-size: 20px;
                    display: flex;
                    align-items: center;
                    gap: 10px;
                }
                .info-grid {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr);
                    gap: 15px;
                    margin-top: 20px;
                }
                .info-item {
                    padding: 12px;
                    background: white;
                    border-radius: 6px;
                    border: 1px solid #e9ecef;
                }
                .info-label {
                    font-size: 11px;
                    text-transform: uppercase;
                    letter-spacing: 0.5px;
                    color: #6c757d;
                    font-weight: 600;
                    margin-bottom: 5px;
                }
                .info-value {
                    font-size: 15px;
                    color: #212529;
                    font-weight: 600;
                }
                .stats-row {
                    display: grid;
                    grid-template-columns: repeat(4, 1fr);
                    gap: 10px;
                    margin-top: 15px;
                }
                .stat-box {
                    text-align: center;
                    padding: 15px;
                    background: white;
                    border-radius: 8px;
                    border: 2px solid #e9ecef;
                }
                .stat-number {
                    font-size: 24px;
                    font-weight: 700;
                    color: #006666;
                    display: block;
                }
                .stat-label {
                    font-size: 11px;
                    color: #6c757d;
                    text-transform: uppercase;
                    margin-top: 5px;
                }
                .info-box {
                    background: linear-gradient(135deg, #e7f3ff 0%%, #f0f8ff 100%%);
                    border-left: 4px solid #0d6efd;
                    padding: 18px;
                    border-radius: 6px;
                    margin: 25px 0;
                    display: flex;
                    align-items: start;
                    gap: 12px;
                }
                .info-box-icon {
                    font-size: 24px;
                    color: #0d6efd;
                    flex-shrink: 0;
                }
                .info-box-content {
                    flex: 1;
                }
                .info-box strong {
                    color: #0d6efd;
                    display: block;
                    margin-bottom: 5px;
                }
                .footer {
                    background: linear-gradient(135deg, #2c3e50 0%%, #34495e 100%%);
                    color: white;
                    padding: 30px;
                    text-align: center;
                }
                .footer-logo {
                    font-size: 32px;
                    margin-bottom: 10px;
                }
                .footer h4 {
                    margin: 0 0 5px;
                    font-size: 18px;
                    font-weight: 600;
                }
                .footer p {
                    margin: 5px 0;
                    font-size: 13px;
                    opacity: 0.9;
                }
                .divider {
                    height: 1px;
                    background: rgba(255,255,255,0.2);
                    margin: 20px 0;
                }
                @media only screen and (max-width: 600px) {
                    .info-grid {
                        grid-template-columns: 1fr;
                    }
                    .stats-row {
                        grid-template-columns: repeat(2, 1fr);
                    }
                }
            </style>
        </head>
        <body>
            <div class="email-container">
                <div class="header">
                    <div class="header-icon">🔥</div>
                    <h1>FireSafety Audit</h1>
                    <p>Rapport d'Audit de Sécurité Incendie</p>
                </div>
                
                <div class="content">
                    <div class="greeting">
                        Bonjour,
                    </div>
                    
                    <!-- MESSAGE PERSONNALISÉ EN PREMIER -->
                    <div class="message-personnalise">
                        <div class="message-personnalise-header">
                            <span class="message-personnalise-icon">✉️</span>
                            <span>Message de votre auditeur</span>
                        </div>
                        <div class="message-personnalise-content">%s</div>
                    </div>
                    
                    <div class="audit-card">
                        <h3>📋 Informations de l'Audit</h3>
                        
                        <div class="info-grid">
                            <div class="info-item">
                                <div class="info-label">🏢 Établissement</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">📅 Date d'audit</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">👤 Auditeur</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">📊 Statut</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">🏷️ Type ERP</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">📜 Norme appliquée</div>
                                <div class="info-value">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">📍 Adresse</div>
                                <div class="info-value" style="font-size: 13px;">%s</div>
                            </div>
                            <div class="info-item">
                                <div class="info-label">✅ Taux conformité</div>
                                <div class="info-value" style="color: #28a745;">%s</div>
                            </div>
                        </div>
                        
                        <div class="stats-row">
                            <div class="stat-box">
                                <span class="stat-number" style="color: #28a745;">%d</span>
                                <div class="stat-label">Conformes</div>
                            </div>
                            <div class="stat-box">
                                <span class="stat-number" style="color: #dc3545;">%d</span>
                                <div class="stat-label">Non conformes</div>
                            </div>
                            <div class="stat-box">
                                <span class="stat-number" style="color: #ffc107;">%d</span>
                                <div class="stat-label">Partiels</div>
                            </div>
                            <div class="stat-box">
                                <span class="stat-number" style="color: #6c757d;">%d</span>
                                <div class="stat-label">N/A</div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="info-box">
                        <div class="info-box-icon">📎</div>
                        <div class="info-box-content">
                            <strong>Pièce jointe</strong>
                            Le rapport complet est joint à cet email au format PDF. 
                            Il contient l'analyse détaillée de tous les critères évalués, 
                            les photos prises lors de l'audit, et nos recommandations.
                        </div>
                    </div>
                </div>
                
                <div class="footer">
                    <div class="footer-logo">🔥</div>
                    <h4>FireSafety Audit</h4>
                    <p>Plateforme de gestion des audits de sécurité incendie</p>
                    <div class="divider"></div>
                    <p style="font-size: 12px; opacity: 0.8;">
                        Cet email a été envoyé automatiquement. Merci de ne pas y répondre.<br>
                        Pour toute question, contactez votre auditeur référent.
                    </p>
                    <p style="margin-top: 15px; font-size: 11px;">
                        © 2026 FireSafety Audit - Tous droits réservés
                    </p>
                </div>
            </div>
        </body>
        </html>
        """,
                messageContent,  // MESSAGE PERSONNALISÉ EN PREMIER
                etablissementNom,
                dateAudit,
                auditeurNom,
                getStatutLabel(audit.getStatut()),
                typeEtab,
                normeCode,
                adresseEtab,
                tauxConformite,
                audit.getNbConformes() != null ? audit.getNbConformes() : 0,
                audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0,
                audit.getNbPartiels() != null ? audit.getNbPartiels() : 0,
                audit.getNbNonApplicables() != null ? audit.getNbNonApplicables() : 0
        );
    }

    private String getStatutLabel(StatutAudit statut) {
        if (statut == null) {
            return "<span class='status-badge status-planifie'>N/A</span>";
        }

        return switch (statut) {
            case VALIDE -> "<span class='status-badge status-valide'>✅ Validé</span>";
            case TERMINE -> "<span class='status-badge status-termine'>⏱️ Terminé</span>";
            case EN_COURS -> "<span class='status-badge status-en-cours'>🔄 En cours</span>";
            case PLANIFIE -> "<span class='status-badge status-planifie'>📅 Planifié</span>";
            default -> "<span class='status-badge status-planifie'>N/A</span>";
        };
    }

    private String buildPasswordResetEmailContent(String nomComplet, String resetLink) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background-color: #f5f7fb;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 10px;
                        overflow: hidden;
                        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                    }
                    .header {
                        background: linear-gradient(135deg, #006666, #008080);
                        color: white;
                        padding: 30px;
                        text-align: center;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 24px;
                    }
                    .content {
                        padding: 30px;
                    }
                    .content p {
                        line-height: 1.6;
                        color: #333;
                        margin: 15px 0;
                    }
                    .button-container {
                        text-align: center;
                        margin: 30px 0;
                    }
                    .reset-button {
                        display: inline-block;
                        background: linear-gradient(135deg, #006666, #008080);
                        color: white;
                        padding: 15px 40px;
                        text-decoration: none;
                        border-radius: 8px;
                        font-weight: 600;
                        font-size: 16px;
                    }
                    .reset-button:hover {
                        opacity: 0.9;
                    }
                    .footer {
                        background: #f8f9fa;
                        padding: 20px;
                        text-align: center;
                        color: #666;
                        font-size: 14px;
                    }
                    .warning {
                        background: #fff3cd;
                        border-left: 4px solid #ffc107;
                        padding: 15px;
                        margin: 20px 0;
                        color: #856404;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🔥 FireSafety Audit</h1>
                    </div>
                    <div class="content">
                        <h2>Bonjour %s,</h2>
                        <p>Vous avez demandé la réinitialisation de votre mot de passe.</p>
                        <p>Cliquez sur le bouton ci-dessous pour créer un nouveau mot de passe :</p>
                        
                        <div class="button-container">
                            <a href="%s" class="reset-button">Réinitialiser mon mot de passe</a>
                        </div>
                        
                        <div class="warning">
                            <strong>⚠️ Important :</strong>
                            <ul style="margin: 10px 0;">
                                <li>Ce lien est valable pendant 24 heures</li>
                                <li>Si vous n'avez pas demandé cette réinitialisation, ignorez cet email</li>
                                <li>Ne partagez jamais ce lien avec personne</li>
                            </ul>
                        </div>
                        
                        <p style="margin-top: 30px; font-size: 14px; color: #666;">
                            Si le bouton ne fonctionne pas, copiez et collez ce lien dans votre navigateur :<br>
                            <a href="%s" style="color: #006666; word-break: break-all;">%s</a>
                        </p>
                    </div>
                    <div class="footer">
                        <p>Cet email a été envoyé automatiquement, merci de ne pas y répondre.</p>
                        <p>© 2026 FireSafety Audit - Tous droits réservés</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nomComplet, resetLink, resetLink, resetLink);
    }
}