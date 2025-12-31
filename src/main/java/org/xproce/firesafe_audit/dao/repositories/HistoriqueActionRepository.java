package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.HistoriqueAction;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueActionRepository extends JpaRepository<HistoriqueAction, Long> {

    List<HistoriqueAction> findByUtilisateurId(Long utilisateurId);

    List<HistoriqueAction> findByUtilisateurIdOrderByDateActionDesc(Long utilisateurId);

    List<HistoriqueAction> findByAction(String action);

    List<HistoriqueAction> findByEntite(String entite);

    List<HistoriqueAction> findByEntiteAndEntiteId(String entite, Long entiteId);

    @Query("SELECT h FROM HistoriqueAction h WHERE h.dateAction BETWEEN :debut AND :fin ORDER BY h.dateAction DESC")
    List<HistoriqueAction> findByPeriod(@Param("debut") LocalDateTime debut,
                                        @Param("fin") LocalDateTime fin);

    @Query("SELECT h FROM HistoriqueAction h WHERE h.utilisateur.id = :utilisateurId " +
            "AND h.dateAction BETWEEN :debut AND :fin ORDER BY h.dateAction DESC")
    List<HistoriqueAction> findByUserAndPeriod(@Param("utilisateurId") Long utilisateurId,
                                               @Param("debut") LocalDateTime debut,
                                               @Param("fin") LocalDateTime fin);

    @Query("SELECT h.action, COUNT(h) FROM HistoriqueAction h GROUP BY h.action ORDER BY COUNT(h) DESC")
    List<Object[]> countByActionGrouped();

    @Query("SELECT h.entite, COUNT(h) FROM HistoriqueAction h GROUP BY h.entite ORDER BY COUNT(h) DESC")
    List<Object[]> countByEntiteGrouped();

    @Query("SELECT COUNT(h) FROM HistoriqueAction h WHERE h.utilisateur.id = :utilisateurId")
    long countByUser(@Param("utilisateurId") Long utilisateurId);

    void deleteByDateActionBefore(LocalDateTime date);
}