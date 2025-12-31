package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Commentaire;
import org.xproce.firesafe_audit.dto.common.CommentaireDTO;

import java.util.stream.Collectors;

@Component
public class CommentaireMapper {

    @Autowired
    private UserMapper userMapper;

    public CommentaireDTO toDTO(Commentaire commentaire) {
        if (commentaire == null) return null;

        return CommentaireDTO.builder()
                .id(commentaire.getId())
                .contenu(commentaire.getContenu())
                .dateCreation(commentaire.getDateCreation())
                .auteur(userMapper.toSummaryDTO(commentaire.getAuteur()))
                .auditId(commentaire.getAudit() != null ? commentaire.getAudit().getId() : null)
                .parentId(commentaire.getParent() != null ? commentaire.getParent().getId() : null)
                .nombreReponses(commentaire.getNombreReponses())
                .reponses(commentaire.getReponses() != null ?
                        commentaire.getReponses().stream()
                                .map(this::toDTO)
                                .collect(Collectors.toList()) : null)
                .build();
    }
}