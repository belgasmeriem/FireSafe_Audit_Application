package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.PhotoEtablissement;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoEtablissementRepository extends JpaRepository<PhotoEtablissement, Long> {

    List<PhotoEtablissement> findByEtablissementId(Long etablissementId);

    List<PhotoEtablissement> findByEtablissementIdOrderByPrincipaleDescDateUploadDesc(Long etablissementId);

    Optional<PhotoEtablissement> findByEtablissementIdAndPrincipaleTrue(Long etablissementId);

    @Query("SELECT p FROM PhotoEtablissement p WHERE p.etablissement.id = :etablissementId ORDER BY p.principale DESC, p.dateUpload DESC")
    List<PhotoEtablissement> findByEtablissementOrdered(@Param("etablissementId") Long etablissementId);

    @Query("SELECT COUNT(p) FROM PhotoEtablissement p WHERE p.etablissement.id = :etablissementId")
    long countByEtablissement(@Param("etablissementId") Long etablissementId);

    void deleteByEtablissementId(Long etablissementId);
}