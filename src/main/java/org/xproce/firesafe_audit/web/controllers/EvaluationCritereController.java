package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.dto.evaluation.*;
import org.xproce.firesafe_audit.service.audit.IEvaluationCritereService;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class EvaluationCritereController {

    private final IEvaluationCritereService evaluationService;

    @GetMapping("/audit/{auditId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EvaluationCritereDTO>>> getEvaluationsByAudit(@PathVariable Long auditId) {
        List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(auditId);
        return ResponseEntity.ok(ResponseDTO.success(evaluations));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<EvaluationCritereDTO>> getEvaluationById(@PathVariable Long id) {
        try {
            EvaluationCritereDTO evaluation = evaluationService.getEvaluationById(id);
            return ResponseEntity.ok(ResponseDTO.success(evaluation));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "EVALUATION_NOT_FOUND"));
        }
    }

    @GetMapping("/audit/{auditId}/non-conformites")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EvaluationCritereDTO>>> getNonConformitesByAudit(@PathVariable Long auditId) {
        List<EvaluationCritereDTO> evaluations = evaluationService.getNonConformitesByAudit(auditId);
        return ResponseEntity.ok(ResponseDTO.success(evaluations));
    }

    @GetMapping("/audit/{auditId}/non-conformites-critiques")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EvaluationCritereDTO>>> getNonConformitesCritiquesByAudit(@PathVariable Long auditId) {
        List<EvaluationCritereDTO> evaluations = evaluationService.getNonConformitesCritiquesByAudit(auditId);
        return ResponseEntity.ok(ResponseDTO.success(evaluations));
    }

    @GetMapping("/actions-non-corrigees")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<EvaluationCritereDTO>>> getActionsNonCorrigees() {
        List<EvaluationCritereDTO> evaluations = evaluationService.getActionsNonCorrigees();
        return ResponseEntity.ok(ResponseDTO.success(evaluations));
    }

    @GetMapping("/etablissement/{etablissementId}/actions-non-corrigees")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<EvaluationCritereDTO>>> getActionsNonCorrigeesByEtablissement(@PathVariable Long etablissementId) {
        List<EvaluationCritereDTO> evaluations = evaluationService.getActionsNonCorrigeesByEtablissement(etablissementId);
        return ResponseEntity.ok(ResponseDTO.success(evaluations));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<EvaluationCritereDTO>> createEvaluation(@Valid @RequestBody EvaluationCreateDTO dto) {
        try {
            EvaluationCritereDTO evaluation = evaluationService.createEvaluation(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Évaluation créée avec succès", evaluation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<EvaluationCritereDTO>> updateEvaluation(
            @PathVariable Long id, @Valid @RequestBody EvaluationUpdateDTO dto) {
        try {
            EvaluationCritereDTO evaluation = evaluationService.updateEvaluation(id, dto);
            return ResponseEntity.ok(ResponseDTO.success("Évaluation mise à jour avec succès", evaluation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @PutMapping("/{id}/signaler-correction")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<String>> signalerCorrection(@PathVariable Long id) {
        try {
            evaluationService.signalerCorrection(id);
            return ResponseEntity.ok(ResponseDTO.success("Correction signalée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "SIGNALER_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteEvaluation(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return ResponseEntity.ok(ResponseDTO.success("Évaluation supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }
}