package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;
import org.xproce.firesafe_audit.service.norme.ICritereService;
import java.util.List;

@RestController
@RequestMapping("/api/criteres")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CritereController {

    private final ICritereService critereService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> getAllCriteres() {
        List<CritereDTO> criteres = critereService.getAllCriteres();
        return ResponseEntity.ok(ResponseDTO.success(criteres));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<CritereDTO>> getCritereById(@PathVariable Long id) {
        try {
            CritereDTO critere = critereService.getCritereById(id);
            return ResponseEntity.ok(ResponseDTO.success(critere));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "CRITERE_NOT_FOUND"));
        }
    }

    @GetMapping("/norme/{normeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> getCriteresByNorme(@PathVariable Long normeId) {
        List<CritereDTO> criteres = critereService.getCriteresByNorme(normeId);
        return ResponseEntity.ok(ResponseDTO.success(criteres));
    }

    @GetMapping("/norme/{normeId}/categorie/{categorie}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> getCriteresByNormeAndCategorie(
            @PathVariable Long normeId, @PathVariable Categorie categorie) {
        List<CritereDTO> criteres = critereService.getCriteresByNormeAndCategorie(normeId, categorie);
        return ResponseEntity.ok(ResponseDTO.success(criteres));
    }

    @GetMapping("/categorie/{categorie}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> getCriteresByCategorie(@PathVariable Categorie categorie) {
        List<CritereDTO> criteres = critereService.getCriteresByCategorie(categorie);
        return ResponseEntity.ok(ResponseDTO.success(criteres));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> searchCriteres(@RequestParam String q) {
        List<CritereDTO> criteres = critereService.searchCriteres(q);
        return ResponseEntity.ok(ResponseDTO.success(criteres));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<CritereDTO>> createCritere(@Valid @RequestBody CritereDTO dto) {
        try {
            CritereDTO critere = critereService.createCritere(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Critère créé avec succès", critere));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteCritere(@PathVariable Long id) {
        try {
            critereService.deleteCritere(id);
            return ResponseEntity.ok(ResponseDTO.success("Critère supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @PostMapping("/import/norme/{normeId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<List<CritereDTO>>> importCriteresFromExcel(
            @PathVariable Long normeId, @RequestParam("file") MultipartFile file) {
        try {
            List<CritereDTO> criteres = critereService.importCriteresFromExcel(normeId, file);
            return ResponseEntity.ok(ResponseDTO.success("Critères importés avec succès", criteres));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "IMPORT_ERROR"));
        }
    }
}
