package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.dto.etablissement.*;
import org.xproce.firesafe_audit.service.etablissement.IEtablissementService;
import java.util.List;

@RestController
@RequestMapping("/api/etablissements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class EtablissementController {

    private final IEtablissementService etablissementService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EtablissementDTO>>> getAllEtablissements() {
        List<EtablissementDTO> etablissements = etablissementService.getAllEtablissements();
        return ResponseEntity.ok(ResponseDTO.success(etablissements));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EtablissementDTO>>> getActiveEtablissements() {
        List<EtablissementDTO> etablissements = etablissementService.getActiveEtablissements();
        return ResponseEntity.ok(ResponseDTO.success(etablissements));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<EtablissementDTO>> getEtablissementById(@PathVariable Long id) {
        try {
            EtablissementDTO etablissement = etablissementService.getEtablissementById(id);
            return ResponseEntity.ok(ResponseDTO.success(etablissement));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "ETABLISSEMENT_NOT_FOUND"));
        }
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EtablissementDTO>>> getEtablissementsByType(@PathVariable TypeEtablissement type) {
        List<EtablissementDTO> etablissements = etablissementService.getEtablissementsByType(type);
        return ResponseEntity.ok(ResponseDTO.success(etablissements));
    }

    @GetMapping("/ville/{ville}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EtablissementDTO>>> getEtablissementsByVille(@PathVariable String ville) {
        List<EtablissementDTO> etablissements = etablissementService.getEtablissementsByVille(ville);
        return ResponseEntity.ok(ResponseDTO.success(etablissements));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<EtablissementDTO>>> searchEtablissements(@RequestParam String q) {
        List<EtablissementDTO> etablissements = etablissementService.searchEtablissements(q);
        return ResponseEntity.ok(ResponseDTO.success(etablissements));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<EtablissementDTO>> createEtablissement(@Valid @RequestBody EtablissementCreateDTO dto) {
        try {
            EtablissementDTO etablissement = etablissementService.createEtablissement(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Établissement créé avec succès", etablissement));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<EtablissementDTO>> updateEtablissement(
            @PathVariable Long id, @Valid @RequestBody EtablissementUpdateDTO dto) {
        try {
            EtablissementDTO etablissement = etablissementService.updateEtablissement(id, dto);
            return ResponseEntity.ok(ResponseDTO.success("Établissement mis à jour avec succès", etablissement));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteEtablissement(@PathVariable Long id) {
        try {
            etablissementService.deleteEtablissement(id);
            return ResponseEntity.ok(ResponseDTO.success("Établissement désactivé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<Long>> countActiveEtablissements() {
        long count = etablissementService.countActiveEtablissements();
        return ResponseEntity.ok(ResponseDTO.success(count));
    }

    @GetMapping("/count/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<Long>> countByType(@PathVariable TypeEtablissement type) {
        long count = etablissementService.countByType(type);
        return ResponseEntity.ok(ResponseDTO.success(count));
    }
}
