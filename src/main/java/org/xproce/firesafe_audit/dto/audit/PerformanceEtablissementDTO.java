package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceEtablissementDTO {
    private String etablissementNom;
    private Integer nombreAudits;
    private Double tauxConformite;
    private Double dureeMoyenne;
    private Integer performance;
}