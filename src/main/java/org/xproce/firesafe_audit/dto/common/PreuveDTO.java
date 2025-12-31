package org.xproce.firesafe_audit.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeFichier;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreuveDTO {

    private Long id;

    private String nomFichier;

    private String cheminFichier;

    private TypeFichier typeFichier;

    private Long tailleFichier;

    private String tailleFichierFormatee;

    private String description;

    private LocalDateTime dateUpload;

    private Long evaluationId;

    private String url;
}