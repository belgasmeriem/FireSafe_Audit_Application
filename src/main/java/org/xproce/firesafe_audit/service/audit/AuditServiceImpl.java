package org.xproce.firesafe_audit.service.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.*;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;
import org.xproce.firesafe_audit.dao.repositories.*;
import org.xproce.firesafe_audit.dto.audit.*;
import org.xproce.firesafe_audit.dto.mapper.AuditMapper;
import org.xproce.firesafe_audit.service.notification.INotificationService;
import org.xproce.firesafe_audit.web.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.xproce.firesafe_audit.dao.repositories.CritereRepository;
import org.xproce.firesafe_audit.dao.repositories.EvaluationCritereRepository;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.xproce.firesafe_audit.dao.enums.RoleType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
    private final EvaluationCritereRepository evaluationCritereRepository;

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
                .map(audit -> {
                    AuditDTO dto = auditMapper.toDTO(audit);
                    dto.setProgression(calculateProgression(audit.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsByStatut(StatutAudit statut) {
        return auditRepository.findByStatutOrdered(statut).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<AuditDTO> getAuditsByType(TypeAudit type) {
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getType() == type)
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
    public List<AuditDTO> getAuditsByAuditeurAndStatut(Long auditeurId, StatutAudit statut) {
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getAuditeur().getId().equals(auditeurId))
                .filter(audit -> audit.getStatut() == statut)
                .sorted((a1, a2) -> a2.getDateAudit().compareTo(a1.getDateAudit()))
                .map(audit -> {
                    AuditDTO dto = auditMapper.toDTO(audit);
                    dto.setProgression(calculateProgression(audit.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsByAuditeurAndStatuts(Long auditeurId, List<StatutAudit> statuts) {
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getAuditeur().getId().equals(auditeurId))
                .filter(audit -> statuts.contains(audit.getStatut()))
                .sorted((a1, a2) -> a2.getDateAudit().compareTo(a1.getDateAudit()))
                .map(audit -> {
                    AuditDTO dto = auditMapper.toDTO(audit);
                    dto.setProgression(calculateProgression(audit.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<AuditDTO> getAuditsEnRetard() {
        LocalDate today = LocalDate.now();
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getStatut() == StatutAudit.PLANIFIE)
                .filter(audit -> audit.getDateAudit().isBefore(today))
                .sorted((a1, a2) -> a1.getDateAudit().compareTo(a2.getDateAudit()))
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsCetteSemaine() {
        LocalDate today = LocalDate.now();
        LocalDate endOfWeek = today.plusDays(7);
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getStatut() == StatutAudit.PLANIFIE)
                .filter(audit -> !audit.getDateAudit().isBefore(today))
                .filter(audit -> !audit.getDateAudit().isAfter(endOfWeek))
                .sorted((a1, a2) -> a1.getDateAudit().compareTo(a2.getDateAudit()))
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuditDTO> getAuditsAVenir() {
        LocalDate futureDate = LocalDate.now().plusDays(7);
        return auditRepository.findAll().stream()
                .filter(audit -> audit.getStatut() == StatutAudit.PLANIFIE)
                .filter(audit -> audit.getDateAudit().isAfter(futureDate))
                .sorted((a1, a2) -> a1.getDateAudit().compareTo(a2.getDateAudit()))
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

        if (dto.getType() == TypeAudit.CONTRE_VISITE) {
            if (dto.getAuditInitialId() == null) {
                throw new IllegalArgumentException(
                        "Une contre-visite doit obligatoirement être liée à un audit initial");
            }

            Audit auditInitial = auditRepository.findById(dto.getAuditInitialId())
                    .orElseThrow(() -> new ResourceNotFoundException("Audit initial non trouvé"));

            // Vérifier que l'audit initial est terminé/validé
            if (auditInitial.getStatut() != StatutAudit.TERMINE &&
                    auditInitial.getStatut() != StatutAudit.VALIDE) {
                throw new IllegalStateException(
                        "L'audit initial doit être terminé ou validé avant de planifier une contre-visite");
            }

            // Vérifier qu'il y a bien des NC à réévaluer
            long nbNC = evaluationRepository.findNonConformitesByAudit(auditInitial.getId()).size();

            if (nbNC == 0) {
                throw new IllegalStateException(
                        "L'audit initial n'a aucune non-conformité. Une contre-visite n'est pas nécessaire.");
            }

            audit.setAuditInitial(auditInitial);

            log.info("Contre-visite créée pour l'audit initial #{} avec {} NC à réévaluer",
                    auditInitial.getId(), nbNC);
        }

        Audit saved = auditRepository.save(audit);

        if (dto.getType() == TypeAudit.CONTRE_VISITE && saved.getAuditInitial() != null) {
            List<EvaluationCritere> ncInitiales = evaluationRepository
                    .findNonConformitesByAudit(saved.getAuditInitial().getId());

            for (EvaluationCritere ncInitiale : ncInitiales) {
                EvaluationCritere evaluation = EvaluationCritere.builder()
                        .audit(saved)
                        .critere(ncInitiale.getCritere())
                        .build();
                evaluationRepository.save(evaluation);
            }

            log.info("✅ {} évaluations créées pour la contre-visite (NC uniquement)", ncInitiales.size());
        } else {
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

            log.info("✅ {} évaluations créées pour l'audit normal (tous critères)", criteres.size());
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
    public AuditDTO updateAuditFull(Long id, AuditUpdateDTO dto) {
        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé avec l'ID : " + id));

        if (audit.getStatut() == StatutAudit.VALIDE || audit.getStatut() == StatutAudit.REJETE) {
            throw new RuntimeException("Impossible de modifier un audit validé ou rejeté");
        }


        if (audit.getStatut() == StatutAudit.PLANIFIE && dto.getType() != null) {
            audit.setType(dto.getType());
        }

        if (dto.getDateAudit() != null) {
            audit.setDateAudit(dto.getDateAudit());
        }

        if (audit.getStatut() == StatutAudit.PLANIFIE && dto.getEtablissementId() != null) {
            if (!audit.getEtablissement().getId().equals(dto.getEtablissementId())) {
                Etablissement nouvelEtab = etablissementRepository.findById(dto.getEtablissementId())
                        .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));
                audit.setEtablissement(nouvelEtab);

            }
        }

        if (dto.getAuditeurId() != null && !audit.getAuditeur().getId().equals(dto.getAuditeurId())) {
            User nouvelAuditeur = userRepository.findById(dto.getAuditeurId())
                    .orElseThrow(() -> new RuntimeException("Auditeur non trouvé"));

            User ancienAuditeur = audit.getAuditeur();
            audit.setAuditeur(nouvelAuditeur);

            notificationService.notifyAuditPlanifie(audit);
        }

        if (dto.getDureeEstimee() != null) {
            audit.setDureeEstimee(dto.getDureeEstimee());
        }

        if (dto.getObservationGenerale() != null) {
            audit.setObservationGenerale(dto.getObservationGenerale());
        }

        audit.setDateModification(java.time.LocalDateTime.now());

        Audit updated = auditRepository.save(audit);


        return auditMapper.toDTO(updated);
    }


    @Override
    @Transactional
    public AuditDTO terminerAudit(Long id) {
        log.info("Démarrage du processus de fin d'audit pour l'ID: {}", id);

        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        if (audit.getStatut() != StatutAudit.EN_COURS) {
            throw new RuntimeException("Seul un audit EN_COURS peut être terminé");
        }

        List<EvaluationCritere> evaluations = evaluationCritereRepository.findByAudit_Id(id);

        if (evaluations.isEmpty()) {
            throw new RuntimeException("Impossible de terminer un audit sans évaluations");
        }

        List<EvaluationCritere> evaluationsValides = evaluations.stream()
                .filter(e -> e.getStatut() != null)
                .toList();

        if (evaluationsValides.isEmpty()) {
            throw new RuntimeException("Aucune évaluation valide trouvée");
        }

        BigDecimal sommePoints = BigDecimal.ZERO;
        BigDecimal pointsMax = BigDecimal.valueOf(evaluationsValides.size() * 3);

        for (EvaluationCritere evaluation : evaluationsValides) {
            if (evaluation.getStatut() == null) {
                continue;
            }

            switch (evaluation.getStatut()) {
                case CONFORME:
                    sommePoints = sommePoints.add(BigDecimal.valueOf(3));
                    break;
                case PARTIELLEMENT_CONFORME:
                    sommePoints = sommePoints.add(BigDecimal.valueOf(2));
                    break;
                case NON_CONFORME:
                    sommePoints = sommePoints.add(BigDecimal.valueOf(1));
                    break;
                case NON_APPLICABLE:
                    pointsMax = pointsMax.subtract(BigDecimal.valueOf(3));
                    break;
            }
        }

        BigDecimal tauxConformite = BigDecimal.ZERO;
        if (pointsMax.compareTo(BigDecimal.ZERO) > 0) {
            tauxConformite = sommePoints
                    .multiply(BigDecimal.valueOf(100))
                    .divide(pointsMax, 2, RoundingMode.HALF_UP);
        }

        audit.setStatut(StatutAudit.TERMINE);
        audit.setDateCloture(LocalDateTime.now());
        audit.setTauxConformite(tauxConformite);

        if (audit.getDateDebut() != null) {
            Duration duree = Duration.between(audit.getDateDebut(), LocalDateTime.now());
            audit.setDureeReelle((int) duree.toMinutes());
        }

        Audit savedAudit = auditRepository.save(audit);
        log.info("Audit {} terminé avec succès. Taux de conformité: {}%", id, tauxConformite);

        return auditMapper.toDTO(savedAudit);
    }



    @Override
    @Transactional
    public AuditDTO demarrerAudit(Long auditId) {
        log.info("Démarrage de l'audit avec ID: {}", auditId);

        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("Audit non trouvé avec l'ID: " + auditId));

        if (audit.getStatut() != StatutAudit.PLANIFIE) {
            throw new IllegalStateException("Seuls les audits planifiés peuvent être démarrés. Statut actuel: " + audit.getStatut());
        }

        audit.setStatut(StatutAudit.EN_COURS);
        audit.setDateDebut(LocalDateTime.now());

        Audit savedAudit = auditRepository.save(audit);
        log.info("Audit {} démarré avec succès", auditId);

        return auditMapper.toDTO(savedAudit);
    }

    @Override
    @Transactional
    public AuditDTO soumettreAudit(Long auditId) {
        log.info("Soumission de l'audit avec ID: {}", auditId);

        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new ResourceNotFoundException("Audit non trouvé avec l'ID: " + auditId));

        if (audit.getStatut() != StatutAudit.EN_COURS) {
            throw new IllegalStateException("Seuls les audits en cours peuvent être soumis. Statut actuel: " + audit.getStatut());
        }


        long evaluationsFaites = evaluationCritereRepository.countByAudit_Id(auditId);

        if (evaluationsFaites == 0) {
            throw new IllegalStateException("L'audit ne contient aucune évaluation");
        }

        Double tauxConformite = calculateTauxConformite(auditId);

        audit.setStatut(StatutAudit.TERMINE);

        audit.setTauxConformite(BigDecimal.valueOf(tauxConformite));


        Audit savedAudit = auditRepository.save(audit);
        log.info("Audit {} soumis avec succès. Taux de conformité: {}%", auditId, tauxConformite);


        return auditMapper.toDTO(savedAudit);
    }

    @Override
    public Integer calculateProgression(Long auditId) {
        Audit audit = auditRepository.findById(auditId)
                .orElseThrow(() -> new RuntimeException("Audit non trouvé"));

        long totalCriteres = critereRepository.countByNormeId(
                audit.getEtablissement().getNorme().getId()
        );

        if (totalCriteres == 0) {
            return 0;
        }

        long evaluationsFaites = evaluationCritereRepository.findByAudit_Id(auditId).stream()
                .filter(e -> e.getStatut() != null)
                .count();

        return (int) Math.round((evaluationsFaites * 100.0) / totalCriteres);
    }

    @Override
    public Double calculateTauxConformite(Long auditId) {
        List<EvaluationCritere> evaluations = evaluationCritereRepository.findByAudit_Id(auditId);

        if (evaluations.isEmpty()) {
            return 0.0;
        }

        List<EvaluationCritere> evaluationsApplicables = evaluations.stream()
                .filter(e -> e.getStatut() != StatutConformite.NON_APPLICABLE)
                .toList();

        if (evaluationsApplicables.isEmpty()) {
            return 100.0;
        }

        double scoreTotal = 0;
        for (EvaluationCritere eval : evaluationsApplicables) {
            switch (eval.getStatut()) {
                case CONFORME:
                    scoreTotal += 100;
                    break;
                case PARTIELLEMENT_CONFORME:
                    scoreTotal += 50;
                    break;
                case NON_CONFORME:
                    scoreTotal += 0;
                    break;
                default:
                    break;
            }
        }

        double tauxConformite = scoreTotal / evaluationsApplicables.size();

        return Math.round(tauxConformite * 100.0) / 100.0;
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

    public StatistiquesAuditeurDTO getStatistiquesByAuditeur(Long auditeurId) {
        List<AuditDTO> audits = getAuditsByAuditeur(auditeurId);

        Integer totalAudits = audits.size();

        Double tauxConformiteMoyen = audits.stream()
                .filter(a -> a.getTauxConformite() != null)
                .mapToDouble(a -> a.getTauxConformite().doubleValue())
                .average()
                .orElse(0.0);

        Double dureeMoyenne = audits.stream()
                .filter(a -> a.getDureeReelle() != null)
                .mapToDouble(a -> a.getDureeReelle().doubleValue())
                .average()
                .orElse(0.0);
        Double scorePerformance = (tauxConformiteMoyen / 10.0);

        List<PerformanceEtablissementDTO> parEtablissement = audits.stream()
                .collect(Collectors.groupingBy(a -> a.getEtablissement().getId()))
                .entrySet()
                .stream()
                .map(entry -> {
                    List<AuditDTO> auditsEtab = entry.getValue();
                    AuditDTO firstAudit = auditsEtab.get(0);

                    Double tauxConf = auditsEtab.stream()
                            .filter(a -> a.getTauxConformite() != null)
                            .mapToDouble(a -> a.getTauxConformite().doubleValue())
                            .average()
                            .orElse(0.0);

                    Double duree = auditsEtab.stream()
                            .filter(a -> a.getDureeReelle() != null)
                            .mapToDouble(a -> a.getDureeReelle().doubleValue())
                            .average()
                            .orElse(0.0);

                    Integer performance = calculateStarRating(tauxConf);

                    return PerformanceEtablissementDTO.builder()
                            .etablissementNom(firstAudit.getEtablissement().getNom())
                            .nombreAudits(auditsEtab.size())
                            .tauxConformite(tauxConf)
                            .dureeMoyenne(duree)
                            .performance(performance)
                            .build();
                })
                .collect(Collectors.toList());

        return StatistiquesAuditeurDTO.builder()
                .totalAudits(totalAudits)
                .tauxConformiteMoyen(tauxConformiteMoyen)
                .dureeMoyenne(dureeMoyenne)
                .scorePerformance(scorePerformance)
                .parEtablissement(parEtablissement)
                .build();
    }

    private Integer calculateStarRating(Double tauxConformite) {
        if (tauxConformite >= 90) return 5;
        if (tauxConformite >= 80) return 4;
        if (tauxConformite >= 70) return 3;
        if (tauxConformite >= 60) return 2;
        return 1;
    }

    @Override
    public Double getTauxMoyenByEtablissement(Long etablissementId) {
        return auditRepository.findTauxMoyenByEtablissement(etablissementId);
    }


    @Override
    public List<AuditDTO> getAuditsTermines() {
        return auditRepository.findByStatutOrdered(StatutAudit.TERMINE).stream()
                .map(auditMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public int countAuditsValidesParManagerCeMois(Long managerId) {
        LocalDateTime debutMois = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        LocalDateTime finMois = LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        return (int) auditRepository.findAll().stream()
                .filter(a -> a.getStatut() == StatutAudit.VALIDE)
                .filter(a -> a.getValidateur() != null)
                .filter(a -> a.getValidateur().getId().equals(managerId))
                .filter(a -> a.getDateValidation() != null)
                .filter(a -> a.getDateValidation().isAfter(debutMois) &&
                        a.getDateValidation().isBefore(finMois))
                .count();
    }


    @Override
    public List<Map<String, Object>> getEvolutionMensuelle(int nombreMois) {
        log.info("Récupération de l'évolution mensuelle sur {} mois", nombreMois);

        LocalDate dateDebut = LocalDate.now().minusMonths(nombreMois);
        List<Audit> audits = auditRepository.findByDateAuditAfter(dateDebut);

        Map<String, Long> auditsParMois = audits.stream()
                .collect(Collectors.groupingBy(
                        audit -> audit.getDateAudit().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH)),
                        Collectors.counting()
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = nombreMois - 1; i >= 0; i--) {
            LocalDate mois = LocalDate.now().minusMonths(i);
            String moisLabel = mois.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH));

            Map<String, Object> data = new HashMap<>();
            data.put("mois", moisLabel);
            data.put("auditsRealises", auditsParMois.getOrDefault(moisLabel, 0L));
            result.add(data);
        }

        log.info("Évolution mensuelle: {} mois avec données", result.size());
        return result;
    }

    @Override
    public Map<String, Long> getRepartitionParStatut() {
        log.info("Récupération de la répartition par statut");

        List<Audit> audits = auditRepository.findAll();

        Map<String, Long> repartition = audits.stream()
                .collect(Collectors.groupingBy(
                        audit -> audit.getStatut().getLibelle(),
                        Collectors.counting()
                ));

        log.info("Répartition par statut: {}", repartition);
        return repartition;
    }

    @Override
    public Map<String, Long> getRepartitionParType() {
        log.info("Récupération de la répartition par type");

        List<Audit> audits = auditRepository.findAll();

        Map<String, Long> repartition = audits.stream()
                .collect(Collectors.groupingBy(
                        audit -> audit.getType().getLibelle(),
                        Collectors.counting()
                ));

        log.info("Répartition par type: {}", repartition);
        return repartition;
    }

    @Override
    public List<Map<String, Object>> getConformiteMensuelle(int nombreMois) {
        log.info("Récupération du taux de conformité mensuel sur {} mois", nombreMois);

        LocalDate dateDebut = LocalDate.now().minusMonths(nombreMois);
        List<Audit> audits = auditRepository.findByDateAuditAfterAndStatutIn(
                dateDebut,
                Arrays.asList(StatutAudit.TERMINE, StatutAudit.VALIDE)
        );

        Map<String, List<Audit>> auditsParMois = audits.stream()
                .filter(audit -> audit.getTauxConformite() != null)
                .collect(Collectors.groupingBy(
                        audit -> audit.getDateAudit().format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH))
                ));

        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = nombreMois - 1; i >= 0; i--) {
            LocalDate mois = LocalDate.now().minusMonths(i);
            String moisLabel = mois.format(DateTimeFormatter.ofPattern("MMM yyyy", Locale.FRENCH));

            List<Audit> auditsDuMois = auditsParMois.getOrDefault(moisLabel, List.of());
            Double tauxMoyen = auditsDuMois.stream()
                    .map(Audit::getTauxConformite)
                    .filter(Objects::nonNull)
                    .mapToDouble(BigDecimal::doubleValue)
                    .average()
                    .orElse(0.0);

            Map<String, Object> data = new HashMap<>();
            data.put("mois", moisLabel);
            data.put("tauxMoyen", Math.round(tauxMoyen * 100.0) / 100.0);
            result.add(data);
        }

        log.info("Conformité mensuelle: {} mois avec données", result.size());
        return result;
    }

    @Override
    public List<Map<String, Object>> getPerformanceAuditeurs() {
        log.info("Récupération de la performance des auditeurs");

        List<User> auditeurs = userRepository.findByRoleType(RoleType.AUDITOR);

        List<Map<String, Object>> performances = new ArrayList<>();

        for (User auditeur : auditeurs) {
            List<Audit> auditsAuditeur = auditRepository.findByAuditeurId(auditeur.getId());

            if (auditsAuditeur.isEmpty()) {
                continue;
            }

            long totalAudits = auditsAuditeur.size();

            // Taux moyen de conformité
            double tauxMoyen = auditsAuditeur.stream()
                    .filter(audit -> audit.getTauxConformite() != null)
                    .mapToDouble(audit -> audit.getTauxConformite().doubleValue())
                    .average()
                    .orElse(0.0);

            // Durée moyenne
            double dureeMoyenne = auditsAuditeur.stream()
                    .filter(audit -> audit.getDureeReelle() != null)
                    .mapToDouble(Audit::getDureeReelle)
                    .average()
                    .orElse(0.0);

            // Audits en retard
            long enRetard = auditsAuditeur.stream()
                    .filter(audit -> audit.getDateValidation() != null)
                    .filter(audit -> audit.getDateValidation().isAfter(audit.getDateAudit().atStartOfDay()))
                    .count();

            // Calcul du score
            double scoreBase = tauxMoyen * 0.6;
            double scoreEffic= Math.max(0, (5.0 - dureeMoyenne) / 5.0) * 20;
            double scorePonct = Math.max(0, (1.0 - (enRetard / (double) totalAudits)) * 20);
            double score = scoreBase + scoreEffic + scorePonct;

            // Générer des couleurs pour l'avatar
            String[] couleurs1 = {"#006666", "#dc3545", "#ffc107", "#198754", "#0dcaf0"};
            String[] couleurs2 = {"#008080", "#c82333", "#f57c00", "#28a745", "#17a2b8"};
            int colorIndex = (int) (auditeur.getId() % couleurs1.length);

            Map<String, Object> perf = new HashMap<>();
            perf.put("nomComplet", auditeur.getNomComplet());
            perf.put("email", auditeur.getEmail());
            perf.put("initiales", getInitiales(auditeur.getNomComplet()));
            perf.put("totalAudits", totalAudits);
            perf.put("tauxMoyen", Math.round(tauxMoyen));
            perf.put("dureeMoyenne", Math.round(dureeMoyenne * 10.0) / 10.0);
            perf.put("enRetard", enRetard);
            perf.put("score", Math.round(score));
            perf.put("couleur1", couleurs1[colorIndex]);
            perf.put("couleur2", couleurs2[colorIndex]);

            performances.add(perf);
        }

        // Trier par score décroissant
        performances.sort((a, b) ->
                Long.compare((Long) b.get("score"), (Long) a.get("score"))
        );

        log.info("Performance de {} auditeurs calculée", performances.size());
        return performances;
    }

    private String getInitiales(String nomComplet) {
        if (nomComplet == null || nomComplet.isEmpty()) {
            return "??";
        }

        String[] parts = nomComplet.split(" ");
        if (parts.length >= 2) {
            return (parts[0].charAt(0) + "" + parts[1].charAt(0)).toUpperCase();
        }

        return nomComplet.substring(0, Math.min(2, nomComplet.length())).toUpperCase();
    }


    @Override
    public int countValidationsCeMois() {
        LocalDateTime debutMois = LocalDateTime.now()
                .withDayOfMonth(1)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        LocalDateTime finMois = LocalDateTime.now()
                .withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth())
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

        return (int) auditRepository.findAll().stream()
                .filter(a -> a.getStatut() == StatutAudit.VALIDE)
                .filter(a -> a.getDateValidation() != null)
                .filter(a -> a.getDateValidation().isAfter(debutMois) &&
                        a.getDateValidation().isBefore(finMois))
                .count();
    }
}