package org.xproce.firesafe_audit.service.norme;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dao.entities.Section;
import org.xproce.firesafe_audit.dao.repositories.NormeRepository;
import org.xproce.firesafe_audit.dao.repositories.SectionRepository;
import org.xproce.firesafe_audit.dto.mapper.SectionMapper;
import org.xproce.firesafe_audit.dto.norme.SectionDTO;
import org.xproce.firesafe_audit.web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionServiceImpl implements ISectionService {

    private final SectionRepository sectionRepository;
    private final NormeRepository normeRepository;
    private final SectionMapper sectionMapper;

    @Override
    public List<SectionDTO> getAllSections() {
        log.info("Récupération de toutes les sections");
        return sectionRepository.findAll().stream()
                .map(sectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SectionDTO getSectionById(Long id) {
        log.info("Récupération de la section avec ID: {}", id);
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'ID: " + id));
        return sectionMapper.toDTO(section);
    }

    @Override
    public List<SectionDTO> getSectionsByNorme(Long normeId) {
        log.info("Récupération des sections pour la norme ID: {}", normeId);
        List<Section> sections = sectionRepository.findByNorme_IdOrderByOrdreAsc(normeId);
        return sections.stream()
                .map(sectionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SectionDTO getSectionByCode(String code) {
        log.info("Récupération de la section avec code: {}", code);
        Section section = sectionRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec le code: " + code));
        return sectionMapper.toDTO(section);
    }

    @Override
    @Transactional
    public SectionDTO createSection(SectionDTO sectionDTO) {
        log.info("Création d'une nouvelle section: {}", sectionDTO.getCode());

        if (sectionRepository.findByCode(sectionDTO.getCode()).isPresent()) {
            throw new IllegalArgumentException("Une section avec le code " + sectionDTO.getCode() + " existe déjà");
        }

        Norme norme = normeRepository.findById(sectionDTO.getNormeId())
                .orElseThrow(() -> new ResourceNotFoundException("Norme non trouvée"));

        Section section = sectionMapper.toEntity(sectionDTO);
        section.setNorme(norme);

        Section savedSection = sectionRepository.save(section);
        log.info("Section créée avec succès, ID: {}", savedSection.getId());

        return sectionMapper.toDTO(savedSection);
    }

    @Override
    @Transactional
    public SectionDTO updateSection(Long id, SectionDTO sectionDTO) {
        log.info("Mise à jour de la section ID: {}", id);

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Section non trouvée avec l'ID: " + id));

        if (!section.getCode().equals(sectionDTO.getCode())) {
            if (sectionRepository.findByCode(sectionDTO.getCode()).isPresent()) {
                throw new IllegalArgumentException("Une section avec le code " + sectionDTO.getCode() + " existe déjà");
            }
        }

        section.setCode(sectionDTO.getCode());
        section.setTitre(sectionDTO.getTitre());
        section.setDescription(sectionDTO.getDescription());
        section.setOrdre(sectionDTO.getOrdre());

        Section updatedSection = sectionRepository.save(section);
        log.info("Section mise à jour avec succès");

        return sectionMapper.toDTO(updatedSection);
    }

    @Override
    @Transactional
    public void deleteSection(Long id) {
        log.info("Suppression de la section ID: {}", id);

        if (!sectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Section non trouvée avec l'ID: " + id);
        }

        sectionRepository.deleteById(id);
        log.info("Section supprimée avec succès");
    }

    @Override
    public long countSectionsByNorme(Long normeId) {
        return sectionRepository.countByNorme_Id(normeId);
    }
}