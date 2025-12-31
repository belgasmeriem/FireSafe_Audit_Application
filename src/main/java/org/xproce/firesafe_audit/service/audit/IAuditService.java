package org.xproce.firesafe_audit.service.audit;

import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dto.audit.*;

import java.time.LocalDate;
import java.util.List;

public interface IAuditService {
    List<AuditDTO> getAllAudits();
    AuditDTO getAuditById(Long id);
    List<AuditDTO> getAuditsByEtablissement(Long etablissementId);
    List<AuditDTO> getAuditsByAuditeur(Long auditeurId);
    List<AuditDTO> getAuditsByStatut(StatutAudit statut);
    List<AuditDTO> getAuditsEnAttenteValidation();
    List<AuditDTO> getAuditsByPeriod(LocalDate debut, LocalDate fin);
    AuditDTO createAudit(AuditCreateDTO dto);
    AuditDTO updateAudit(Long id, AuditUpdateDTO dto);
    AuditDTO terminerAudit(Long id);
    AuditDTO validerAudit(Long id, Long validateurId);
    void deleteAudit(Long id);
    AuditStatisticsDTO getStatistics();
    Double getTauxMoyenByEtablissement(Long etablissementId);
}