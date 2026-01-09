package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xproce.firesafe_audit.dto.dashboard.DashboardDTO;
import org.xproce.firesafe_audit.service.dashboard.IDashboardService;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final IDashboardService dashboardService;
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM yyyy");

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public String showDashboard(Model model) {
        try {
            log.info("=== DÉBUT CHARGEMENT DASHBOARD ===");

            DashboardDTO dashboardData = dashboardService.getDashboardData();

            log.info("Données du dashboard récupérées:");
            log.info("- Total établissements: {}", dashboardData.getTotalEtablissements());
            log.info("- Total audits: {}", dashboardData.getTotalAudits());
            log.info("- Audits en cours: {}", dashboardData.getAuditsEnCours());
            log.info("- Audits terminés: {}", dashboardData.getAuditsTermines());
            log.info("- Audits validés: {}", dashboardData.getAuditsValides());
            log.info("- Non-conformités critiques: {}", dashboardData.getNonConformitesCritiques());
            log.info("- Actions non corrigées: {}", dashboardData.getActionsNonCorrigees());

            // Statistiques de base
            model.addAttribute("dashboard", dashboardData);
            model.addAttribute("pageTitle", "Tableau de bord - FireSafety Audit");
            model.addAttribute("totalEtablissements", dashboardData.getTotalEtablissements());
            model.addAttribute("totalAudits", dashboardData.getTotalAudits());
            model.addAttribute("auditsEnCours", dashboardData.getAuditsEnCours());
            model.addAttribute("auditsTermines", dashboardData.getAuditsTermines());
            model.addAttribute("auditsValides", dashboardData.getAuditsValides());

            // Calcul des pourcentages
            if (dashboardData.getTotalAudits() > 0) {
                String pctEnCours = formatPercentage(dashboardData.getAuditsEnCours() * 100.0 / dashboardData.getTotalAudits());
                String pctTermines = formatPercentage(dashboardData.getAuditsTermines() * 100.0 / dashboardData.getTotalAudits());
                String pctValides = formatPercentage(dashboardData.getAuditsValides() * 100.0 / dashboardData.getTotalAudits());

                log.info("Pourcentages calculés: EnCours={}, Terminés={}, Validés={}", pctEnCours, pctTermines, pctValides);

                model.addAttribute("pourcentageEnCours", pctEnCours);
                model.addAttribute("pourcentageTermines", pctTermines);
                model.addAttribute("pourcentageValides", pctValides);
            } else {
                model.addAttribute("pourcentageEnCours", "0%");
                model.addAttribute("pourcentageTermines", "0%");
                model.addAttribute("pourcentageValides", "0%");
            }

            model.addAttribute("tauxMoyenConformite",
                    formatPercentage(dashboardData.getTauxMoyenConformite() != null ? dashboardData.getTauxMoyenConformite() : 0));
            model.addAttribute("nonConformitesCritiques", dashboardData.getNonConformitesCritiques());
            model.addAttribute("actionsNonCorrigees", dashboardData.getActionsNonCorrigees());

            // ✅ ÉTABLISSEMENTS À RISQUE
            List<Map<String, Object>> etablissementsARisque = dashboardData.getEtablissementsARisque();
            if (etablissementsARisque != null && !etablissementsARisque.isEmpty()) {
                log.info("=== ÉTABLISSEMENTS À RISQUE ===");
                log.info("Nombre d'établissements à risque: {}", etablissementsARisque.size());
                etablissementsARisque.forEach(etab -> {
                    log.info("  - Établissement: {}, Type: {}, Taux: {}%",
                            etab.get("etablissement"),
                            etab.get("type"),
                            etab.get("taux"));
                });
                model.addAttribute("etablissementsARisque", etablissementsARisque);
            } else {
                log.warn("Aucun établissement à risque trouvé");
                model.addAttribute("etablissementsARisque", List.of());
            }

            // ✅ TOP ÉTABLISSEMENTS
            List<Map<String, Object>> topEtablissements = dashboardData.getTopEtablissements();
            if (topEtablissements != null && !topEtablissements.isEmpty()) {
                log.info("=== TOP ÉTABLISSEMENTS ===");
                log.info("Nombre d'établissements dans le top: {}", topEtablissements.size());
                topEtablissements.forEach(etab -> {
                    log.info("  - Établissement: {}, Taux: {}%",
                            etab.get("etablissement"),
                            etab.get("taux"));
                });
                model.addAttribute("topEtablissements", topEtablissements);
            } else {
                log.warn("Aucun établissement dans le top");
                model.addAttribute("topEtablissements", List.of());
            }

            // ✅ ÉVOLUTION MENSUELLE
            List<Map<String, Object>> evolutionMensuelle = dashboardData.getEvolutionMensuelle();
            if (evolutionMensuelle != null && !evolutionMensuelle.isEmpty()) {
                log.info("=== ÉVOLUTION MENSUELLE ===");
                log.info("Nombre de mois: {}", evolutionMensuelle.size());
                evolutionMensuelle.forEach(mois -> {
                    log.info("  - Mois: {}, Taux: {}%",
                            mois.get("mois"),
                            mois.get("taux"));
                });
                model.addAttribute("evolutionMensuelle", evolutionMensuelle);
            } else {
                log.warn("Aucune donnée d'évolution mensuelle");
                model.addAttribute("evolutionMensuelle", List.of());
            }

            // ✅ RÉPARTITION PAR TYPE
            List<Map<String, Object>> repartitionParType = dashboardData.getRepartitionParType();
            if (repartitionParType != null && !repartitionParType.isEmpty()) {
                log.info("=== RÉPARTITION PAR TYPE ===");
                log.info("Nombre de types: {}", repartitionParType.size());
                repartitionParType.forEach(type -> {
                    log.info("  - Type: {}, Count: {}",
                            type.get("type"),
                            type.get("count"));
                });
                model.addAttribute("repartitionParType", repartitionParType);
            } else {
                log.warn("Aucune donnée de répartition par type");
                model.addAttribute("repartitionParType", List.of());
            }

            // Niveau d'alerte
            String alertLevel = "success";
            if (dashboardData.getNonConformitesCritiques() > 10) {
                alertLevel = "danger";
            } else if (dashboardData.getNonConformitesCritiques() > 5) {
                alertLevel = "warning";
            }
            model.addAttribute("alertLevel", alertLevel);

            log.info("=== FIN CHARGEMENT DASHBOARD ===");
            return "dashboard/index";

        } catch (Exception e) {
            log.error("ERREUR lors du chargement du dashboard", e);
            model.addAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());

            // Valeurs par défaut en cas d'erreur
            model.addAttribute("totalEtablissements", 0L);
            model.addAttribute("totalAudits", 0L);
            model.addAttribute("auditsEnCours", 0L);
            model.addAttribute("auditsTermines", 0L);
            model.addAttribute("auditsValides", 0L);
            model.addAttribute("nonConformitesCritiques", 0L);
            model.addAttribute("actionsNonCorrigees", 0L);
            model.addAttribute("pourcentageEnCours", "0%");
            model.addAttribute("pourcentageTermines", "0%");
            model.addAttribute("pourcentageValides", "0%");
            model.addAttribute("etablissementsARisque", List.of());
            model.addAttribute("topEtablissements", List.of());
            model.addAttribute("evolutionMensuelle", List.of());
            model.addAttribute("repartitionParType", List.of());

            return "dashboard/index";
        }
    }

    private String formatPercentage(Double value) {
        if (value == null) return "0%";
        return DECIMAL_FORMAT.format(value) + "%";
    }

    private String formatPercentage(long count, long total) {
        if (total == 0) return "0%";
        return DECIMAL_FORMAT.format(count * 100.0 / total) + "%";
    }
}