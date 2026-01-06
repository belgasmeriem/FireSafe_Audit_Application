package org.xproce.firesafe_audit.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import org.xproce.firesafe_audit.dao.repositories.*;
import org.xproce.firesafe_audit.dto.evaluation.*;
import org.xproce.firesafe_audit.dto.mapper.EvaluationMapper;
import org.xproce.firesafe_audit.service.notification.INotificationService;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EvaluationCritereServiceImpl implements IEvaluationCritereService {

    private final EvaluationCritereRepository evaluationRepository;
    private final AuditRepository auditRepository;
    private final CritereRepository critereRepository;
    private final EvaluationMapper evaluationMapper;
    private final INotificationService notificationService;

    @Override
    public List<EvaluationCritereDTO> getEvaluationsByAudit(Long auditId) {
        return evaluationRepository.findByAuditIdOrderByCritereCodeAsc(auditId).stream()
                .filter(eval -> eval.getStatut() != null)
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EvaluationCritereDTO getEvaluationById(Long id) {
        EvaluationCritere evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));
        return evaluationMapper.toDTO(evaluation);
    }

    @Override
    public List<EvaluationCritereDTO> getNonConformitesByAudit(Long auditId) {
        return evaluationRepository.findNonConformitesByAudit(auditId).stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationCritereDTO> getNonConformitesCritiquesByAudit(Long auditId) {
        return evaluationRepository.findNonConformitesCritiquesByAudit(auditId).stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationCritereDTO> getActionsNonCorrigees() {
        return evaluationRepository.findActionsNonCorrigees().stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<EvaluationCritereDTO> getActionsNonCorrigeesByEtablissement(Long etablissementId) {
        return evaluationRepository.findActionsNonCorrigeesByEtablissement(etablissementId).stream()
                .map(evaluationMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<Critere> getCriteresByNorme(Long normeId) {

        return critereRepository.findByNormeIdOrderByCodeAsc(normeId);
    }

    @Override
    @Transactional
    public EvaluationCritereDTO createEvaluation(EvaluationCreateDTO dto) {
        Audit audit = auditRepository.findById(dto.getAuditId())
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        Critere critere = critereRepository.findById(dto.getCritereId())
                .orElseThrow(() -> new RuntimeException("Critère non trouvé"));

        EvaluationCritere evaluation = EvaluationCritere.builder()
                .statut(dto.getStatut())
                .observation(dto.getObservation())
                .urgence(dto.getUrgence())
                .actionCorrective(dto.getActionCorrective())
                .responsableAction(dto.getResponsableAction())
                .dateEcheance(dto.getDateEcheance())
                .coutEstime(dto.getCoutEstime())
                .audit(audit)
                .critere(critere)
                .corrigee(false)
                .build();

        EvaluationCritere saved = evaluationRepository.save(evaluation);

        if (saved.getStatut() == StatutConformite.NON_CONFORME &&
                saved.getCritere().getCriticite() == org.xproce.firesafe_audit.dao.enums.Criticite.CRITIQUE) {
            notificationService.notifyNonConformiteCritique(saved);
        }

        return evaluationMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public EvaluationCritereDTO updateEvaluation(Long id, EvaluationUpdateDTO dto) {
        EvaluationCritere evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        if (dto.getStatut() != null) evaluation.setStatut(dto.getStatut());
        if (dto.getObservation() != null) evaluation.setObservation(dto.getObservation());
        if (dto.getUrgence() != null) evaluation.setUrgence(dto.getUrgence());
        if (dto.getActionCorrective() != null) evaluation.setActionCorrective(dto.getActionCorrective());
        if (dto.getResponsableAction() != null) evaluation.setResponsableAction(dto.getResponsableAction());
        if (dto.getDateEcheance() != null) evaluation.setDateEcheance(dto.getDateEcheance());
        if (dto.getCoutEstime() != null) evaluation.setCoutEstime(dto.getCoutEstime());
        if (dto.getCorrigee() != null) evaluation.setCorrigee(dto.getCorrigee());

        EvaluationCritere updated = evaluationRepository.save(evaluation);

        Audit audit = updated.getAudit();
        audit.calculerStatistiques();
        auditRepository.save(audit);

        return evaluationMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public void signalerCorrection(Long id) {
        EvaluationCritere evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Évaluation non trouvée"));

        evaluation.signalerCorrection();
        evaluationRepository.save(evaluation);
    }

    @Override
    @Transactional
    public void deleteEvaluation(Long id) {
        evaluationRepository.deleteById(id);
    }
}