package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.etablissement.*;
import org.xproce.firesafe_audit.service.etablissement.IEtablissementService;
import org.xproce.firesafe_audit.service.norme.INormeService;

@Slf4j
@Controller
@RequestMapping("/etablissements")
@RequiredArgsConstructor
public class EtablissementController {

    private final IEtablissementService etablissementService;
    private final INormeService normeService;

    @GetMapping
    public String list(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "type", required = false) TypeEtablissement type,
            @RequestParam(value = "ville", required = false) String ville,
            Model model) {

        var etablissements = search != null && !search.isEmpty()
                ? etablissementService.searchEtablissements(search)
                : type != null
                ? etablissementService.getEtablissementsByType(type)
                : ville != null && !ville.isEmpty()
                ? etablissementService.getEtablissementsByVille(ville)
                : etablissementService.getActiveEtablissements();

        model.addAttribute("etablissements", etablissements);
        model.addAttribute("types", TypeEtablissement.values());
        model.addAttribute("search", search);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedVille", ville);
        model.addAttribute("totalEtablissements", etablissements.size());
        model.addAttribute("pageTitle", "Gestion des Établissements");

        return "etablissement/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("etablissement", new EtablissementCreateDTO());
        model.addAttribute("types", TypeEtablissement.values());
        model.addAttribute("normes", normeService.getActiveNormes());
        model.addAttribute("pageTitle", "Nouvel Établissement");
        return "etablissement/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
            @Valid @ModelAttribute("etablissement") EtablissementCreateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("normes", normeService.getActiveNormes());
            return "etablissement/add";
        }

        try {
            EtablissementDTO created = etablissementService.createEtablissement(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Établissement créé avec succès");
            return "redirect:/etablissements";
        } catch (Exception e) {
            log.error("Erreur lors de la création", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("normes", normeService.getActiveNormes());
            return "etablissement/add";
        }
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        try {
            EtablissementDTO etablissement = etablissementService.getEtablissementById(id);
            model.addAttribute("etablissement", etablissement);
            model.addAttribute("pageTitle", etablissement.getNom());
            return "etablissement/details";
        } catch (Exception e) {
            log.error("Erreur lors de la récupération", e);
            return "redirect:/etablissements?error=" + e.getMessage();
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            EtablissementDTO etablissement = etablissementService.getEtablissementById(id);

            EtablissementUpdateDTO updateDTO = EtablissementUpdateDTO.builder()
                    .nom(etablissement.getNom())
                    .description(etablissement.getDescription())
                    .type(etablissement.getType())
                    .adresse(etablissement.getAdresse())
                    .ville(etablissement.getVille())
                    .codePostal(etablissement.getCodePostal())
                    .pays(etablissement.getPays())
                    .capaciteAccueil(etablissement.getCapaciteAccueil())
                    .nombreEtages(etablissement.getNombreEtages())
                    .surfaceTotale(etablissement.getSurfaceTotale())
                    .responsableNom(etablissement.getResponsableNom())
                    .responsableEmail(etablissement.getResponsableEmail())
                    .responsableTelephone(etablissement.getResponsableTelephone())
                    .normeId(etablissement.getNormeId())
                    .actif(etablissement.getActif())
                    .build();

            model.addAttribute("etablissement", updateDTO);
            model.addAttribute("etablissementId", id);
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("normes", normeService.getActiveNormes());
            model.addAttribute("pageTitle", "Modifier " + etablissement.getNom());
            return "etablissement/edit";
        } catch (Exception e) {
            log.error("Erreur lors de la récupération", e);
            return "redirect:/etablissements?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("etablissement") EtablissementUpdateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("etablissementId", id);
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("normes", normeService.getActiveNormes());
            return "etablissement/edit";
        }

        try {
            etablissementService.updateEtablissement(id, dto);
            redirectAttributes.addFlashAttribute("success",
                    "Établissement mis à jour avec succès");
            return "redirect:/etablissements/" + id;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("etablissementId", id);
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("normes", normeService.getActiveNormes());
            return "etablissement/edit";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            etablissementService.deleteEtablissement(id);
            redirectAttributes.addFlashAttribute("success", "Établissement désactivé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/etablissements";
    }


    @GetMapping("/type/{typeCode}")
    public String filterByType(
            @PathVariable String typeCode,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            TypeEtablissement type = findTypeByCode(typeCode.toUpperCase());

            if (type == null) {
                throw new IllegalArgumentException("Type non trouvé: " + typeCode);
            }

            var etablissements = etablissementService.getEtablissementsByType(type);

            model.addAttribute("isFiltered", true);
            model.addAttribute("filteredType", type);
            model.addAttribute("filteredTypeLibelle", type.getLibelle());
            model.addAttribute("filteredTypeCode", type.getCode());

            model.addAttribute("etablissements", etablissements);
            model.addAttribute("types", TypeEtablissement.values());
            model.addAttribute("selectedType", type);
            model.addAttribute("totalEtablissements", etablissements.size());
            model.addAttribute("pageTitle", type.getLibelle());

            return "etablissement/list";

        } catch (IllegalArgumentException e) {
            log.error("Type d'établissement invalide: {}", typeCode);
            redirectAttributes.addFlashAttribute("error", "Type d'établissement invalide");
            return "redirect:/etablissements";
        }
    }

    private TypeEtablissement findTypeByCode(String code) {
        for (TypeEtablissement type : TypeEtablissement.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}