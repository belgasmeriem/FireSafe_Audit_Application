package org.xproce.firesafe_audit.dto.norme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dao.enums.Criticite;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CritereDTO {

    private Long id;

    private String code;

    private String libelle;

    private String description;

    private Categorie categorie;

    private Criticite criticite;

    private Integer ponderation;

    private Boolean obligatoire;

    private String preuvesRequises;

    private String referenceTexteLoi;

    private Long normeId;

    private String normeNom;
}