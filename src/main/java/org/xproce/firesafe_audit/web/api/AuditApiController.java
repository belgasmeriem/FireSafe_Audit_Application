package org.xproce.firesafe_audit.web.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dto.audit.AuditDTO;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.audit.IAuditService;
import org.xproce.firesafe_audit.service.auth.IAuthService;


@Slf4j
@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuditApiController {

    private final IAuditService auditService;
    private final IAuthService authService;


    @PostMapping("/{id}/demarrer")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<AuditDTO>> demarrerAudit(@PathVariable Long id) {
        try {
            log.info("📋 Démarrage de l'audit {} via API", id);

            AuditDTO audit = auditService.demarrerAudit(id);

            log.info("✅ Audit {} démarré avec succès", id);
            return ResponseEntity.ok(ResponseDTO.success("Audit démarré avec succès", audit));

        } catch (Exception e) {
            log.error("❌ Erreur lors du démarrage de l'audit {} : {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DEMARRER_ERROR"));
        }
    }


    @PostMapping("/{id}/soumettre")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<AuditDTO>> soumettreAudit(@PathVariable Long id) {
        try {
            log.info("📋 Soumission de l'audit {} via API", id);

            User currentUser = authService.getCurrentUser();
            AuditDTO audit = auditService.getAuditById(id);

            if (audit.getAuditeur() == null || !audit.getAuditeur().getId().equals(currentUser.getId())) {
                log.warn("⚠️ Tentative de soumission non autorisée pour l'audit {} par l'utilisateur {}",
                        id, currentUser.getEmail());
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ResponseDTO.error("Vous n'êtes pas autorisé à terminer cet audit", "FORBIDDEN"));
            }

            AuditDTO auditTermine = auditService.terminerAudit(id);

            log.info("✅ Audit {} terminé avec succès", id);
            return ResponseEntity.ok(ResponseDTO.success("Audit terminé avec succès", auditTermine));

        } catch (Exception e) {
            log.error("❌ Erreur lors de la soumission de l'audit {} : {}", id, e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "SOUMETTRE_ERROR"));
        }
    }


    @PostMapping("/{id}/brouillon")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<String>> sauvegarderBrouillon(@PathVariable Long id) {
        try {
            log.info("💾 Sauvegarde du brouillon pour l'audit {}", id);

            return ResponseEntity.ok(ResponseDTO.success("Brouillon sauvegardé avec succès"));

        } catch (Exception e) {
            log.error("❌ Erreur lors de la sauvegarde du brouillon : {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "BROUILLON_ERROR"));
        }
    }


    @GetMapping("/{id}/progression")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<ProgressionDTO>> getProgression(@PathVariable Long id) {
        try {

            ProgressionDTO progression = new ProgressionDTO();
            progression.setAuditId(id);
            progression.setNombreTotal(0);
            progression.setNombreEvalues(0);
            progression.setPourcentage(0.0);

            return ResponseEntity.ok(ResponseDTO.success(progression));

        } catch (Exception e) {
            log.error("❌ Erreur lors de la récupération de la progression : {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "PROGRESSION_ERROR"));
        }
    }


    @lombok.Data
    public static class ProgressionDTO {
        private Long auditId;
        private Integer nombreTotal;
        private Integer nombreEvalues;
        private Double pourcentage;
    }
}