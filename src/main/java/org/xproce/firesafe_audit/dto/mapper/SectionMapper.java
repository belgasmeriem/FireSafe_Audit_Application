package org.xproce.firesafe_audit.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Section;
import org.xproce.firesafe_audit.dto.norme.SectionDTO;

@Component
@RequiredArgsConstructor
public class SectionMapper {

    private final CritereMapper critereMapper;

    public SectionDTO toDTO(Section section) {
        if (section == null) {
            return null;
        }

        return SectionDTO.builder()
                .id(section.getId())
                .code(section.getCode())
                .titre(section.getTitre())
                .description(section.getDescription())
                .ordre(section.getOrdre())
                .normeId(section.getNorme() != null ? section.getNorme().getId() : null)
                .normeNom(section.getNorme() != null ? section.getNorme().getNom() : null)
                .nombreCriteres(section.getCriteres() != null ? section.getCriteres().size() : 0)
                .build();
    }

    public SectionDTO toDTOWithCriteres(Section section) {
        if (section == null) {
            return null;
        }

        SectionDTO dto = toDTO(section);

        if (section.getCriteres() != null && !section.getCriteres().isEmpty()) {
            dto.setCriteres(section.getCriteres().stream()
                    .map(critereMapper::toDTO)
                    .toList());
        }

        return dto;
    }

    public Section toEntity(SectionDTO dto) {
        if (dto == null) {
            return null;
        }

        Section section = new Section();
        section.setId(dto.getId());
        section.setCode(dto.getCode());
        section.setTitre(dto.getTitre());
        section.setDescription(dto.getDescription());
        section.setOrdre(dto.getOrdre());

        return section;
    }
}