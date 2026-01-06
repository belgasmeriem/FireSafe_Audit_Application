package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditUpdateDTO {

    private Long id;

    @NotNull(message = "Le type est requis")
    private TypeAudit type;

    @NotNull(message = "La date est requise")
    private LocalDate dateAudit;

    @NotNull(message = "L'établissement est requis")
    private Long etablissementId;

    @NotNull(message = "L'auditeur est requis")
    private Long auditeurId;

    @Positive(message = "La durée doit être positive")
    private Integer dureeEstimee;

    private String observationGenerale;

    private StatutAudit statut;

    private Integer dureeReelle;
}