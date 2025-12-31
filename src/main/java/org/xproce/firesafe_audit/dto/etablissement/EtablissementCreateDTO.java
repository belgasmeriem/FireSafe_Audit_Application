package org.xproce.firesafe_audit.dto.etablissement;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EtablissementCreateDTO {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 200)
    private String nom;

    private String description;

    @NotNull(message = "Le type d'établissement est obligatoire")
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

    @NotNull(message = "La norme est obligatoire")
    private Long normeId;
}