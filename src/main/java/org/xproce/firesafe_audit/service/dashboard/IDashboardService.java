package org.xproce.firesafe_audit.service.dashboard;

import org.xproce.firesafe_audit.dto.dashboard.DashboardDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IDashboardService {
    DashboardDTO getDashboardData();
    List<Map<String, Object>> getEvolutionMensuelle(LocalDate dateDebut);
    List<Map<String, Object>> getRepartitionParType();
    List<Map<String, Object>> getTopEtablissements(int limit);
    List<Map<String, Object>> getEtablissementsARisque(Double seuilTaux);
}