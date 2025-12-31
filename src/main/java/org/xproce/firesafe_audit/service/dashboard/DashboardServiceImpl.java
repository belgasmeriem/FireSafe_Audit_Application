package org.xproce.firesafe_audit.service.dashboard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xproce.firesafe_audit.dao.entities.Etablissement;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.repositories.AuditRepository;
import org.xproce.firesafe_audit.dao.repositories.EtablissementRepository;
import org.xproce.firesafe_audit.dao.repositories.EvaluationCritereRepository;
import org.xproce.firesafe_audit.dto.dashboard.DashboardDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final EtablissementRepository etablissementRepository;
    private final AuditRepository auditRepository;
    private final EvaluationCritereRepository evaluationRepository;

    @Override
    public DashboardDTO getDashboardData() {
        return DashboardDTO.builder()
                .totalEtablissements(etablissementRepository.countActiveEtablissements())
                .totalAudits((long) auditRepository.findAll().size())
                .auditsEnCours(auditRepository.countByStatut(StatutAudit.EN_COURS))
                .auditsTermines(auditRepository.countByStatut(StatutAudit.TERMINE))
                .auditsValides(auditRepository.countByStatut(StatutAudit.VALIDE))
                .tauxMoyenConformite(auditRepository.findTauxMoyenGlobal())
                .nonConformitesCritiques(countNonConformitesCritiques())
                .actionsNonCorrigees((long) evaluationRepository.findActionsNonCorrigees().size())
                .evolutionMensuelle(getEvolutionMensuelle(LocalDate.now().minusMonths(12)))
                .repartitionParType(getRepartitionParType())
                .topEtablissements(getTopEtablissements(10))
                .etablissementsARisque(getEtablissementsARisque(70.0))
                .build();
    }

    @Override
    public List<Map<String, Object>> getEvolutionMensuelle(LocalDate dateDebut) {
        List<Object[]> results = auditRepository.findEvolutionTauxMensuel(dateDebut);

        List<Map<String, Object>> evolution = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("mois", result[0]);
            data.put("taux", result[1]);
            evolution.add(data);
        }

        return evolution;
    }

    @Override
    public List<Map<String, Object>> getRepartitionParType() {
        List<Object[]> results = etablissementRepository.countByTypeGrouped();

        List<Map<String, Object>> repartition = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("type", result[0].toString());
            data.put("count", result[1]);
            repartition.add(data);
        }

        return repartition;
    }

    @Override
    public List<Map<String, Object>> getTopEtablissements(int limit) {
        List<Object[]> results = auditRepository.findTopEtablissementsByTaux();

        List<Map<String, Object>> top = new ArrayList<>();
        int count = 0;
        for (Object[] result : results) {
            if (count >= limit) break;

            Map<String, Object> data = new HashMap<>();
            data.put("etablissement", ((Etablissement) result[0]).getNom());
            data.put("taux", result[1]);
            top.add(data);
            count++;
        }

        return top;
    }

    @Override
    public List<Map<String, Object>> getEtablissementsARisque(Double seuilTaux) {
        List<Object[]> results = auditRepository.findEtablissementsARisque(seuilTaux);

        List<Map<String, Object>> risques = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> data = new HashMap<>();
            data.put("etablissement", ((Etablissement) result[0]).getNom());
            data.put("taux", result[1]);
            data.put("type", ((Etablissement) result[0]).getType().toString());
            risques.add(data);
        }

        return risques;
    }

    private long countNonConformitesCritiques() {
        try {
            List<?> critiques = evaluationRepository.findNonConformitesCritiquesByAudit(null);
            return critiques != null ? critiques.size() : 0L;
        } catch (Exception e) {
            return 0L;
        }
    }
}