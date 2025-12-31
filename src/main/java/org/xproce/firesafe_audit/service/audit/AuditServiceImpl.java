package org.xproce.firesafe_audit.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.repositories.*;
import org.xproce.firesafe_audit.dto.audit.*;
import org.xproce.firesafe_audit.dto.mapper.AuditMapper;
import org.xproce.firesafe_audit.service.notification.INotificationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements IAuditService {

    private final AuditRepository auditRepository;
    private final EtablissementRepository etablissementRepository;
    private final UserRepository userRepository;
    private final CritereRepository critereRepository;
    private final EvaluationCritereRepository evaluationRepository;
    private final AuditMapper auditMapper;
    private final INotificationService notificationService;

    @Override
    public List<AuditDTO> getAllAudits() {
        return auditRepository.findAll().stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuditDTO getAuditById(Long id) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));
        return auditMapper.toDTO(audit);
    }

    @Override
    public List<AuditDTO> getAuditsByEtablissement(Long etablissementId) {
        return auditRepository.findByEtablissementIdOrderByDateAuditDesc(etablissementId).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsByAuditeur(Long auditeurId) {
        return auditRepository.findByAuditeurIdOrderByDateAuditDesc(auditeurId).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsByStatut(StatutAudit statut) {
        return auditRepository.findByStatutOrdered(statut).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsEnAttenteValidation() {
        return auditRepository.findAuditsEnAttenteValidation().stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsByPeriod(LocalDate debut, LocalDate fin) {
        return auditRepository.findByDateAuditBetween(debut, fin).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AuditDTO createAudit(AuditCreateDTO dto) {
        Etablissement etablissement = etablissementRepository.findById(dto.getEtablissementId())
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

        User auditeur = userRepository.findById(dto.getAuditeurId())
                .orElseThrow(() -> new RuntimeException("Auditeur non trouvé"));

        Audit audit = Audit.builder()
                .dateAudit(dto.getDateAudit())
                .type(dto.getType())
                .statut(StatutAudit.PLANIFIE)
                .etablissement(etablissement)
                .auditeur(auditeur)
                .dureeEstimee(dto.getDureeEstimee())
                .observationGenerale(dto.getObservationGenerale())
                .build();

        Audit saved = auditRepository.save(audit);

        List<Critere> criteres = critereRepository.findByNormeIdOrderByCodeAsc(
                etablissement.getNorme().getId()
        );

        for (Critere critere : criteres) {
            EvaluationCritere evaluation = EvaluationCritere.builder()
                    .audit(saved)
                    .critere(critere)
                    .build();
            evaluationRepository.save(evaluation);
        }

        notificationService.notifyAuditPlanifie(saved);

        return auditMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public AuditDTO updateAudit(Long id, AuditUpdateDTO dto) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        if (dto.getDateAudit() != null) audit.setDateAudit(dto.getDateAudit());
        if (dto.getStatut() != null) audit.setStatut(dto.getStatut());
        if (dto.getObservationGenerale() != null) audit.setObservationGenerale(dto.getObservationGenerale());
        if (dto.getDureeReelle() != null) audit.setDureeReelle(dto.getDureeReelle());

        Audit updated = auditRepository.save(audit);
        return auditMapper.toDTO(updated);
    }

    @Override
    @Transactional
    public AuditDTO terminerAudit(Long id) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        audit.setStatut(StatutAudit.TERMINE);
        audit.calculerStatistiques();

        Audit saved = auditRepository.save(audit);

        notificationService.notifyAuditTermine(saved);

        return auditMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public AuditDTO validerAudit(Long id, Long validateurId) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        User validateur = userRepository.findById(validateurId)
                .orElseThrow(() -> new RuntimeException("Validateur non trouvé"));

        audit.setStatut(StatutAudit.VALIDE);
        audit.setValidateur(validateur);
        audit.setDateValidation(java.time.LocalDateTime.now());

        Audit saved = auditRepository.save(audit);

        return auditMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteAudit(Long id) {
        auditRepository.deleteById(id);
    }

    @Override
    public AuditStatisticsDTO getStatistics() {
        return AuditStatisticsDTO.builder()
                .totalAudits((long) auditRepository.findAll().size())
                .auditsEnCours(auditRepository.countByStatut(StatutAudit.EN_COURS))
                .auditsTermines(auditRepository.countByStatut(StatutAudit.TERMINE))
                .auditsValides(auditRepository.countByStatut(StatutAudit.VALIDE))
                .tauxMoyenConformite(auditRepository.findTauxMoyenGlobal())
                .actionsNonCorrigees((long) evaluationRepository.findActionsNonCorrigees().size())
                .build();
    }

    @Override
    public Double getTauxMoyenByEtablissement(Long etablissementId) {
        return auditRepository.findTauxMoyenByEtablissement(etablissementId);
    }
}