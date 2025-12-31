package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.EvaluationCritere;
import org.xproce.firesafe_audit.dao.enums.NiveauUrgence;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EvaluationCritereRepository extends JpaRepository<EvaluationCritere, Long> {

    List<EvaluationCritere> findByAuditId(Long auditId);

    List<EvaluationCritere> findByAuditIdOrderByCritereCodeAsc(Long auditId);

    List<EvaluationCritere> findByStatut(StatutConformite statut);

    List<EvaluationCritere> findByAuditIdAndStatut(Long auditId, StatutConformite statut);

    List<EvaluationCritere> findByUrgence(NiveauUrgence urgence);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.id = :auditId " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME')")
    List<EvaluationCritere> findNonConformitesByAudit(@Param("auditId") Long auditId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.id = :auditId " +
            "AND e.statut = 'NON_CONFORME' " +
            "AND e.critere.criticite = 'CRITIQUE'")
    List<EvaluationCritere> findNonConformitesCritiquesByAudit(@Param("auditId") Long auditId);

    @Query("SELECT e FROM EvaluationCritere e WHERE e.corrigee = false " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME')")
    List<EvaluationCritere> findActionsNonCorrigees();

    @Query("SELECT e FROM EvaluationCritere e WHERE e.audit.etablissement.id = :etablissementId " +
            "AND e.corrigee = false " +
            "AND (e.statut = 'NON_CONFORME' OR e.statut = 'PARTIELLEMENT_CONFORME')")
    List<EvaluationCritere> findActionsNonCorrigeesByEtablissement(@Param("etablissementId") Long etablissementId);

    @Query("SELECT COUNT(e) FROM EvaluationCritere e WHERE e.audit.id = :auditId AND e.statut = :statut")
    long countByAuditAndStatut(@Param("auditId") Long auditId, @Param("statut") StatutConformite statut);

    @Query("SELECT e.critere.categorie, COUNT(e) FROM EvaluationCritere e " +
            "WHERE e.audit.id = :auditId AND e.statut = 'NON_CONFORME' " +
            "GROUP BY e.critere.categorie")
    List<Object[]> countNonConformitesByCategorie(@Param("auditId") Long auditId);

    @Query("SELECT e.critere, COUNT(e) FROM EvaluationCritere e " +
            "WHERE e.statut = 'NON_CONFORME' " +
            "GROUP BY e.critere " +
            "ORDER BY COUNT(e) DESC")
    List<Object[]> findCriteresMostNonConformes();
}