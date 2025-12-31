package org.xproce.firesafe_audit.dto.etablissement;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtablissementUpdateDTO {

    @Size(max = 200)
    private String nom;

    private String description;

    private TypeEtablissement type;

    @Size(max = 255)
    private String adresse;

    @Size(max = 100)
    private String ville;

    @Size(max = 20)
    private String codePostal;

    @Size(max = 100)
    private String pays;

    private Integer capaciteAccueil;

    private Integer nombreEtages;

    private Double surfaceTotale;

    @Size(max = 100)
    private String responsableNom;

    @Size(max = 100)
    private String responsableEmail;

    @Size(max = 20)
    private String responsableTelephone;

    private Long normeId;

    private Boolean actif;
}