package org.xproce.firesafe_audit.service.norme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dao.repositories.NormeRepository;
import org.xproce.firesafe_audit.dto.norme.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NormeServiceImpl implements INormeService {

    private final NormeRepository normeRepository;

    @Override
    public List<NormeDTO> getAllNormes() {
        return normeRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<NormeDTO> getNormesActives() {
        return normeRepository.findNormesActives(LocalDate.now()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NormeDTO> getActiveNormes() {
        return normeRepository.findByActifTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NormeDTO getNormeById(Long id) {
        Norme norme = normeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Norme non trouvée" + id));
        return toDTO(norme);
    }

    @Override
    public List<NormeDTO> getNormesByPays(String pays) {
        return normeRepository.findByPaysAndActifTrue(pays).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NormeDTO> getNormesByTypeEtablissement(TypeEtablissement type) {
        return normeRepository.findActiveByTypeEtablissement(type).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<NormeDTO> getNormesSansCriteres() {
        return normeRepository.findAll().stream()
                .filter(norme -> norme.getCriteres() == null || norme.getCriteres().isEmpty())
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override

    public List<NormeDTO> searchNormes(String search) {
        return normeRepository.searchNormes(search).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NormeDTO createNorme(NormeCreateDTO dto) {
        if (normeRepository.existsByCode(dto.getCode())) {
            throw new RuntimeException("Ce code de norme existe déjà");
        }

        Norme norme = Norme.builder()
                .code(dto.getCode())
                .nom(dto.getNom())
                .description(dto.getDescription())
                .pays(dto.getPays())
                .version(dto.getVersion())
                .dateVigueur(dto.getDateVigueur())
                .typesEtablissements(dto.getTypesEtablissements())
                .actif(true)
                .build();

        Norme saved = normeRepository.save(norme);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public NormeDTO updateNorme(Long id, NormeCreateDTO dto) {
        Norme norme = normeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Norme non trouvée"));

        if (dto.getCode() != null && !dto.getCode().equals(norme.getCode())) {
            if (normeRepository.existsByCode(dto.getCode())) {
                throw new RuntimeException("Ce code de norme existe déjà");
            }
            norme.setCode(dto.getCode());
        }

        if (dto.getNom() != null) norme.setNom(dto.getNom());
        if (dto.getDescription() != null) norme.setDescription(dto.getDescription());
        if (dto.getPays() != null) norme.setPays(dto.getPays());
        if (dto.getVersion() != null) norme.setVersion(dto.getVersion());
        if (dto.getDateVigueur() != null) norme.setDateVigueur(dto.getDateVigueur());
        if (dto.getTypesEtablissements() != null) norme.setTypesEtablissements(dto.getTypesEtablissements());

        Norme updated = normeRepository.save(norme);
        return toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteNorme(Long id) {
        Norme norme = normeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Norme non trouvée"));

        norme.getEtablissements().forEach(e -> e.setNorme(null));
        normeRepository.delete(norme);
    }

    @Override
    public long countAllNormes() {
        return normeRepository.count();
    }

    @Override
    public long countActiveNormes() {
        return normeRepository.countActiveNormes();
    }
    @Override
    public long countNormesActives() {
        return normeRepository.countNormesActives(LocalDate.now());
    }

    @Override
    public long countNormesByPays(String pays) {
        return normeRepository.countByPays(pays);
    }

    @Override
    public long countNormesSansCriteres() {
        return normeRepository.findAll().stream()
                .filter(norme -> norme.getCriteres() == null || norme.getCriteres().isEmpty())
                .count();
    }

    private NormeDTO toDTO(Norme norme) {
        return NormeDTO.builder()
                .id(norme.getId())
                .code(norme.getCode())
                .nom(norme.getNom())
                .description(norme.getDescription())
                .pays(norme.getPays())
                .version(norme.getVersion())
                .dateVigueur(norme.getDateVigueur())
                .typesEtablissements(norme.getTypesEtablissements())
                .actif(norme.getActif())
                .dateCreation(norme.getDateCreation())
                .nombreCriteres(norme.getNombreCriteres())
                .build();
    }
}