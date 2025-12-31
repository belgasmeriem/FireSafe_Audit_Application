package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

import java.util.List;
import java.util.Optional;

@Repository
public interface NormeRepository extends JpaRepository<Norme, Long> {

    Optional<Norme> findByCode(String code);

    boolean existsByCode(String code);

    List<Norme> findByActifTrue();

    List<Norme> findByPays(String pays);

    List<Norme> findByPaysAndActifTrue(String pays);

    @Query("SELECT n FROM Norme n WHERE :type MEMBER OF n.typesEtablissements")
    List<Norme> findByTypeEtablissement(@Param("type") TypeEtablissement type);

    @Query("SELECT n FROM Norme n WHERE :type MEMBER OF n.typesEtablissements AND n.actif = true")
    List<Norme> findActiveByTypeEtablissement(@Param("type") TypeEtablissement type);

    @Query("SELECT n FROM Norme n WHERE LOWER(n.nom) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(n.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Norme> searchNormes(@Param("search") String search);

    @Query("SELECT COUNT(n) FROM Norme n WHERE n.actif = true")
    long countActiveNormes();
}