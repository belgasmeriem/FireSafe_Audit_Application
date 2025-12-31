package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Critere;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dao.enums.Criticite;

import java.util.List;
import java.util.Optional;

@Repository
public interface CritereRepository extends JpaRepository<Critere, Long> {

    Optional<Critere> findByCode(String code);

    List<Critere> findByNormeId(Long normeId);

    List<Critere> findByNormeIdOrderByCodeAsc(Long normeId);

    List<Critere> findByCategorie(Categorie categorie);

    List<Critere> findByCriticite(Criticite criticite);

    List<Critere> findByNormeIdAndCategorie(Long normeId, Categorie categorie);

    List<Critere> findByNormeIdAndCriticite(Long normeId, Criticite criticite);

    List<Critere> findByObligatoireTrue();

    List<Critere> findByNormeIdAndObligatoireTrue(Long normeId);

    @Query("SELECT c FROM Critere c WHERE c.norme.id = :normeId AND c.categorie = :categorie ORDER BY c.code ASC")
    List<Critere> findByNormeAndCategorieOrdered(@Param("normeId") Long normeId, @Param("categorie") Categorie categorie);

    @Query("SELECT c FROM Critere c WHERE LOWER(c.libelle) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Critere> searchCriteres(@Param("search") String search);

    @Query("SELECT COUNT(c) FROM Critere c WHERE c.norme.id = :normeId")
    long countByNorme(@Param("normeId") Long normeId);

    @Query("SELECT c.categorie, COUNT(c) FROM Critere c WHERE c.norme.id = :normeId GROUP BY c.categorie")
    List<Object[]> countByCategorieGrouped(@Param("normeId") Long normeId);

    @Query("SELECT c.criticite, COUNT(c) FROM Critere c WHERE c.norme.id = :normeId GROUP BY c.criticite")
    List<Object[]> countByCriticiteGrouped(@Param("normeId") Long normeId);
}