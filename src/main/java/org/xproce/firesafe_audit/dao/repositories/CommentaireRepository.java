package org.xproce.firesafe_audit.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.xproce.firesafe_audit.dao.entities.Commentaire;

import java.util.List;

@Repository
public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {

    List<Commentaire> findByAuditId(Long auditId);

    List<Commentaire> findByAuditIdOrderByDateCreationDesc(Long auditId);

    List<Commentaire> findByAuteurId(Long auteurId);

    List<Commentaire> findByParentId(Long parentId);

    @Query("SELECT c FROM Commentaire c WHERE c.audit.id = :auditId AND c.parent IS NULL ORDER BY c.dateCreation DESC")
    List<Commentaire> findTopLevelCommentsByAudit(@Param("auditId") Long auditId);

    @Query("SELECT COUNT(c) FROM Commentaire c WHERE c.audit.id = :auditId")
    long countByAudit(@Param("auditId") Long auditId);

    @Query("SELECT COUNT(c) FROM Commentaire c WHERE c.parent.id = :parentId")
    long countReponsesByParentId(@Param("parentId") Long parentId);

    void deleteByAuditId(Long auditId);
}