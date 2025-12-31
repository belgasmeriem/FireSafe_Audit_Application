package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditSummaryDTO {

    private Long id;

    private LocalDate dateAudit;

    private TypeAudit type;

    private StatutAudit statut;

    private Double tauxConformite;

    private String etablissementNom;

    private String auditeurNom;
}