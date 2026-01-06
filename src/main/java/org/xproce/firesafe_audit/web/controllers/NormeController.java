package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;
import org.xproce.firesafe_audit.dto.norme.NormeCreateDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;
import org.xproce.firesafe_audit.service.norme.ICritereService;
import org.xproce.firesafe_audit.service.norme.INormeService;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/normes")
@RequiredArgsConstructor
public class NormeController {

    private final INormeService normeService;
    private final ICritereService critereService;

    @GetMapping
    public String listNormes(
            @RequestParam(value = "filter", required = false) String filter,
            Model model
    ) {
        List<NormeDTO> normes;

        if (filter != null) {
            switch (filter) {
                case "actives":
                    normes = normeService.getActiveNormes();
                    break;
                case "maroc":
                    normes = normeService.getNormesByPays("Maroc");
                    break;
                case "sans-criteres":
                    normes = normeService.getActiveNormes().stream()
                            .filter(n -> n.getNombreCriteres() == 0)
                            .toList();
                    break;
                default:
                    normes = normeService.getActiveNormes();
            }
        } else {
            normes = normeService.getActiveNormes();
        }

        model.addAttribute("normes", normes);

        long totalNormes = normes.size();
        long normesActives = normes.stream()
                .filter(n -> n.getDateVigueur() != null &&
                        !n.getDateVigueur().isAfter(java.time.LocalDate.now()))
                .count();
        long normesMaroc = normes.stream()
                .filter(n -> "Maroc".equalsIgnoreCase(n.getPays()))
                .count();
        long normesSansCriteres = normes.stream()
                .filter(n -> n.getNombreCriteres() == 0)
                .count();

        model.addAttribute("totalNormes", totalNormes);
        model.addAttribute("normesActives", normesActives);
        model.addAttribute("normesMaroc", normesMaroc);
        model.addAttribute("normesSansCriteres", normesSansCriteres);
        model.addAttribute("currentFilter", filter != null ? filter : "all");

        return "norme/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateForm(Model model) {
        NormeCreateDTO dto = new NormeCreateDTO();
        dto.setDateVigueur(LocalDate.now());
        model.addAttribute("norme", dto);
        model.addAttribute("typesEtablissements", TypeEtablissement.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Nouvelle Norme");
        return "norme/add";
    }


    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveNorme(
            @Valid @ModelAttribute("norme") NormeCreateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("typesEtablissements", TypeEtablissement.values());
            model.addAttribute("isEdit", false);
            return "norme/add";
        }

        try {
            NormeDTO created = normeService.createNorme(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Norme créée avec succès : " + created.getNom());
            return "redirect:/normes/" + created.getId();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("typesEtablissements", TypeEtablissement.values());
            model.addAttribute("isEdit", false);
            return "norme/add";
        }
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        try {
            System.out.println("🔍 Chargement des détails de la norme ID : " + id);

            NormeDTO norme = normeService.getNormeById(id);
            System.out.println("📋 Norme trouvée : " + norme.getNom());
            model.addAttribute("norme", norme);

            List<CritereDTO> criteres = critereService.getCriteresByNorme(id);
            System.out.println("📊 Nombre de critères trouvés : " + criteres.size());
            model.addAttribute("criteres", criteres);

            long critiquesCount = criteres.stream()
                    .filter(c -> c.getCriticite() != null &&
                            c.getCriticite().toString().equals("CRITIQUE"))
                    .count();

            long importantesCount = criteres.stream()
                    .filter(c -> c.getCriticite() != null &&
                            c.getCriticite().toString().equals("IMPORTANTE"))
                    .count();

            long normalesCount = criteres.stream()
                    .filter(c -> c.getCriticite() != null &&
                            c.getCriticite().toString().equals("NORMALE"))
                    .count();

            long faiblesCount = criteres.stream()
                    .filter(c -> c.getCriticite() != null &&
                            c.getCriticite().toString().equals("FAIBLE"))
                    .count();

            System.out.println("📈 Stats - Critiques: " + critiquesCount +
                    ", Importantes: " + importantesCount +
                    ", Normales: " + normalesCount +
                    ", Faibles: " + faiblesCount);

            model.addAttribute("critiquesCount", critiquesCount);
            model.addAttribute("importantesCount", importantesCount);
            model.addAttribute("normalesCount", normalesCount);
            model.addAttribute("faiblesCount", faiblesCount);
            model.addAttribute("pageTitle", norme.getNom());

            return "norme/details";
        } catch (Exception e) {
            return "redirect:/normes?error=" + e.getMessage();
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            NormeDTO norme = normeService.getNormeById(id);

            NormeCreateDTO dto = new NormeCreateDTO();
            dto.setCode(norme.getCode());
            dto.setVersion(norme.getVersion());
            dto.setNom(norme.getNom());
            dto.setDescription(norme.getDescription());
            dto.setPays(norme.getPays());
            dto.setDateVigueur(norme.getDateVigueur());
            dto.setTypesEtablissements(norme.getTypesEtablissements());

            model.addAttribute("norme", dto);
            model.addAttribute("typesEtablissements", TypeEtablissement.values());
            model.addAttribute("normeId", id);
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Modifier - " + norme.getNom());

            return "norme/edit";
        } catch (Exception e) {
            return "redirect:/normes?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateNorme(
            @PathVariable Long id,
            @Valid @ModelAttribute("norme") NormeCreateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            model.addAttribute("typesEtablissements", TypeEtablissement.values());
            model.addAttribute("normeId", id);
            model.addAttribute("isEdit", true);
            return "norme/edit";
        }

        try {
            NormeDTO updated = normeService.updateNorme(id, dto);
            redirectAttributes.addFlashAttribute("success",
                    "Norme mise à jour avec succès");
            return "redirect:/normes/" + id;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("normeId", id);
            model.addAttribute("typesEtablissements", TypeEtablissement.values());
            model.addAttribute("isEdit", true);
            return "norme/edit";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteNorme(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        try {
            normeService.deleteNorme(id);
            redirectAttributes.addFlashAttribute("success",
                    "Norme désactivée avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/normes";
    }
}