package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Etablissement;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

import java.util.List;

@Repository
public interface EtablissementRepository extends JpaRepository<Etablissement, Long> {

    List<Etablissement> findByActifTrue();

    List<Etablissement> findByType(TypeEtablissement type);

    List<Etablissement> findByTypeAndActifTrue(TypeEtablissement type);

    List<Etablissement> findByVille(String ville);

    List<Etablissement> findByVilleAndActifTrue(String ville);

    List<Etablissement> findByPays(String pays);

    List<Etablissement> findByNormeId(Long normeId);

    @Query("SELECT e FROM Etablissement e WHERE LOWER(e.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.ville) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(e.responsableNom) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Etablissement> searchEtablissements(@Param("search") String search);

    @Query("SELECT COUNT(e) FROM Etablissement e WHERE e.actif = true")
    long countActiveEtablissements();

    @Query("SELECT COUNT(e) FROM Etablissement e WHERE e.type = :type AND e.actif = true")
    long countByType(@Param("type") TypeEtablissement type);

    @Query("SELECT e.type, COUNT(e) FROM Etablissement e WHERE e.actif = true GROUP BY e.type")
    List<Object[]> countByTypeGrouped();

    @Query("SELECT e.ville, COUNT(e) FROM Etablissement e WHERE e.actif = true GROUP BY e.ville ORDER BY COUNT(e) DESC")
    List<Object[]> countByVilleGrouped();
}