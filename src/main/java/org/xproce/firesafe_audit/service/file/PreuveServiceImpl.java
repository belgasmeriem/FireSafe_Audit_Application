package org.xproce.firesafe_audit.service.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.entities.EvaluationCritere;
import org.xproce.firesafe_audit.dao.entities.Preuve;
import org.xproce.firesafe_audit.dao.enums.TypeFichier;
import org.xproce.firesafe_audit.dao.repositories.EvaluationCritereRepository;
import org.xproce.firesafe_audit.dao.repositories.PreuveRepository;
import org.xproce.firesafe_audit.dto.common.PreuveDTO;
import org.xproce.firesafe_audit.dto.mapper.PreuveMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreuveServiceImpl implements IPreuveService {

    private final PreuveRepository preuveRepository;
    private final EvaluationCritereRepository evaluationRepository;
    private final IFileStorageService fileStorageService;
    private final PreuveMapper preuveMapper;

    @Override
    public List<PreuveDTO> getPreuvesByEvaluation(Long evaluationId) {
        return preuveRepository.findByEvaluationIdOrderByDateUploadDesc(evaluationId).stream()
                .map(preuveMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PreuveDTO getPreuveById(Long id) {
        Preuve preuve = preuveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preuve non trouvée"));
        return preuveMapper.toDTO(preuve);
    }

    @Override
    public byte[] downloadPreuve(Long id) {
        Preuve preuve = preuveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preuve non trouvée"));
        return fileStorageService.loadFile(preuve.getCheminFichier());
    }

    @Override
    @Transactional
    public PreuveDTO uploadPreuve(Long evaluationId, MultipartFile file, String description) {
        EvaluationCritere evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        String filePath = fileStorageService.storeFile(file, "preuves");

        TypeFichier typeFichier = TypeFichier.fromExtension(file.getOriginalFilename());

        Preuve preuve = Preuve.builder()
                .nomFichier(file.getOriginalFilename())
                .cheminFichier(filePath)
                .typeFichier(typeFichier)
                .tailleFichier(file.getSize())
                .description(description)
                .evaluation(evaluation)
                .build();

        Preuve saved = preuveRepository.save(preuve);
        return preuveMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deletePreuve(Long id) {
        Preuve preuve = preuveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preuve non trouvée"));

        fileStorageService.deleteFile(preuve.getCheminFichier());
        preuveRepository.delete(preuve);
    }

    @Override
    public long countByEvaluation(Long evaluationId) {
        return preuveRepository.countByEvaluation(evaluationId);
    }
}