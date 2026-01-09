package org.xproce.firesafe_audit.service.audit;

import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;
import org.xproce.firesafe_audit.dto.audit.*;

        import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IAuditService {
    List<AuditDTO> getAllAudits();
    AuditDTO getAuditById(Long id);
    AuditDTO createAudit(AuditCreateDTO dto);
    AuditDTO updateAudit(Long id, AuditUpdateDTO dto);
    AuditDTO updateAuditFull(Long id, AuditUpdateDTO dto);
    void deleteAudit(Long id);
    List<AuditDTO> getAuditsByEtablissement(Long etablissementId);
    List<AuditDTO> getAuditsByAuditeur(Long auditeurId);
    List<AuditDTO> getAuditsByStatut(StatutAudit statut);
    List<AuditDTO> getAuditsByType(TypeAudit type);
    List<AuditDTO> getAuditsByPeriod(LocalDate debut, LocalDate fin);
    List<AuditDTO> getAuditsEnAttenteValidation();
    AuditDTO terminerAudit(Long id);
    AuditDTO validerAudit(Long id, Long validateurId);
    List<AuditDTO> getAuditsByAuditeurAndStatut(Long auditeurId, StatutAudit statut);
    List<AuditDTO> getAuditsByAuditeurAndStatuts(Long auditeurId, List<StatutAudit> statuts);
    List<AuditDTO> getAuditsEnRetard();
    List<AuditDTO> getAuditsCetteSemaine();
    List<AuditDTO> getAuditsAVenir();
    StatistiquesAuditeurDTO getStatistiquesByAuditeur(Long auditeurId);
    AuditStatisticsDTO getStatistics();
    List<Map<String, Object>> getEvolutionMensuelle(int mois);
    Map<String, Long> getRepartitionParStatut();
    Map<String, Long> getRepartitionParType();
    List<Map<String, Object>> getConformiteMensuelle(int mois);
    List<Map<String, Object>> getPerformanceAuditeurs();
    Double getTauxMoyenByEtablissement(Long etablissementId);
    AuditDTO demarrerAudit(Long auditId);
    AuditDTO soumettreAudit(Long auditId);
    Integer calculateProgression(Long auditId);
    Double calculateTauxConformite(Long auditId);
    List<AuditDTO> getAuditsTermines();
    int countAuditsValidesParManagerCeMois(Long managerId);
    int countValidationsCeMois();
}