package org.xproce.firesafe_audit.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.NiveauUrgence;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationCritereDTO {

    private Long id;

    private StatutConformite statut;

    private String observation;

    private LocalDateTime dateEvaluation;

    private NiveauUrgence urgence;

    private String actionCorrective;

    private String responsableAction;

    private LocalDate dateEcheance;

    private Double coutEstime;

    private Boolean corrigee;

    private LocalDateTime dateCorrectionSignalee;

    private CritereDTO critere;

    private Long auditId;

    private List<String> preuvesUrls;
}