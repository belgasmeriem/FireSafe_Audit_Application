package org.xproce.firesafe_audit.service.norme;

import org.xproce.firesafe_audit.dto.norme.SectionDTO;

import java.util.List;


public interface ISectionService {


    List<SectionDTO> getAllSections();

    SectionDTO getSectionById(Long id);

    List<SectionDTO> getSectionsByNorme(Long normeId);


    SectionDTO getSectionByCode(String code);


    SectionDTO createSection(SectionDTO sectionDTO);


    SectionDTO updateSection(Long id, SectionDTO sectionDTO);


    void deleteSection(Long id);


    long countSectionsByNorme(Long normeId);
}