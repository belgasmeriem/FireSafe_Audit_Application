package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Critere;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;

@Component
public class CritereMapper {

    public CritereDTO toDTO(Critere critere) {
        if (critere == null) {
            return null;
        }

        return CritereDTO.builder()
                .id(critere.getId())
                .code(critere.getCode())
                .libelle(critere.getLibelle())
                .description(critere.getDescription())
                .categorie(critere.getCategorie())
                .criticite(critere.getCriticite())
                .ponderation(critere.getPonderation())
                .obligatoire(critere.getObligatoire())
                .preuvesRequises(critere.getPreuvesRequises())
                .referenceTexteLoi(critere.getReferenceTexteLoi())
                .normeId(critere.getNorme() != null ? critere.getNorme().getId() : null)
                .normeNom(critere.getNorme() != null ? critere.getNorme().getNom() : null)
                .build();
    }

    public Critere toEntity(CritereDTO dto) {
        if (dto == null) {
            return null;
        }

        Critere critere = new Critere();
        critere.setId(dto.getId());
        critere.setCode(dto.getCode());
        critere.setLibelle(dto.getLibelle());
        critere.setDescription(dto.getDescription());
        critere.setCategorie(dto.getCategorie());
        critere.setCriticite(dto.getCriticite());
        critere.setPonderation(dto.getPonderation());
        critere.setObligatoire(dto.getObligatoire());
        critere.setPreuvesRequises(dto.getPreuvesRequises());
        critere.setReferenceTexteLoi(dto.getReferenceTexteLoi());

        return critere;
    }
}