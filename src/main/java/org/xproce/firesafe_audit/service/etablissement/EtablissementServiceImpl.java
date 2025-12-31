package org.xproce.firesafe_audit.service.etablissement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.Etablissement;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dao.repositories.EtablissementRepository;
import org.xproce.firesafe_audit.dao.repositories.NormeRepository;
import org.xproce.firesafe_audit.dto.etablissement.*;
import org.xproce.firesafe_audit.dto.mapper.EtablissementMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EtablissementServiceImpl implements IEtablissementService {

    private final EtablissementRepository etablissementRepository;
    private final NormeRepository normeRepository;
    private final EtablissementMapper etablissementMapper;

    @Override
    public List<EtablissementDTO> getAllEtablissements() {
        return etablissementRepository.findAll().stream()
                .map(etablissementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtablissementDTO> getActiveEtablissements() {
        return etablissementRepository.findByActifTrue().stream()
                .map(etablissementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EtablissementDTO getEtablissementById(Long id) {
        Etablissement etablissement = etablissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));
        return etablissementMapper.toDTO(etablissement);
    }

    @Override
    public List<EtablissementDTO> getEtablissementsByType(TypeEtablissement type) {
        return etablissementRepository.findByTypeAndActifTrue(type).stream()
                .map(etablissementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtablissementDTO> getEtablissementsByVille(String ville) {
        return etablissementRepository.findByVilleAndActifTrue(ville).stream()
                .map(etablissementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EtablissementDTO> searchEtablissements(String search) {
        return etablissementRepository.searchEtablissements(search).stream()
                .map(etablissementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EtablissementDTO createEtablissement(EtablissementCreateDTO dto) {
        Norme norme = normeRepository.findById(dto.getNormeId())
                .orElseThrow(() -> new RuntimeException("Norme non trouvée"));

        Etablissement etablissement = Etablissement.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .type(dto.getType())
                .adresse(dto.getAdresse())
                .ville(dto.getVille())
                .codePostal(dto.getCodePostal())
                .pays(dto.getPays())
                .capaciteAccueil(dto.getCapaciteAccueil())
                .nombreEtages(dto.getNombreEtages())
                .surfaceTotale(dto.getSurfaceTotale())
                .responsableNom(dto.getResponsableNom())
                .responsableEmail(dto.getResponsableEmail())
                .responsableTelephone(dto.getResponsableTelephone())
                .norme(norme)
                .actif(true)
                .build();

        Etablissement saved = etablissementRepository.save(etablissement);
        return etablissementMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EtablissementDTO updateEtablissement(Long id, EtablissementUpdateDTO dto) {
        Etablissement etablissement = etablissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

        if (dto.getNom() != null) etablissement.setNom(dto.getNom());
        if (dto.getDescription() != null) etablissement.setDescription(dto.getDescription());
        if (dto.getType() != null) etablissement.setType(dto.getType());
        if (dto.getAdresse() != null) etablissement.setAdresse(dto.getAdresse());
        if (dto.getVille() != null) etablissement.setVille(dto.getVille());
        if (dto.getCodePostal() != null) etablissement.setCodePostal(dto.getCodePostal());
        if (dto.getPays() != null) etablissement.setPays(dto.getPays());
        if (dto.getCapaciteAccueil() != null) etablissement.setCapaciteAccueil(dto.getCapaciteAccueil());
        if (dto.getNombreEtages() != null) etablissement.setNombreEtages(dto.getNombreEtages());
        if (dto.getSurfaceTotale() != null) etablissement.setSurfaceTotale(dto.getSurfaceTotale());
        if (dto.getResponsableNom() != null) etablissement.setResponsableNom(dto.getResponsableNom());
        if (dto.getResponsableEmail() != null) etablissement.setResponsableEmail(dto.getResponsableEmail());
        if (dto.getResponsableTelephone() != null) etablissement.setResponsableTelephone(dto.getResponsableTelephone());
        if (dto.getActif() != null) etablissement.setActif(dto.getActif());

        if (dto.getNormeId() != null) {
            Norme norme = normeRepository.findById(dto.getNormeId())
                    .orElseThrow(() -> new RuntimeException("Norme non trouvée"));
            etablissement.setNorme(norme);
        }

        Etablissement updated = etablissementRepository.save(etablissement);
        return etablissementMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void deleteEtablissement(Long id) {
        Etablissement etablissement = etablissementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));
        etablissement.setActif(false);
        etablissementRepository.save(etablissement);
    }

    @Override
    public long countActiveEtablissements() {
        return etablissementRepository.countActiveEtablissements();
    }

    @Override
    public long countByType(TypeEtablissement type) {
        return etablissementRepository.countByType(type);
    }
}