package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.*;
import org.xproce.firesafe_audit.dto.audit.AuditCreateDTO;
import org.xproce.firesafe_audit.dto.audit.AuditDTO;
import org.xproce.firesafe_audit.dto.audit.AuditUpdateDTO;
import org.xproce.firesafe_audit.dto.common.CommentaireDTO;
import org.xproce.firesafe_audit.dto.evaluation.EvaluationCritereDTO;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;
import org.xproce.firesafe_audit.dto.norme.SectionDTO;
import org.xproce.firesafe_audit.service.audit.IAuditService;
import org.xproce.firesafe_audit.service.audit.ICommentaireService;
import org.xproce.firesafe_audit.service.audit.IEvaluationCritereService;
import org.xproce.firesafe_audit.service.auth.IAuthService;
import org.xproce.firesafe_audit.service.etablissement.IEtablissementService;
import org.xproce.firesafe_audit.service.norme.ICritereService;
import org.xproce.firesafe_audit.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.xproce.firesafe_audit.service.norme.INormeService;
import org.xproce.firesafe_audit.service.norme.ISectionService;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Controller
@RequestMapping("/audits")
@RequiredArgsConstructor
public class AuditController {

    private final IAuditService auditService;
    private final IEtablissementService etablissementService;
    private final IUserService userService;
    private final IEvaluationCritereService evaluationService;
    private final ICommentaireService commentaireService;
    private final IAuthService authService;
    private final ISectionService sectionService;
    private final ICritereService critereService;
    private final INormeService normeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String listAudits(
            @RequestParam(value = "statut", required = false) StatutAudit statut,
            @RequestParam(value = "type", required = false) TypeAudit type,
            @RequestParam(value = "dateDebut", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(value = "dateFin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            Model model) {

        List<AuditDTO> audits;

        if (dateDebut != null && dateFin != null) {
            audits = auditService.getAuditsByPeriod(dateDebut, dateFin);
            model.addAttribute("dateDebut", dateDebut);
            model.addAttribute("dateFin", dateFin);
        } else if (statut != null) {
            audits = auditService.getAuditsByStatut(statut);
            model.addAttribute("selectedStatut", statut);
        } else if (type != null) {
            audits = auditService.getAuditsByType(type);
            model.addAttribute("selectedType", type);
        } else {
            audits = auditService.getAllAudits();
        }

        long auditsPlanifies = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.PLANIFIE)
                .count();

        long auditsEnCours = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.EN_COURS)
                .count();

        long auditsTermines = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.TERMINE)
                .count();

        long auditsValides = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.VALIDE)
                .count();

        model.addAttribute("currentMenu", "tous");
        model.addAttribute("audits", audits);
        model.addAttribute("statuts", StatutAudit.values());
        model.addAttribute("typesAudit", TypeAudit.values());
        model.addAttribute("totalAudits", audits.size());

        model.addAttribute("auditsPlanifies", auditsPlanifies);
        model.addAttribute("auditsEnCours", auditsEnCours);
        model.addAttribute("auditsTermines", auditsTermines);
        model.addAttribute("auditsValides", auditsValides);

        model.addAttribute("pageTitle", "Gestion des Audits");

        return "audit/shared/list";
    }


    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String newAuditForm(
            @RequestParam(value = "type", required = false) String type,
            Model model) {

        AuditCreateDTO auditDTO = new AuditCreateDTO();

        if (type != null && !type.isEmpty()) {
            try {
                TypeAudit typeAudit = TypeAudit.valueOf(type);
                auditDTO.setType(typeAudit);
            } catch (IllegalArgumentException e) {
            }
        }

        model.addAttribute("currentMenu", "planifier");
        model.addAttribute("audit", auditDTO);
        model.addAttribute("etablissements", etablissementService.getActiveEtablissements());
        model.addAttribute("auditeurs", userService.getUsersByRole(RoleType.AUDITOR));
        model.addAttribute("typesAudit", TypeAudit.values());

        List<AuditDTO> tousLesAudits = auditService.getAllAudits();

        List<AuditDTO> auditsAvecNC = tousLesAudits.stream()
                .filter(a -> a.getStatut() == StatutAudit.TERMINE || a.getStatut() == StatutAudit.VALIDE)
                .map(a -> {
                    List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(a.getId());

                    long nbNC = evaluations.stream()
                            .filter(e -> e.getStatut() == StatutConformite.NON_CONFORME ||
                                    e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME)
                            .count();

                    a.setNbNonConformes((int) nbNC);

                    return a;
                })
                .filter(a -> a.getNbNonConformes() != null && a.getNbNonConformes() > 0)
                .collect(Collectors.toList());

        log.info("✅ {} audits avec NC disponibles pour contre-visites (TERMINE: {}, VALIDE: {})",
                auditsAvecNC.size(),
                auditsAvecNC.stream().filter(a -> a.getStatut() == StatutAudit.TERMINE).count(),
                auditsAvecNC.stream().filter(a -> a.getStatut() == StatutAudit.VALIDE).count()
        );

        model.addAttribute("auditsInitiaux", auditsAvecNC);
        model.addAttribute("pageTitle", "Planifier un Audit");

        return "audit/admin/planifier";
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String createAudit(
            @Valid @ModelAttribute("audit") AuditCreateDTO auditDTO,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("etablissements", etablissementService.getActiveEtablissements());
            model.addAttribute("auditeurs", userService.getUsersByRole(RoleType.AUDITOR));
            model.addAttribute("typesAudit", TypeAudit.values());
            return "audit/admin/planifier";
        }

        try {
            AuditDTO created = auditService.createAudit(auditDTO);
            redirectAttributes.addFlashAttribute("success", "Audit planifié avec succès");
            return "redirect:/audits/" + created.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("etablissements", etablissementService.getActiveEtablissements());
            model.addAttribute("auditeurs", userService.getUsersByRole(RoleType.AUDITOR));
            model.addAttribute("typesAudit", TypeAudit.values());
            return "audit/admin/planifier";
        }
    }


    @GetMapping("/{id}")
    public String viewAudit(@PathVariable Long id, Model model) {
        try {
            log.info("Récupération des détails de l'audit {}", id);

            AuditDTO audit = auditService.getAuditById(id);

            List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(id);

            log.info("Nombre d'évaluations récupérées : {}", evaluations.size());

            long nbConformes = evaluations.stream()
                    .filter(e -> e.getStatut() == StatutConformite.CONFORME)
                    .count();

            long nbNonConformes = evaluations.stream()
                    .filter(e -> e.getStatut() == StatutConformite.NON_CONFORME)
                    .count();

            long nbPartiels = evaluations.stream()
                    .filter(e -> e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME)
                    .count();

            long nbNonApplicables = evaluations.stream()
                    .filter(e -> e.getStatut() == StatutConformite.NON_APPLICABLE)
                    .count();

            log.info("Statistiques calculées - C:{}, NC:{}, P:{}, NA:{}",
                    nbConformes, nbNonConformes, nbPartiels, nbNonApplicables);

            audit.setNbConformes((int) nbConformes);
            audit.setNbNonConformes((int) nbNonConformes);
            audit.setNbPartiels((int) nbPartiels);
            audit.setNbNonApplicables((int) nbNonApplicables);
            audit.setNbTotalCriteres(evaluations.size());

            List<CommentaireDTO> commentaires = commentaireService.getCommentairesByAudit(id);

            model.addAttribute("audit", audit);
            model.addAttribute("evaluations", evaluations);
            model.addAttribute("commentaires", commentaires);
            model.addAttribute("pageTitle", "Détails de l'Audit");

            log.info("Détails de l'audit {} chargés avec succès", id);

            return "audit/shared/details";

        } catch (Exception e) {
            log.error("Erreur lors de la récupération des détails de l'audit {}: {}", id, e.getMessage(), e);
            return "redirect:/audits?error=" + e.getMessage();
        }
    }


    @PostMapping("/{id}/commentaires")
    public String ajouterCommentaire(
            @PathVariable Long id,
            @RequestParam("texte") String texte,
            RedirectAttributes redirectAttributes) {

        try {
            User currentUser = authService.getCurrentUser();

            if (texte == null || texte.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Le commentaire ne peut pas être vide");
                return "redirect:/audits/" + id;
            }

            if (texte.length() > 500) {
                redirectAttributes.addFlashAttribute("error", "Le commentaire ne peut pas dépasser 500 caractères");
                return "redirect:/audits/" + id;
            }

            commentaireService.createCommentaire(
                    id,
                    currentUser.getId(),
                    texte.trim(),
                    null
            );

            log.info("Commentaire ajouté à l'audit {} par l'utilisateur {}", id, currentUser.getEmail());

            redirectAttributes.addFlashAttribute("success", "Commentaire publié avec succès");

        } catch (Exception e) {
            log.error("Erreur lors de l'ajout du commentaire à l'audit {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la publication du commentaire");
        }

        return "redirect:/audits/" + id;
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String editAuditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            AuditDTO audit = auditService.getAuditById(id);

            if (audit.getStatut() == StatutAudit.VALIDE || audit.getStatut() == StatutAudit.REJETE) {
                redirectAttributes.addFlashAttribute("error",
                        "Impossible de modifier un audit validé ou rejeté.");
                return "redirect:/audits/" + id;
            }

            AuditUpdateDTO updateDTO = AuditUpdateDTO.builder()
                    .id(audit.getId())
                    .type(audit.getType())
                    .dateAudit(audit.getDateAudit())
                    .etablissementId(audit.getEtablissement().getId())
                    .auditeurId(audit.getAuditeur().getId())
                    .dureeEstimee(audit.getDureeEstimee())
                    .observationGenerale(audit.getObservationGenerale())
                    .statut(audit.getStatut())
                    .dureeReelle(audit.getDureeReelle())
                    .build();

            model.addAttribute("audit", updateDTO);
            model.addAttribute("auditOriginal", audit);

            model.addAttribute("etablissements", etablissementService.getActiveEtablissements());
            model.addAttribute("auditeurs", userService.getUsersByRole(RoleType.AUDITOR));
            model.addAttribute("typesAudit", TypeAudit.values());
            model.addAttribute("statuts", StatutAudit.values());

            addStatsToModel(model);

            model.addAttribute("pageTitle", "Modifier l'Audit");

            return "audit/shared/edit";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
            return "redirect:/audits";
        }
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String updateAudit(
            @PathVariable Long id,
            @Valid @ModelAttribute("audit") AuditUpdateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            AuditDTO auditOriginal = auditService.getAuditById(id);
            model.addAttribute("auditOriginal", auditOriginal);
            model.addAttribute("etablissements", etablissementService.getActiveEtablissements());
            model.addAttribute("auditeurs", userService.getUsersByRole(RoleType.AUDITOR));
            model.addAttribute("typesAudit", TypeAudit.values());
            model.addAttribute("statuts", StatutAudit.values());
            addStatsToModel(model);

            return "audit/shared/edit";
        }

        try {
            AuditDTO updated = auditService.updateAuditFull(id, dto);

            redirectAttributes.addFlashAttribute("success",
                    "Audit #" + id + " modifié avec succès !");

            return "redirect:/audits/" + id;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la modification : " + e.getMessage());
            return "redirect:/audits/" + id + "/edit";
        }
    }

    private void addStatsToModel(Model model) {
        List<AuditDTO> allAudits = auditService.getAllAudits();

        model.addAttribute("totalAudits", allAudits.size());
        model.addAttribute("auditsPlanifies",
                allAudits.stream().filter(a -> a.getStatut() == StatutAudit.PLANIFIE).count());
        model.addAttribute("auditsEnCours",
                allAudits.stream().filter(a -> a.getStatut() == StatutAudit.EN_COURS).count());
        model.addAttribute("auditsTermines",
                allAudits.stream().filter(a -> a.getStatut() == StatutAudit.TERMINE).count());
        model.addAttribute("auditsValides",
                allAudits.stream().filter(a -> a.getStatut() == StatutAudit.VALIDE).count());
    }

    @PostMapping("/{id}/terminer")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public String terminerAudit(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            auditService.terminerAudit(id);
            redirectAttributes.addFlashAttribute("success", "Audit terminé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/audits/" + id;
    }


    @PostMapping("/{id}/valider")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String validerAudit(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            User currentUser = authService.getCurrentUser();
            auditService.validerAudit(id, currentUser.getId());
            redirectAttributes.addFlashAttribute("success", "Audit validé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/audits/" + id;
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteAudit(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            auditService.deleteAudit(id);
            redirectAttributes.addFlashAttribute("success", "Audit supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/audits";
    }


    @GetMapping("/etablissement/{etablissementId}")
    public String auditsByEtablissement(@PathVariable Long etablissementId, Model model) {
        try {
            var etablissement = etablissementService.getEtablissementById(etablissementId);
            List<AuditDTO> audits = auditService.getAuditsByEtablissement(etablissementId);

            model.addAttribute("etablissement", etablissement);
            model.addAttribute("audits", audits);
            model.addAttribute("totalAudits", audits.size());
            model.addAttribute("pageTitle", "Audits - " + etablissement.getNom());

            return "audit/shared/by-etablissement";
        } catch (Exception e) {
            return "redirect:/audits?error=" + e.getMessage();
        }
    }

    @GetMapping("/a-valider")
    @PreAuthorize("hasRole('MANAGER')")
    public String auditsAValiderManager(Model model) {
        log.info("=== CHARGEMENT PAGE A VALIDER ===");

        User manager = authService.getCurrentUser();

        List<AuditDTO> audits = auditService.getAuditsTermines();

        for (AuditDTO audit : audits) {
            try {
                List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(audit.getId());

                long nbConformes = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.CONFORME)
                        .count();

                long nbNonConformes = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.NON_CONFORME)
                        .count();

                long nbPartiels = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME)
                        .count();

                long nbNonApplicables = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.NON_APPLICABLE)
                        .count();

                audit.setNbConformes((int) nbConformes);
                audit.setNbNonConformes((int) nbNonConformes);
                audit.setNbPartiels((int) nbPartiels);
                audit.setNbNonApplicables((int) nbNonApplicables);
                audit.setNbTotalCriteres(evaluations.size());

                log.debug("Audit {} - NC:{}, C:{}, P:{}, NA:{}",
                        audit.getId(), nbNonConformes, nbConformes, nbPartiels, nbNonApplicables);

            } catch (Exception e) {
                log.error("Erreur calcul stats pour audit {}: {}", audit.getId(), e.getMessage());
                audit.setNbNonConformes(0);
                audit.setNbConformes(0);
                audit.setNbPartiels(0);
                audit.setNbNonApplicables(0);
            }
        }

        long auditsAvecNC = audits.stream()
                .filter(a -> a.getNbNonConformes() != null && a.getNbNonConformes() > 0)
                .count();

        log.info("Total audits à valider: {}", audits.size());
        log.info("Audits avec NC: {}", auditsAvecNC);

        Double tauxMoyen = audits.stream()
                .map(AuditDTO::getTauxConformite)
                .filter(t -> t != null)
                .mapToDouble(t -> t.doubleValue())
                .average()
                .orElse(0.0);

        tauxMoyen = Math.round(tauxMoyen * 100.0) / 100.0;
        int auditsValidesParMoi = auditService.countAuditsValidesParManagerCeMois(manager.getId());

        model.addAttribute("audits", audits);
        model.addAttribute("auditsAvecNC", auditsAvecNC);
        model.addAttribute("tauxMoyen", tauxMoyen);
        model.addAttribute("auditsValidesParMoi", auditsValidesParMoi);
        model.addAttribute("currentMenu", "a-valider");
        model.addAttribute("pageTitle", "Audits à Valider");

        log.info("=== FIN CHARGEMENT A VALIDER ===");

        return "audit/manager/a-valider";
    }


    @GetMapping("/supervision-validation")
    @PreAuthorize("hasRole('ADMIN')")
    public String supervisionValidation(Model model) {
        log.info("=== CHARGEMENT PAGE SUPERVISION VALIDATION ===");

        List<AuditDTO> audits = auditService.getAuditsTermines();

        for (AuditDTO audit : audits) {
            try {
                List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(audit.getId());

                long nbConformes = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.CONFORME)
                        .count();

                long nbNonConformes = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.NON_CONFORME)
                        .count();

                long nbPartiels = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME)
                        .count();

                long nbNonApplicables = evaluations.stream()
                        .filter(e -> e.getStatut() == StatutConformite.NON_APPLICABLE)
                        .count();

                audit.setNbConformes((int) nbConformes);
                audit.setNbNonConformes((int) nbNonConformes);
                audit.setNbPartiels((int) nbPartiels);
                audit.setNbNonApplicables((int) nbNonApplicables);
                audit.setNbTotalCriteres(evaluations.size());

                log.debug("Audit {} - NC:{}, C:{}, P:{}, NA:{}",
                        audit.getId(), nbNonConformes, nbConformes, nbPartiels, nbNonApplicables);

            } catch (Exception e) {
                log.error("Erreur calcul stats pour audit {}: {}", audit.getId(), e.getMessage());
                audit.setNbNonConformes(0);
                audit.setNbConformes(0);
                audit.setNbPartiels(0);
                audit.setNbNonApplicables(0);
            }
        }

        long auditsAvecNC = audits.stream()
                .filter(a -> a.getNbNonConformes() != null && a.getNbNonConformes() > 0)
                .count();

        log.info("Total audits terminés: {}", audits.size());
        log.info("Audits avec NC: {}", auditsAvecNC);

        Double tauxMoyen = audits.stream()
                .map(AuditDTO::getTauxConformite)
                .filter(t -> t != null)
                .mapToDouble(t -> t.doubleValue())
                .average()
                .orElse(0.0);

        tauxMoyen = Math.round(tauxMoyen * 100.0) / 100.0;

        int totalValidations = auditService.countValidationsCeMois();

        model.addAttribute("audits", audits);
        model.addAttribute("auditsAvecNC", auditsAvecNC);
        model.addAttribute("tauxMoyen", tauxMoyen);
        model.addAttribute("totalValidations", totalValidations);
        model.addAttribute("currentMenu", "supervision-validation");
        model.addAttribute("pageTitle", "Supervision des Validations");

        log.info("=== FIN CHARGEMENT SUPERVISION VALIDATION ===");

        return "audit/admin/supervision-validation";
    }


    @GetMapping("/{id}/valider-form")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String validerAuditForm(@PathVariable Long id, Model model) {
        try {
            AuditDTO audit = auditService.getAuditById(id);
            var evaluations = evaluationService.getEvaluationsByAudit(id);
            var nonConformites = evaluations.stream()
                    .filter(e -> "NON_CONFORME".equals(e.getStatut()) || "PARTIELLEMENT_CONFORME".equals(e.getStatut()))
                    .toList();

            model.addAttribute("audit", audit);
            model.addAttribute("evaluations", evaluations);
            model.addAttribute("nonConformites", nonConformites);
            model.addAttribute("pageTitle", "Valider l'Audit");

            return "audit/manager/valider-detail";
        } catch (Exception e) {
            return "redirect:/audits?error=" + e.getMessage();
        }
    }


    @GetMapping("/en-attente")
    @PreAuthorize("hasRole('ADMIN')")
    public String auditsEnAttente(Model model) {
        List<AuditDTO> auditsRetard = auditService.getAuditsEnRetard();
        List<AuditDTO> auditsCetteSemaine = auditService.getAuditsCetteSemaine();
        List<AuditDTO> auditsAVenir = auditService.getAuditsAVenir();

        model.addAttribute("currentMenu", "en-attente");
        model.addAttribute("auditsRetard", auditsRetard);
        model.addAttribute("auditsCetteSemaine", auditsCetteSemaine);
        model.addAttribute("auditsAVenir", auditsAVenir);
        model.addAttribute("pageTitle", "Audits en Attente");

        return "audit/admin/en-attente";
    }


    @GetMapping("/mes-audits")
    @PreAuthorize("hasRole('AUDITOR')")
    public String mesAudits(Model model) {
        User currentUser = authService.getCurrentUser();
        List<AuditDTO> audits = auditService.getAuditsByAuditeur(currentUser.getId());

        long totalAssignes = audits.size();
        long aRealiser = audits.stream().filter(a -> a.getStatut() == StatutAudit.PLANIFIE).count();
        long enCours = audits.stream().filter(a -> a.getStatut() == StatutAudit.EN_COURS).count();
        long termines = audits.stream().filter(a ->
                a.getStatut() == StatutAudit.TERMINE ||
                        a.getStatut() == StatutAudit.VALIDE).count();

        model.addAttribute("audits", audits);
        model.addAttribute("totalAssignes", totalAssignes);
        model.addAttribute("auditsARealiserCount", aRealiser);
        model.addAttribute("auditsEnCoursCount", enCours);
        model.addAttribute("auditsTerminesCount", termines);
        model.addAttribute("pageTitle", "Mes Audits Assignés");

        addAuditorStatsToModel(model, currentUser);
        model.addAttribute("currentMenu", "mes-audits");

        return "audit/auditor/mes-audits";
    }



    @GetMapping("/a-realiser")
    @PreAuthorize("hasRole('AUDITOR')")
    public String auditsARealiser(Model model) {
        User currentUser = authService.getCurrentUser();
        List<AuditDTO> audits = auditService.getAuditsByAuditeurAndStatut(
                currentUser.getId(),
                StatutAudit.PLANIFIE
        );

        model.addAttribute("audits", audits);
        model.addAttribute("totalAudits", audits.size());
        model.addAttribute("pageTitle", "Audits à Réaliser");

        addAuditorStatsToModel(model, currentUser);
        model.addAttribute("currentMenu", "a-realiser");

        return "audit/auditor/a-realiser";
    }

    @GetMapping("/en-cours")
    @PreAuthorize("hasRole('AUDITOR')")
    public String auditsEnCours(Model model) {
        User currentUser = authService.getCurrentUser();
        List<AuditDTO> audits = auditService.getAuditsByAuditeurAndStatut(
                currentUser.getId(),
                StatutAudit.EN_COURS
        );

        for (AuditDTO audit : audits) {
            try {
                int totalCriteres;

                if (audit.getType() == TypeAudit.CONTRE_VISITE && audit.getAuditInitial() != null) {
                    List<Long> critereNCIds = evaluationService
                            .getNonConformeCritereIdsByAudit(audit.getAuditInitial().getId());
                    totalCriteres = critereNCIds.size();
                } else {
                    NormeDTO norme = normeService.getNormeById(audit.getNorme().getId());
                    List<SectionDTO> sections = sectionService.getSectionsByNorme(norme.getId());

                    totalCriteres = 0;
                    for (SectionDTO section : sections) {
                        List<CritereDTO> criteres = critereService.getCriteresBySection(section.getId());
                        totalCriteres += criteres.size();
                    }
                }

                List<EvaluationCritereDTO> evaluations = evaluationService.getEvaluationsByAudit(audit.getId());

                long nbCriteresUniques = evaluations.stream()
                        .map(e -> e.getCritere().getId())
                        .distinct()
                        .count();

                Integer progression = totalCriteres > 0
                        ? Math.round((nbCriteresUniques * 100.0f) / totalCriteres)
                        : 0;

                audit.setProgression(progression);

                log.debug("Audit {} ({}) : {} critères uniques évalués sur {} ({}%)",
                        audit.getId(), audit.getType(), nbCriteresUniques, totalCriteres, progression);

            } catch (Exception e) {
                log.error("Erreur calcul progression audit {}: {}", audit.getId(), e.getMessage());
                audit.setProgression(0);
            }
        }

        model.addAttribute("audits", audits);
        model.addAttribute("totalAudits", audits.size());
        model.addAttribute("pageTitle", "Audits en Cours");

        addAuditorStatsToModel(model, currentUser);
        model.addAttribute("currentMenu", "en-cours");

        return "audit/auditor/en-cours";
    }


    @GetMapping("/termines")
    @PreAuthorize("hasRole('AUDITOR')")
    public String auditsTermines(Model model) {
        User currentUser = authService.getCurrentUser();
        List<AuditDTO> audits = auditService.getAuditsByAuditeurAndStatuts(
                currentUser.getId(),
                List.of(StatutAudit.TERMINE, StatutAudit.VALIDE, StatutAudit.REJETE)
        );

        model.addAttribute("audits", audits);
        model.addAttribute("totalAudits", audits.size());
        model.addAttribute("pageTitle", "Audits Terminés");

        addAuditorStatsToModel(model, currentUser);
        model.addAttribute("currentMenu", "termines");

        return "audit/auditor/termines";
    }


    @GetMapping("/mes-stats")
    @PreAuthorize("hasRole('AUDITOR')")
    public String mesStatistiques(Model model) {
        User currentUser = authService.getCurrentUser();
        var stats = auditService.getStatistiquesByAuditeur(currentUser.getId());

        model.addAttribute("statistics", stats);
        model.addAttribute("pageTitle", "Mes Statistiques");

        addAuditorStatsToModel(model, currentUser);
        model.addAttribute("currentMenu", "mes-stats");

        return "audit/auditor/mes-stats";
    }

    @GetMapping("/statistiques")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String statistics(Model model) {
        try {
            log.info("=== CHARGEMENT PAGE STATISTIQUES ===");

            var stats = auditService.getStatistics();

            log.info("Statistiques récupérées:");
            log.info("- Total audits: {}", stats.getTotalAudits());
            log.info("- Audits en cours: {}", stats.getAuditsEnCours());
            log.info("- Taux moyen conformité: {}", stats.getTauxMoyenConformite());

            model.addAttribute("statistics", stats);
            model.addAttribute("currentMenu", "statistiques");
            model.addAttribute("pageTitle", "Statistiques Globales");


            List<Map<String, Object>> evolutionData = auditService.getEvolutionMensuelle(12);
            log.info("Évolution mensuelle: {} mois", evolutionData != null ? evolutionData.size() : 0);
            model.addAttribute("evolutionData", evolutionData != null ? evolutionData : List.of());

            ObjectMapper mapper = new ObjectMapper();

            Map<String, Long> statutsDataMap = auditService.getRepartitionParStatut();
            log.info("Répartition par statut: {}", statutsDataMap);

            String statutsDataJson = "{}";
            try {
                statutsDataJson = mapper.writeValueAsString(statutsDataMap != null ? statutsDataMap : Map.of());
            } catch (Exception e) {
                log.error("Erreur conversion JSON statuts", e);
            }
            model.addAttribute("statutsDataJson", statutsDataJson);
            model.addAttribute("statutsData", statutsDataMap);

            Map<String, Long> typesDataMap = auditService.getRepartitionParType();
            log.info("Répartition par type: {}", typesDataMap);

            String typesDataJson = "{}";
            try {
                typesDataJson = mapper.writeValueAsString(typesDataMap != null ? typesDataMap : Map.of());
            } catch (Exception e) {
                log.error("Erreur conversion JSON types", e);
            }
            model.addAttribute("typesDataJson", typesDataJson);
            model.addAttribute("typesData", typesDataMap);
            List<Map<String, Object>> conformiteData = auditService.getConformiteMensuelle(12);
            log.info("Conformité mensuelle: {} mois", conformiteData != null ? conformiteData.size() : 0);
            model.addAttribute("conformiteData", conformiteData != null ? conformiteData : List.of());


            List<Map<String, Object>> topConformes = etablissementService.getTopConformes(10);
            log.info("Top établissements conformes: {}", topConformes != null ? topConformes.size() : 0);
            if (topConformes != null && !topConformes.isEmpty()) {
                topConformes.forEach(etab -> {
                    log.info("  - Établissement: {}, Taux: {}%", etab.get("nom"), etab.get("tauxMoyen"));
                });
            }
            model.addAttribute("topConformes", topConformes != null ? topConformes : List.of());

            List<Map<String, Object>> topRisque = etablissementService.getTopARisque(10);
            log.info("Top établissements à risque: {}", topRisque != null ? topRisque.size() : 0);
            if (topRisque != null && !topRisque.isEmpty()) {
                topRisque.forEach(etab -> {
                    log.info("  - Établissement: {}, Taux: {}%", etab.get("nom"), etab.get("tauxMoyen"));
                });
            }
            model.addAttribute("topRisque", topRisque != null ? topRisque : List.of());

            List<Map<String, Object>> performanceAuditeurs = auditService.getPerformanceAuditeurs();
            log.info("Performance auditeurs: {}", performanceAuditeurs != null ? performanceAuditeurs.size() : 0);
            if (performanceAuditeurs != null && !performanceAuditeurs.isEmpty()) {
                performanceAuditeurs.forEach(auditeur -> {
                    log.info("  - Auditeur: {}, Audits: {}, Taux: {}%, Score: {}",
                            auditeur.get("nomComplet"),
                            auditeur.get("totalAudits"),
                            auditeur.get("tauxMoyen"),
                            auditeur.get("score"));
                });
            }
            model.addAttribute("performanceAuditeurs", performanceAuditeurs != null ? performanceAuditeurs : List.of());

            log.info("=== FIN CHARGEMENT STATISTIQUES ===");

            return "audit/admin/statistiques";

        } catch (Exception e) {
            log.error("ERREUR lors du chargement des statistiques", e);
            model.addAttribute("error", "Erreur lors du chargement des statistiques: " + e.getMessage());

            model.addAttribute("statistics", new Object());
            model.addAttribute("evolutionData", List.of());
            model.addAttribute("statutsData", Map.of());
            model.addAttribute("typesData", Map.of());
            model.addAttribute("conformiteData", List.of());
            model.addAttribute("topConformes", List.of());
            model.addAttribute("topRisque", List.of());
            model.addAttribute("performanceAuditeurs", List.of());
            model.addAttribute("currentMenu", "statistiques");
            model.addAttribute("pageTitle", "Statistiques Globales");

            return "audit/admin/statistiques";
        }
    }



    @GetMapping("/historique")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String historique(
            @RequestParam(value = "dateDebut", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(value = "dateFin", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(value = "type", required = false) TypeAudit type,
            @RequestParam(value = "conformite", required = false) String conformite,
            Model model) {

        log.info("=== CHARGEMENT PAGE HISTORIQUE ===");

        List<AuditDTO> audits = auditService.getAuditsByStatut(StatutAudit.VALIDE);

        if (dateDebut != null && dateFin != null) {
            audits = audits.stream()
                    .filter(a -> !a.getDateAudit().isBefore(dateDebut) && !a.getDateAudit().isAfter(dateFin))
                    .toList();
            model.addAttribute("dateDebut", dateDebut);
            model.addAttribute("dateFin", dateFin);
            log.info("Filtré par période: {} à {}", dateDebut, dateFin);
        }

        if (type != null) {
            audits = audits.stream()
                    .filter(a -> a.getType() == type)
                    .toList();
            model.addAttribute("selectedType", type);
            log.info("Filtré par type: {}", type);
        }

        if (conformite != null && !conformite.isEmpty()) {
            audits = audits.stream()
                    .filter(a -> {
                        if (a.getTauxConformite() == null) return false;
                        double taux = a.getTauxConformite().doubleValue();
                        return switch (conformite) {
                            case "excellent" -> taux >= 90;
                            case "tres-bon" -> taux >= 80 && taux < 90;
                            case "bon" -> taux >= 70 && taux < 80;
                            case "moyen" -> taux >= 60 && taux < 70;
                            case "faible" -> taux < 60;
                            default -> true;
                        };
                    })
                    .toList();
            model.addAttribute("selectedConformite", conformite);
            log.info("Filtré par conformité: {}", conformite);
        }

        int totalAudits = audits.size();

        Double tauxMoyenPeriode = null;
        if (!audits.isEmpty()) {
            tauxMoyenPeriode = audits.stream()
                    .filter(a -> a.getTauxConformite() != null)
                    .mapToDouble(a -> a.getTauxConformite().doubleValue())
                    .average()
                    .orElse(0.0);
            tauxMoyenPeriode = Math.round(tauxMoyenPeriode * 100.0) / 100.0;
        }

        long etablissementsAudites = audits.stream()
                .map(a -> a.getEtablissement().getId())
                .distinct()
                .count();

        long auditeursActifs = audits.stream()
                .map(a -> a.getAuditeur().getId())
                .distinct()
                .count();

        log.info("Statistiques calculées:");
        log.info("- Total audits: {}", totalAudits);
        log.info("- Taux moyen: {}%", tauxMoyenPeriode);
        log.info("- Établissements audités: {}", etablissementsAudites);
        log.info("- Auditeurs actifs: {}", auditeursActifs);

        List<Map<String, Object>> evolutionData = null;
        if (totalAudits > 0) {
            try {
                Map<String, List<AuditDTO>> auditsParMois = audits.stream()
                        .collect(java.util.stream.Collectors.groupingBy(
                                a -> a.getDateAudit().format(java.time.format.DateTimeFormatter.ofPattern("MMM yyyy", java.util.Locale.FRENCH))
                        ));

                evolutionData = new java.util.ArrayList<>();
                for (Map.Entry<String, List<AuditDTO>> entry : auditsParMois.entrySet()) {
                    double tauxMoisMoyen = entry.getValue().stream()
                            .filter(a -> a.getTauxConformite() != null)
                            .mapToDouble(a -> a.getTauxConformite().doubleValue())
                            .average()
                            .orElse(0.0);

                    Map<String, Object> data = new java.util.HashMap<>();
                    data.put("mois", entry.getKey());
                    data.put("tauxMoyen", Math.round(tauxMoisMoyen * 100.0) / 100.0);
                    evolutionData.add(data);
                }
                log.info("Données d'évolution: {} mois", evolutionData.size());
            } catch (Exception e) {
                log.error("Erreur création données évolution", e);
            }
        }

        model.addAttribute("currentMenu", "historique");
        model.addAttribute("audits", audits);
        model.addAttribute("totalAudits", totalAudits);
        model.addAttribute("tauxMoyenPeriode", tauxMoyenPeriode);
        model.addAttribute("etablissementsAudites", etablissementsAudites);
        model.addAttribute("auditeursActifs", auditeursActifs);
        model.addAttribute("evolutionData", evolutionData);
        model.addAttribute("pageTitle", "Historique des Audits");

        log.info("=== FIN CHARGEMENT HISTORIQUE ===");

        return "audit/shared/historique";
    }

    @GetMapping("/contre-visites")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String contreVisites(Model model) {
        List<AuditDTO> audits = auditService.getAuditsByType(TypeAudit.CONTRE_VISITE);

        for (AuditDTO audit : audits) {
            if (audit.getAuditInitial() != null) {
                List<EvaluationCritereDTO> evaluations =
                        evaluationService.getEvaluationsByAudit(audit.getAuditInitial().getId());

                long nbNCCritiques = evaluations.stream()
                        .filter(e -> (e.getStatut() == StatutConformite.NON_CONFORME ||
                                e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME) &&
                                e.getCritere().getCriticite() == Criticite.CRITIQUE)
                        .count();

                audit.getAuditInitial().setNbNonConformitesCritiques((int) nbNCCritiques);
            }
        }

        List<AuditDTO> contreVisitesPlanifieesListe = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.PLANIFIE)
                .toList();

        List<AuditDTO> contreVisitesEnCoursListe = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.EN_COURS)
                .toList();

        List<AuditDTO> contreVisitesTermineesListe = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.TERMINE ||
                        a.getStatut() == StatutAudit.VALIDE)
                .toList();

        long contreVisitesPlanifiees = contreVisitesPlanifieesListe.size();
        long contreVisitesEnCours = contreVisitesEnCoursListe.size();
        long contreVisitesTerminees = contreVisitesTermineesListe.size();

        Double tauxAmelioration = contreVisitesTermineesListe.stream()
                .filter(cv -> cv.getTauxConformite() != null && cv.getAuditInitial() != null &&
                        cv.getAuditInitial().getTauxConformite() != null)
                .mapToDouble(cv -> cv.getTauxConformite()
                        .subtract(cv.getAuditInitial().getTauxConformite())
                        .doubleValue())
                .average()
                .orElse(0.0);

        model.addAttribute("currentMenu", "contre-visites");
        model.addAttribute("pageTitle", "Contre-visites");

        model.addAttribute("contreVisitesPlanifieesListe", contreVisitesPlanifieesListe);
        model.addAttribute("contreVisitesEnCoursListe", contreVisitesEnCoursListe);
        model.addAttribute("audits", contreVisitesTermineesListe);

        model.addAttribute("contreVisitesPlanifiees", contreVisitesPlanifiees);
        model.addAttribute("contreVisitesEnCours", contreVisitesEnCours);
        model.addAttribute("contreVisitesTerminees", contreVisitesTerminees);
        model.addAttribute("tauxAmelioration", Math.round(tauxAmelioration));

        model.addAttribute("totalAudits", audits.size());

        return "audit/admin/contre-visites";
    }


    @GetMapping("/{id}/evaluer")
    @PreAuthorize("hasRole('AUDITOR')")
    public String evaluerAudit(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User currentUser = authService.getCurrentUser();
            AuditDTO audit = auditService.getAuditById(id);


            NormeDTO norme = normeService.getNormeById(audit.getNorme().getId());
            List<SectionDTO> sections = sectionService.getSectionsByNorme(norme.getId());

            if (audit.getType() == TypeAudit.CONTRE_VISITE && audit.getAuditInitial() != null) {
                log.info("🔄 Chargement contre-visite pour audit initial #{}",
                        audit.getAuditInitial().getId());

                List<Long> critereNCIds = evaluationService
                        .getNonConformeCritereIdsByAudit(audit.getAuditInitial().getId());

                log.info("📋 Critères NC à réévaluer: {}", critereNCIds.size());

                for (SectionDTO section : sections) {
                    List<CritereDTO> toutsCriteres = critereService.getCriteresBySection(section.getId());

                    List<CritereDTO> criteresNC = toutsCriteres.stream()
                            .filter(c -> critereNCIds.contains(c.getId()))
                            .collect(Collectors.toList());

                    section.setCriteres(criteresNC);

                    log.info("Section {} : {} critères NC sur {} total",
                            section.getCode(), criteresNC.size(), toutsCriteres.size());
                }

                sections = sections.stream()
                        .filter(s -> s.getCriteres() != null && !s.getCriteres().isEmpty())
                        .collect(Collectors.toList());

                model.addAttribute("isContreVisite", true);
                model.addAttribute("auditInitial", audit.getAuditInitial());
                model.addAttribute("nbCriteresAReevaluer", critereNCIds.size());

            } else {
                log.info("📝 Audit normal - chargement de tous les critères");

                for (SectionDTO section : sections) {
                    List<CritereDTO> criteres = critereService.getCriteresBySection(section.getId());
                    section.setCriteres(criteres);
                }

                model.addAttribute("isContreVisite", false);
            }

            if (audit.getStatut() == StatutAudit.PLANIFIE) {
                auditService.demarrerAudit(id);
                audit.setStatut(StatutAudit.EN_COURS);
            }

            model.addAttribute("audit", audit);
            model.addAttribute("sections", sections);

            return "audit/evaluation/evaluer";

        } catch (Exception e) {
            log.error("Erreur évaluation audit {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/audits/mes-audits";
        }
    }

    private void addAuditorStatsToModel(Model model, User currentUser) {
        List<AuditDTO> audits = auditService.getAuditsByAuditeur(currentUser.getId());

        long aRealiser = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.PLANIFIE)
                .count();

        long enCours = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.EN_COURS)
                .count();

        long termines = audits.stream()
                .filter(a -> a.getStatut() == StatutAudit.TERMINE ||
                        a.getStatut() == StatutAudit.VALIDE)
                .count();

        model.addAttribute("auditsARealiserCount", aRealiser);
        model.addAttribute("auditsEnCoursCount", enCours);
        model.addAttribute("auditsTerminesCount", termines);
    }
}
