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

    private String adresse;

    private String ville;

    private String codePostal;

    private Boolean actif;

    public String getAdresseComplete() {
        StringBuilder sb = new StringBuilder();

        if (adresse != null && !adresse.isEmpty()) {
            sb.append(adresse);
        }

        if (ville != null && !ville.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(ville);
        }

        if (codePostal != null && !codePostal.isEmpty()) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(codePostal);
        }

        return sb.length() > 0 ? sb.toString() : "Adresse non renseignée";
    }
}