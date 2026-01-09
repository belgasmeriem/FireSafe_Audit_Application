package org.xproce.firesafe_audit.web.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.repositories.AuditRepository;
import org.xproce.firesafe_audit.dao.repositories.EtablissementRepository;
import org.xproce.firesafe_audit.dao.repositories.NormeRepository;
import org.xproce.firesafe_audit.dao.repositories.UserRepository;
import org.xproce.firesafe_audit.service.notification.IEmailService;
import org.xproce.firesafe_audit.service.report.IReportService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/rapports")
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;
    private final IEmailService emailService;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;
    private final EtablissementRepository etablissementRepository;
    private final NormeRepository normeRepository;

    @GetMapping
    public String listRapports(
            @RequestParam(required = false) Long auditeur,
            @RequestParam(required = false) Long etablissement,
            @RequestParam(required = false) Long norme,
            @RequestParam(required = false) StatutAudit statut,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        User currentUser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Audit> audits;
        boolean isAuditor = currentUser.isAuditor();

        if (isAuditor) {
            if (statut != null && statut != StatutAudit.TERMINE && statut != StatutAudit.VALIDE) {
                audits = Collections.emptyList();
            } else {
                audits = auditRepository.findCompletedAuditsWithFilters(
                        currentUser.getId(),
                        etablissement,
                        norme,
                        statut
                );
            }
        } else {
            boolean hasFilters = auditeur != null || etablissement != null ||
                    norme != null || statut != null;

            if (hasFilters) {
                if (statut != null && statut != StatutAudit.TERMINE && statut != StatutAudit.VALIDE) {
                    audits = Collections.emptyList();
                } else {
                    audits = auditRepository.findCompletedAuditsWithFilters(
                            auditeur, etablissement, norme, statut
                    );
                }
            } else {
                audits = auditRepository.findAllCompletedAudits();
            }
        }

        model.addAttribute("audits", audits);
        model.addAttribute("auditeurs", userRepository.findAll());
        model.addAttribute("etablissements", etablissementRepository.findAll());
        model.addAttribute("normes", normeRepository.findAll());
        model.addAttribute("isAuditor", isAuditor);
        model.addAttribute("currentUser", currentUser);

        String userInitiales = currentUser.getNom().substring(0, 1).toUpperCase() +
                currentUser.getPrenom().substring(0, 1).toUpperCase();
        model.addAttribute("userInitiales", userInitiales);
        model.addAttribute("currentUserRole", getRoleLibelle(currentUser));
        model.addAttribute("currentUserId", currentUser.getId());

        return "rapports/list";
    }

    @GetMapping("/audit/{auditId}/view")
    public ResponseEntity<byte[]> viewRapport(@PathVariable Long auditId) {
        try {
            byte[] pdfBytes = reportService.generateRapportPDF(auditId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.inline()
                            .filename("rapport_audit_" + auditId + ".pdf")
                            .build()
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/audit/{auditId}/download")
    public ResponseEntity<byte[]> downloadRapport(@PathVariable Long auditId) {
        try {
            byte[] pdfBytes = reportService.generateRapportPDF(auditId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("rapport_audit_" + auditId + ".pdf")
                            .build()
            );

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/audit/send-email")
    @ResponseBody
    public ResponseEntity<String> sendEmailReport(
            @RequestParam Long auditId,
            @RequestParam String recipientEmail,
            @RequestParam String subject,
            @RequestParam String message,
            @RequestParam(required = false) String additionalEmails) {

        try {
            Audit audit = auditRepository.findById(auditId)
                    .orElseThrow(() -> new RuntimeException("Audit non trouvé avec l'ID: " + auditId));

            byte[] pdfBytes = reportService.generateRapportPDF(auditId);

            List<String> ccEmails = new ArrayList<>();
            if (additionalEmails != null && !additionalEmails.isEmpty() && !additionalEmails.equals("[]")) {
                ObjectMapper mapper = new ObjectMapper();
                ccEmails = mapper.readValue(additionalEmails, new TypeReference<List<String>>() {});
            }

            String fileName = "Rapport_Audit_" + audit.getId() + "_" +
                    audit.getEtablissement().getNom().replaceAll("\\s+", "_") + ".pdf";

            emailService.sendEmailWithAttachment(
                    recipientEmail,
                    ccEmails,
                    subject,
                    message,
                    pdfBytes,
                    fileName,
                    audit
            );

            return ResponseEntity.ok("Email envoyé avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
    }

    private String getRoleLibelle(User user) {
        if (user.isAdmin()) {
            return "Administrateur";
        } else if (user.isManager()) {
            return "Manager";
        } else if (user.isAuditor()) {
            return "Auditeur";
        }
        return "Utilisateur";
    }
}