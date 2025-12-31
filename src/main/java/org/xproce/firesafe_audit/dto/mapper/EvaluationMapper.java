package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.EvaluationCritere;
import org.xproce.firesafe_audit.dto.evaluation.EvaluationCritereDTO;

import java.util.stream.Collectors;

@Component
public class EvaluationMapper {

    public EvaluationCritereDTO toDTO(EvaluationCritere evaluation) {
        if (evaluation == null) return null;

        return EvaluationCritereDTO.builder()
                .id(evaluation.getId())
                .statut(evaluation.getStatut())
                .observation(evaluation.getObservation())
                .dateEvaluation(evaluation.getDateEvaluation())
                .urgence(evaluation.getUrgence())
                .actionCorrective(evaluation.getActionCorrective())
                .responsableAction(evaluation.getResponsableAction())
                .dateEcheance(evaluation.getDateEcheance())
                .coutEstime(evaluation.getCoutEstime())
                .corrigee(evaluation.getCorrigee())
                .dateCorrectionSignalee(evaluation.getDateCorrectionSignalee())
                .auditId(evaluation.getAudit() != null ? evaluation.getAudit().getId() : null)
                .preuvesUrls(evaluation.getPreuves() != null ?
                        evaluation.getPreuves().stream()
                                .map(p -> "/api/preuves/" + p.getId() + "/download")
                                .collect(Collectors.toList()) : null)
                .build();
    }
}