package org.xproce.firesafe_audit.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.repositories.*;
import org.xproce.firesafe_audit.dto.common.CommentaireDTO;
import org.xproce.firesafe_audit.dto.mapper.CommentaireMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentaireServiceImpl implements ICommentaireService {

    private final CommentaireRepository commentaireRepository;
    private final AuditRepository auditRepository;
    private final UserRepository userRepository;
    private final CommentaireMapper commentaireMapper;

    @Override
    public List<CommentaireDTO> getCommentairesByAudit(Long auditId) {
        return commentaireRepository.findTopLevelCommentsByAudit(auditId).stream()
                .map(commentaireMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentaireDTO getCommentaireById(Long id) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        return commentaireMapper.toDTO(commentaire);
    }

    @Override
    @Transactional
    public CommentaireDTO createCommentaire(Long auditId, Long auteurId, String contenu, Long parentId) {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        User auteur = userRepository.findById(auteurId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Commentaire commentaire = Commentaire.builder()
                .contenu(contenu)
                .audit(audit)
                .auteur(auteur)
                .build();

        if (parentId != null) {
            Commentaire parent = commentaireRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Commentaire parent non trouvé"));
            commentaire.setParent(parent);
        }

        Commentaire saved = commentaireRepository.save(commentaire);
        return commentaireMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public CommentaireDTO updateCommentaire(Long id, String contenu) {
        Commentaire commentaire = commentaireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));

        commentaire.setContenu(contenu);
        Commentaire updated = commentaireRepository.save(commentaire);

        return commentaireMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteCommentaire(Long id) {
        commentaireRepository.deleteById(id);
    }

    @Override
    public long countByAudit(Long auditId) {
        return commentaireRepository.countByAudit(auditId);
    }
}