package org.xproce.firesafe_audit.dto.audit;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditCreateDTO {

    @NotNull(message = "La date d'audit est obligatoire")
    private LocalDate dateAudit;

    @NotNull(message = "Le type d'audit est obligatoire")
    private TypeAudit type;

    @NotNull(message = "L'établissement est obligatoire")
    private Long etablissementId;

    @NotNull(message = "L'auditeur est obligatoire")
    private Long auditeurId;

    private Integer dureeEstimee;

    private String observationGenerale;
}