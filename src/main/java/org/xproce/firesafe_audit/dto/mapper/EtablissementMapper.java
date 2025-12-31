package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Etablissement;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementDTO;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementSummaryDTO;

@Component
public class EtablissementMapper {

    public EtablissementDTO toDTO(Etablissement etablissement) {
        if (etablissement == null) return null;

        return EtablissementDTO.builder()
                .id(etablissement.getId())
                .nom(etablissement.getNom())
                .description(etablissement.getDescription())
                .type(etablissement.getType())
                .adresse(etablissement.getAdresse())
                .ville(etablissement.getVille())
                .codePostal(etablissement.getCodePostal())
                .pays(etablissement.getPays())
                .capaciteAccueil(etablissement.getCapaciteAccueil())
                .nombreEtages(etablissement.getNombreEtages())
                .surfaceTotale(etablissement.getSurfaceTotale())
                .responsableNom(etablissement.getResponsableNom())
                .responsableEmail(etablissement.getResponsableEmail())
                .responsableTelephone(etablissement.getResponsableTelephone())
                .normeId(etablissement.getNorme() != null ? etablissement.getNorme().getId() : null)
                .normeNom(etablissement.getNorme() != null ? etablissement.getNorme().getNom() : null)
                .actif(etablissement.getActif())
                .dateCreation(etablissement.getDateCreation())
                .adresseComplete(etablissement.getAdresseComplete())
                .nombreAudits(etablissement.getAudits() != null ? etablissement.getAudits().size() : 0)
                .build();
    }

    public EtablissementSummaryDTO toSummaryDTO(Etablissement etablissement) {
        if (etablissement == null) return null;

        return EtablissementSummaryDTO.builder()
                .id(etablissement.getId())
                .nom(etablissement.getNom())
                .type(etablissement.getType())
                .ville(etablissement.getVille())
                .actif(etablissement.getActif())
                .build();
    }
}