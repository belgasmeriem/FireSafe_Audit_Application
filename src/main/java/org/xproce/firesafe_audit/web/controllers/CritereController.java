package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dao.enums.Criticite;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;
import org.xproce.firesafe_audit.service.norme.ICritereService;
import org.xproce.firesafe_audit.service.norme.INormeService;

import java.util.List;

@Controller
@RequestMapping("/normes/{normeId}/criteres")
@RequiredArgsConstructor
public class CritereController {

    private final ICritereService critereService;
    private final INormeService normeService;


    @GetMapping
    public String listCriteres(
            @PathVariable Long normeId,
            @RequestParam(value = "criticite", required = false) String criticite,
            Model model
    ) {
        NormeDTO norme = normeService.getNormeById(normeId);
        List<CritereDTO> criteres = critereService.getCriteresByNorme(normeId);

        if (criticite != null && !criticite.isEmpty()) {
            criteres = criteres.stream()
                    .filter(c -> c.getCriticite() != null &&
                            c.getCriticite().toString().equalsIgnoreCase(criticite))
                    .toList();
        }

        model.addAttribute("norme", norme);
        model.addAttribute("criteres", criteres);
        model.addAttribute("currentFilter", criticite);

        return "critere/list";
    }

    @GetMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String showImportForm(@PathVariable Long normeId, Model model) {
        System.out.println("🔍 Chargement de la norme avec ID : " + normeId);

        NormeDTO norme = normeService.getNormeById(normeId);

        System.out.println("📋 Norme trouvée : " + (norme != null ? norme.getNom() : "NULL"));
        System.out.println("📋 Code : " + (norme != null ? norme.getCode() : "NULL"));
        System.out.println("📋 Version : " + (norme != null ? norme.getVersion() : "NULL"));

        if (norme == null) {
            throw new RuntimeException("Norme introuvable avec l'ID : " + normeId);
        }

        model.addAttribute("norme", norme);
        return "critere/import";
    }


    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public String importCriteres(
            @PathVariable Long normeId,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        System.out.println("📤 POST /import reçu pour norme ID : " + normeId);
        System.out.println("📁 Fichier reçu : " + (file != null ? file.getOriginalFilename() : "NULL"));

        try {
            if (file == null || file.isEmpty()) {
                System.out.println("⚠️ Fichier vide détecté");

                NormeDTO norme = normeService.getNormeById(normeId);
                model.addAttribute("norme", norme);
                model.addAttribute("error", "Le fichier est vide. Veuillez sélectionner un fichier Excel.");
                return "critere/import";
            }

            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                System.out.println("⚠️ Format de fichier invalide : " + fileName);

                NormeDTO norme = normeService.getNormeById(normeId);
                model.addAttribute("norme", norme);
                model.addAttribute("error", "Format de fichier invalide. Seuls les fichiers .xlsx et .xls sont acceptés.");
                return "critere/import";
            }

            System.out.println("✅ Fichier valide, démarrage de l'import...");

            List<CritereDTO> criteresImportes = critereService.importCriteresFromExcel(normeId, file);
            int nombreImportes = criteresImportes != null ? criteresImportes.size() : 0;
            System.out.println("✅ Import réussi : " + nombreImportes + " critère(s)");

            redirectAttributes.addFlashAttribute("success",
                    nombreImportes + " critère(s) importé(s) avec succès !");

            return "redirect:/normes/" + normeId;

        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'import : " + e.getMessage());
            e.printStackTrace();

            NormeDTO norme = normeService.getNormeById(normeId);
            model.addAttribute("norme", norme);
            model.addAttribute("error", "Erreur lors de l'importation : " + e.getMessage());

            return "critere/import";
        }
    }
}