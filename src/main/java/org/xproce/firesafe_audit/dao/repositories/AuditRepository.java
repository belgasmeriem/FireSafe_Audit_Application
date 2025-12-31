package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findByEtablissementId(Long etablissementId);

    List<Audit> findByEtablissementIdOrderByDateAuditDesc(Long etablissementId);

    List<Audit> findByAuditeurId(Long auditeurId);

    List<Audit> findByAuditeurIdOrderByDateAuditDesc(Long auditeurId);

    List<Audit> findByStatut(StatutAudit statut);

    List<Audit> findByType(TypeAudit type);

    List<Audit> findByDateAuditBetween(LocalDate debut, LocalDate fin);

    List<Audit> findByStatutAndAuditeurId(StatutAudit statut, Long auditeurId);

    @Query("SELECT a FROM Audit a WHERE a.statut = :statut ORDER BY a.dateAudit DESC")
    List<Audit> findByStatutOrdered(@Param("statut") StatutAudit statut);

    @Query("SELECT a FROM Audit a WHERE a.etablissement.id = :etablissementId " +
            "AND a.dateAudit BETWEEN :debut AND :fin ORDER BY a.dateAudit DESC")
    List<Audit> findByEtablissementAndPeriod(@Param("etablissementId") Long etablissementId,
                                             @Param("debut") LocalDate debut,
                                             @Param("fin") LocalDate fin);

    @Query("SELECT a FROM Audit a WHERE a.statut = 'TERMINE' ORDER BY a.dateModification DESC")
    List<Audit> findAuditsEnAttenteValidation();

    @Query("SELECT AVG(a.tauxConformite) FROM Audit a WHERE a.statut = 'VALIDE'")
    Double findTauxMoyenGlobal();

    @Query("SELECT AVG(a.tauxConformite) FROM Audit a WHERE a.etablissement.id = :etablissementId AND a.statut = 'VALIDE'")
    Double findTauxMoyenByEtablissement(@Param("etablissementId") Long etablissementId);

    @Query("SELECT a FROM Audit a WHERE a.etablissement.id = :etablissementId " +
            "AND a.statut = 'VALIDE' ORDER BY a.dateAudit DESC")
    List<Audit> findValidatedByEtablissement(@Param("etablissementId") Long etablissementId);

    @Query("SELECT COUNT(a) FROM Audit a WHERE a.statut = :statut")
    long countByStatut(@Param("statut") StatutAudit statut);

    @Query("SELECT COUNT(a) FROM Audit a WHERE a.dateAudit BETWEEN :debut AND :fin")
    long countByPeriod(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    @Query("SELECT a.type, COUNT(a) FROM Audit a WHERE a.statut = 'VALIDE' GROUP BY a.type")
    List<Object[]> countByTypeGrouped();

    @Query("SELECT e.type, AVG(a.tauxConformite) FROM Audit a JOIN a.etablissement e " +
            "WHERE a.statut = 'VALIDE' GROUP BY e.type")
    List<Object[]> avgTauxByTypeEtablissement();

    @Query("SELECT DATE_FORMAT(a.dateAudit, '%Y-%m'), AVG(a.tauxConformite) " +
            "FROM Audit a WHERE a.statut = 'VALIDE' " +
            "AND a.dateAudit >= :dateDebut " +
            "GROUP BY DATE_FORMAT(a.dateAudit, '%Y-%m') " +
            "ORDER BY DATE_FORMAT(a.dateAudit, '%Y-%m')")
    List<Object[]> findEvolutionTauxMensuel(@Param("dateDebut") LocalDate dateDebut);

    @Query("SELECT a.etablissement, AVG(a.tauxConformite) FROM Audit a " +
            "WHERE a.statut = 'VALIDE' " +
            "GROUP BY a.etablissement " +
            "ORDER BY AVG(a.tauxConformite) DESC")
    List<Object[]> findTopEtablissementsByTaux();

    @Query("SELECT a.etablissement, AVG(a.tauxConformite) FROM Audit a " +
            "WHERE a.statut = 'VALIDE' " +
            "GROUP BY a.etablissement " +
            "HAVING AVG(a.tauxConformite) < :seuil " +
            "ORDER BY AVG(a.tauxConformite) ASC")
    List<Object[]> findEtablissementsARisque(@Param("seuil") Double seuil);
}