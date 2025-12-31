package org.xproce.firesafe_audit.dto.etablissement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtablissementSummaryDTO {

    private Long id;

    private String nom;

    private TypeEtablissement type;

    private String ville;

    private Boolean actif;
}