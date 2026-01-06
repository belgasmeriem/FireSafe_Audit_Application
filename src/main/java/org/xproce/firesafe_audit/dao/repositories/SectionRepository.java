package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Section;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByNorme_IdOrderByOrdreAsc(Long normeId);

    Optional<Section> findByCode(String code);

    long countByNorme_Id(Long normeId);

    List<Section> findByNorme_Id(Long normeId);

    boolean existsByCode(String code);

    @Query("SELECT s FROM Section s WHERE LOWER(s.titre) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(s.code) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Section> searchSections(@Param("search") String search);

    @Query("SELECT MAX(s.ordre) FROM Section s WHERE s.norme.id = :normeId")
    Integer findMaxOrdreByNorme(@Param("normeId") Long normeId);
    Optional<Section> findByNormeIdAndCode(Long normeId, String code);
}