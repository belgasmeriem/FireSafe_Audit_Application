package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.EvaluationCritere;
import org.xproce.firesafe_audit.dao.enums.NiveauUrgence;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationCritereRepository extends JpaRepository<EvaluationCritere, Long> {

    List<EvaluationCritere> findByAudit_Id(Long auditId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.id = :auditId ORDER BY e.critere.code ASC")
    List<EvaluationCritere> findByAuditIdOrderByCritereCodeAsc(@Param("auditId") Long auditId);

    long countByAudit_Id(Long auditId);

    Optional<EvaluationCritere> findByCritereIdAndAuditId(Long critereId, Long auditId);

    @Query("SELECT e.critere.id FROM EvaluationCritere e " +
            "WHERE e.audit.id = :auditId " +
            "AND e.statut IN ('NON_CONFORME', 'PARTIELLEMENT_CONFORME')")
    List<Long> findNonConformeCritereIdsByAuditId(@Param("auditId") Long auditId);

    List<EvaluationCritere> findByAudit_IdAndStatut(Long auditId, StatutConformite statut);

    Optional<EvaluationCritere> findByAudit_IdAndCritere_Id(Long auditId, Long critereId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.id = :auditId " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME')")
    List<EvaluationCritere> findNonConformitesByAudit(@Param("auditId") Long auditId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.id = :auditId " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME') " +
            "AND e.critere.criticite = 'CRITIQUE'")
    List<EvaluationCritere> findNonConformitesCritiquesByAudit(@Param("auditId") Long auditId);

    @Query("SELECT e FROM EvaluationCritere e WHERE " +
            "(e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME') " +
            "AND e.corrigee = false")
    List<EvaluationCritere> findActionsNonCorrigees();

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.etablissement.id = :etablissementId " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME') " +
            "AND e.corrigee = false")
    List<EvaluationCritere> findActionsNonCorrigeesByEtablissement(@Param("etablissementId") Long etablissementId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.statut IN :statuts AND e.corrigee = false")
    List<EvaluationCritere> findNonConformitesNonCorrigees(@Param("statuts") List<StatutConformite> statuts);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.etablissement.id = :etablissementId AND e.statut IN :statuts AND e.corrigee = false")
    List<EvaluationCritere> findNonConformitesNonCorrigeesByEtablissement(
            @Param("etablissementId") Long etablissementId,
            @Param("statuts") List<StatutConformite> statuts
    );

    List<EvaluationCritere> findByAudit_IdAndUrgence(Long auditId, NiveauUrgence urgence);
}