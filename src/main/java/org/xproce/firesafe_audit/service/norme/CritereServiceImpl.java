package org.xproce.firesafe_audit.service.norme;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.entities.Critere;
import org.xproce.firesafe_audit.dao.entities.Norme;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dao.enums.Criticite;
import org.xproce.firesafe_audit.dao.repositories.CritereRepository;
import org.xproce.firesafe_audit.dao.repositories.NormeRepository;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CritereServiceImpl implements ICritereService {

    private final CritereRepository critereRepository;
    private final NormeRepository normeRepository;

    @Override
    public List<CritereDTO> getAllCriteres() {
        return critereRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CritereDTO getCritereById(Long id) {
        Critere critere = critereRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Critère non trouvé"));
        return toDTO(critere);
    }

    @Override
    public List<CritereDTO> getCriteresByNorme(Long normeId) {
        return critereRepository.findByNormeIdOrderByCodeAsc(normeId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CritereDTO> getCriteresByNormeAndCategorie(Long normeId, Categorie categorie) {
        return critereRepository.findByNormeAndCategorieOrdered(normeId, categorie).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CritereDTO> getCriteresByCategorie(Categorie categorie) {
        return critereRepository.findByCategorie(categorie).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CritereDTO> searchCriteres(String search) {
        return critereRepository.searchCriteres(search).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CritereDTO createCritere(CritereDTO dto) {
        Norme norme = normeRepository.findById(dto.getNormeId())
                .orElseThrow(() -> new RuntimeException("Norme non trouvée"));

        Critere critere = Critere.builder()
                .code(dto.getCode())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .categorie(dto.getCategorie())
                .criticite(dto.getCriticite())
                .ponderation(dto.getPonderation())
                .obligatoire(dto.getObligatoire())
                .preuvesRequises(dto.getPreuvesRequises())
                .referenceTexteLoi(dto.getReferenceTexteLoi())
                .norme(norme)
                .build();

        Critere saved = critereRepository.save(critere);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCritere(Long id) {
        critereRepository.deleteById(id);
    }

    @Override
    @Transactional
    public List<CritereDTO> importCriteresFromExcel(Long normeId, MultipartFile file) throws IOException {
        Norme norme = normeRepository.findById(normeId)
                .orElseThrow(() -> new RuntimeException("Norme non trouvée"));

        List<Critere> criteres = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheet("CRITÈRES");

            if (sheet == null) {
                throw new RuntimeException("La feuille 'CRITÈRES' n'existe pas dans le fichier");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    Critere critere = Critere.builder()
                            .code(getCellValue(row, 0))
                            .libelle(getCellValue(row, 1))
                            .categorie(Categorie.valueOf(getCellValue(row, 2)))
                            .criticite(Criticite.valueOf(getCellValue(row, 3)))
                            .ponderation(Integer.parseInt(getCellValue(row, 4)))
                            .referenceTexteLoi(getCellValue(row, 5))
                            .norme(norme)
                            .obligatoire(true)
                            .build();

                    criteres.add(critere);
                } catch (Exception e) {
                    throw new RuntimeException("Erreur ligne " + (i + 1) + ": " + e.getMessage());
                }
            }
        }

        List<Critere> saved = critereRepository.saveAll(criteres);
        return saved.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private String getCellValue(Row row, int colIndex) {
        Cell cell = row.getCell(colIndex);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private CritereDTO toDTO(Critere critere) {
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
}