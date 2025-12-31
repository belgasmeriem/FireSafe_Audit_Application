package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Preuve;
import org.xproce.firesafe_audit.dao.enums.TypeFichier;

import java.util.List;

@Repository
public interface PreuveRepository extends JpaRepository<Preuve, Long> {

    List<Preuve> findByEvaluationId(Long evaluationId);

    List<Preuve> findByEvaluationIdOrderByDateUploadDesc(Long evaluationId);

    List<Preuve> findByTypeFichier(TypeFichier typeFichier);

    @Query("SELECT p FROM Preuve p WHERE p.evaluation.audit.id = :auditId ORDER BY p.dateUpload DESC")
    List<Preuve> findByAudit(@Param("auditId") Long auditId);

    @Query("SELECT COUNT(p) FROM Preuve p WHERE p.evaluation.id = :evaluationId")
    long countByEvaluation(@Param("evaluationId") Long evaluationId);

    @Query("SELECT COUNT(p) FROM Preuve p WHERE p.evaluation.audit.id = :auditId")
    long countByAudit(@Param("auditId") Long auditId);

    @Query("SELECT p.typeFichier, COUNT(p) FROM Preuve p WHERE p.evaluation.audit.id = :auditId GROUP BY p.typeFichier")
    List<Object[]> countByTypeFichierGrouped(@Param("auditId") Long auditId);

    void deleteByEvaluationId(Long evaluationId);
}