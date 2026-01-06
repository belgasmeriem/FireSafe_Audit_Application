package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatistiquesAuditeurDTO {
    private Integer totalAudits;
    private Double tauxConformiteMoyen;
    private Double dureeMoyenne;
    private Double scorePerformance;

    @Builder.Default
    private List<PerformanceEtablissementDTO> parEtablissement = new ArrayList<>();
}