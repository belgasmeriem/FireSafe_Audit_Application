package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditStatisticsDTO {

    private Long totalAudits;

    private Long auditsEnCours;

    private Long auditsTermines;

    private Long auditsValides;

    private Double tauxMoyenConformite;

    private Long totalNonConformitesCritiques;

    private Long actionsNonCorrigees;
}