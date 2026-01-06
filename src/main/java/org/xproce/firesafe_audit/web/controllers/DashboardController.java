package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
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
            DashboardDTO dashboardData = dashboardService.getDashboardData();

            model.addAttribute("dashboard", dashboardData);
            model.addAttribute("pageTitle", "Tableau de bord - FireSafety Audit");

            model.addAttribute("totalEtablissements", dashboardData.getTotalEtablissements());
            model.addAttribute("totalAudits", dashboardData.getTotalAudits());
            model.addAttribute("auditsEnCours", dashboardData.getAuditsEnCours());
            model.addAttribute("auditsTermines", dashboardData.getAuditsTermines());
            model.addAttribute("auditsValides", dashboardData.getAuditsValides());

            if (dashboardData.getTotalAudits() > 0) {
                model.addAttribute("pourcentageEnCours",
                        formatPercentage(dashboardData.getAuditsEnCours() * 100.0 / dashboardData.getTotalAudits()));
                model.addAttribute("pourcentageTermines",
                        formatPercentage(dashboardData.getAuditsTermines() * 100.0 / dashboardData.getTotalAudits()));
                model.addAttribute("pourcentageValides",
                        formatPercentage(dashboardData.getAuditsValides() * 100.0 / dashboardData.getTotalAudits()));
            } else {
                model.addAttribute("pourcentageEnCours", "0%");
                model.addAttribute("pourcentageTermines", "0%");
                model.addAttribute("pourcentageValides", "0%");
            }

            model.addAttribute("tauxMoyenConformite",
                    formatPercentage(dashboardData.getTauxMoyenConformite() != null ? dashboardData.getTauxMoyenConformite() : 0));
            model.addAttribute("nonConformitesCritiques", dashboardData.getNonConformitesCritiques());
            model.addAttribute("actionsNonCorrigees", dashboardData.getActionsNonCorrigees());

            model.addAttribute("evolutionMensuelle", dashboardData.getEvolutionMensuelle());
            model.addAttribute("repartitionParType", dashboardData.getRepartitionParType());
            model.addAttribute("topEtablissements", dashboardData.getTopEtablissements());
            model.addAttribute("etablissementsARisque", dashboardData.getEtablissementsARisque());

            String alertLevel = "success";
            if (dashboardData.getNonConformitesCritiques() > 10) {
                alertLevel = "danger";
            } else if (dashboardData.getNonConformitesCritiques() > 5) {
                alertLevel = "warning";
            }
            model.addAttribute("alertLevel", alertLevel);

            return "dashboard/index";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement du dashboard: " + e.getMessage());
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