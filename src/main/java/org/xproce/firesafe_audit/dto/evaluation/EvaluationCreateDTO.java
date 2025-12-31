package org.xproce.firesafe_audit.dto.evaluation;

import jakarta.validation.constraints.NotNull;
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
public class EvaluationCreateDTO {

    @NotNull(message = "Le statut est obligatoire")
    private StatutConformite statut;

    private String observation;

    private NiveauUrgence urgence;

    private String actionCorrective;

    private String responsableAction;

    private LocalDate dateEcheance;

    private Double coutEstime;

    @NotNull(message = "L'audit est obligatoire")
    private Long auditId;

    @NotNull(message = "Le critère est obligatoire")
    private Long critereId;
}