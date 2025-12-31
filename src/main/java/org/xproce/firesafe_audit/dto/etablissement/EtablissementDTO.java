package org.xproce.firesafe_audit.dto.etablissement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtablissementDTO {

    private Long id;

    private String nom;

    private String description;

    private TypeEtablissement type;

    private String adresse;

    private String ville;

    private String codePostal;

    private String pays;

    private Integer capaciteAccueil;

    private Integer nombreEtages;

    private Double surfaceTotale;

    private String responsableNom;

    private String responsableEmail;

    private String responsableTelephone;

    private Long normeId;

    private String normeNom;

    private Boolean actif;

    private LocalDateTime dateCreation;

    private String adresseComplete;

    private Integer nombreAudits;

    private Double tauxMoyenConformite;
}