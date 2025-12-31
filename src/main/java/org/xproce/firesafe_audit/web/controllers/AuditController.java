package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dto.audit.*;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.audit.IAuditService;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/audits")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuditController {

    private final IAuditService auditService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAllAudits() {
        List<AuditDTO> audits = auditService.getAllAudits();
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<AuditDTO>> getAuditById(@PathVariable Long id) {
        try {
            AuditDTO audit = auditService.getAuditById(id);
            return ResponseEntity.ok(ResponseDTO.success(audit));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "AUDIT_NOT_FOUND"));
        }
    }

    @GetMapping("/etablissement/{etablissementId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAuditsByEtablissement(@PathVariable Long etablissementId) {
        List<AuditDTO> audits = auditService.getAuditsByEtablissement(etablissementId);
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @GetMapping("/auditeur/{auditeurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAuditsByAuditeur(@PathVariable Long auditeurId) {
        List<AuditDTO> audits = auditService.getAuditsByAuditeur(auditeurId);
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAuditsByStatut(@PathVariable StatutAudit statut) {
        List<AuditDTO> audits = auditService.getAuditsByStatut(statut);
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @GetMapping("/attente-validation")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAuditsEnAttenteValidation() {
        List<AuditDTO> audits = auditService.getAuditsEnAttenteValidation();
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @GetMapping("/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<AuditDTO>>> getAuditsByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        List<AuditDTO> audits = auditService.getAuditsByPeriod(debut, fin);
        return ResponseEntity.ok(ResponseDTO.success(audits));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<AuditDTO>> createAudit(@Valid @RequestBody AuditCreateDTO dto) {
        try {
            AuditDTO audit = auditService.createAudit(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Audit créé avec succès", audit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<AuditDTO>> updateAudit(@PathVariable Long id, @Valid @RequestBody AuditUpdateDTO dto) {
        try {
            AuditDTO audit = auditService.updateAudit(id, dto);
            return ResponseEntity.ok(ResponseDTO.success("Audit mis à jour avec succès", audit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @PutMapping("/{id}/terminer")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<AuditDTO>> terminerAudit(@PathVariable Long id) {
        try {
            AuditDTO audit = auditService.terminerAudit(id);
            return ResponseEntity.ok(ResponseDTO.success("Audit terminé avec succès", audit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "TERMINER_ERROR"));
        }
    }

    @PutMapping("/{id}/valider")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ResponseDTO<AuditDTO>> validerAudit(@PathVariable Long id, @RequestParam Long validateurId) {
        try {
            AuditDTO audit = auditService.validerAudit(id, validateurId);
            return ResponseEntity.ok(ResponseDTO.success("Audit validé avec succès", audit));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "VALIDER_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteAudit(@PathVariable Long id) {
        try {
            auditService.deleteAudit(id);
            return ResponseEntity.ok(ResponseDTO.success("Audit supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<AuditStatisticsDTO>> getStatistics() {
        AuditStatisticsDTO stats = auditService.getStatistics();
        return ResponseEntity.ok(ResponseDTO.success(stats));
    }

    @GetMapping("/etablissement/{etablissementId}/taux-moyen")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<Double>> getTauxMoyenByEtablissement(@PathVariable Long etablissementId) {
        Double taux = auditService.getTauxMoyenByEtablissement(etablissementId);
        return ResponseEntity.ok(ResponseDTO.success(taux));
    }
}