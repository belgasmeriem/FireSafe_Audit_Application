package org.xproce.firesafe_audit.dto.evaluation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.NiveauUrgence;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationUpdateDTO {

    private StatutConformite statut;

    private String observation;

    private NiveauUrgence urgence;

    private String actionCorrective;

    private String responsableAction;

    private LocalDate dateEcheance;

    private Double coutEstime;

    private Boolean corrigee;
}