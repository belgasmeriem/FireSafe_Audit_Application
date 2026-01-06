package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;


@Component
public class NormeMapper {


    public NormeDTO toDTO(Norme norme) {
        if (norme == null) return null;

        return NormeDTO.builder()
                .id(norme.getId())
                .code(norme.getCode())
                .nom(norme.getNom())
                .description(norme.getDescription())
                .version(norme.getVersion())
                .actif(norme.getActif())
                .dateCreation(norme.getDateCreation())
                .dateModification(norme.getDateModification())
                .build();
    }


    public Norme toEntity(NormeDTO dto) {
        if (dto == null) return null;

        Norme norme = new Norme();
        norme.setId(dto.getId());
        norme.setCode(dto.getCode());
        norme.setNom(dto.getNom());
        norme.setDescription(dto.getDescription());
        norme.setVersion(dto.getVersion());
        norme.setActif(dto.getActif());

        return norme;
    }
}