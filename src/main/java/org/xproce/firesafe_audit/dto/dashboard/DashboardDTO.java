package org.xproce.firesafe_audit.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {

    private Long totalEtablissements;

    private Long totalAudits;

    private Long auditsEnCours;

    private Long auditsTermines;

    private Long auditsValides;

    private Double tauxMoyenConformite;

    private Long nonConformitesCritiques;

    private Long actionsNonCorrigees;

    private List<Map<String, Object>> evolutionMensuelle;

    private List<Map<String, Object>> repartitionParType;

    private List<Map<String, Object>> topEtablissements;

    private List<Map<String, Object>> etablissementsARisque;
}