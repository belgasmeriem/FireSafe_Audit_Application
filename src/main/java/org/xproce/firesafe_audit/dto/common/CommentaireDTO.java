package org.xproce.firesafe_audit.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dto.user.UserSummaryDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentaireDTO {

    private Long id;

    private String contenu;

    private LocalDateTime dateCreation;

    private UserSummaryDTO auteur;

    private Long auditId;

    private Long parentId;

    private List<CommentaireDTO> reponses;

    private Integer nombreReponses;
}