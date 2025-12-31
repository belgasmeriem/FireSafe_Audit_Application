package org.xproce.firesafe_audit.service.audit;

import org.xproce.firesafe_audit.dto.common.CommentaireDTO;

import java.util.List;

public interface ICommentaireService {
    List<CommentaireDTO> getCommentairesByAudit(Long auditId);
    CommentaireDTO getCommentaireById(Long id);
    CommentaireDTO createCommentaire(Long auditId, Long auteurId, String contenu, Long parentId);
    CommentaireDTO updateCommentaire(Long id, String contenu);
    void deleteCommentaire(Long id);
    long countByAudit(Long auditId);
}