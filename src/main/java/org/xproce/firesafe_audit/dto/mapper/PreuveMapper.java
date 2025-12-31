package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Preuve;
import org.xproce.firesafe_audit.dto.common.PreuveDTO;

@Component
public class PreuveMapper {

    public PreuveDTO toDTO(Preuve preuve) {
        if (preuve == null) return null;

        return PreuveDTO.builder()
                .id(preuve.getId())
                .nomFichier(preuve.getNomFichier())
                .cheminFichier(preuve.getCheminFichier())
                .typeFichier(preuve.getTypeFichier())
                .tailleFichier(preuve.getTailleFichier())
                .tailleFichierFormatee(preuve.getTailleFichierFormatee())
                .description(preuve.getDescription())
                .dateUpload(preuve.getDateUpload())
                .evaluationId(preuve.getEvaluation() != null ? preuve.getEvaluation().getId() : null)
                .url("/api/preuves/" + preuve.getId() + "/download")
                .build();
    }
}