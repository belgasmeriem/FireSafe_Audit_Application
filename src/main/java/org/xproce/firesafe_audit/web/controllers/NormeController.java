package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.dto.norme.NormeCreateDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;
import org.xproce.firesafe_audit.service.norme.INormeService;
import java.util.List;

@RestController
@RequestMapping("/api/normes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class NormeController {

    private final INormeService normeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<NormeDTO>>> getAllNormes() {
        List<NormeDTO> normes = normeService.getAllNormes();
        return ResponseEntity.ok(ResponseDTO.success(normes));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<NormeDTO>>> getActiveNormes() {
        List<NormeDTO> normes = normeService.getActiveNormes();
        return ResponseEntity.ok(ResponseDTO.success(normes));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<NormeDTO>> getNormeById(@PathVariable Long id) {
        try {
            NormeDTO norme = normeService.getNormeById(id);
            return ResponseEntity.ok(ResponseDTO.success(norme));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "NORME_NOT_FOUND"));
        }
    }

    @GetMapping("/pays/{pays}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<NormeDTO>>> getNormesByPays(@PathVariable String pays) {
        List<NormeDTO> normes = normeService.getNormesByPays(pays);
        return ResponseEntity.ok(ResponseDTO.success(normes));
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<NormeDTO>>> getNormesByTypeEtablissement(@PathVariable TypeEtablissement type) {
        List<NormeDTO> normes = normeService.getNormesByTypeEtablissement(type);
        return ResponseEntity.ok(ResponseDTO.success(normes));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<NormeDTO>>> searchNormes(@RequestParam String q) {
        List<NormeDTO> normes = normeService.searchNormes(q);
        return ResponseEntity.ok(ResponseDTO.success(normes));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<NormeDTO>> createNorme(@Valid @RequestBody NormeCreateDTO dto) {
        try {
            NormeDTO norme = normeService.createNorme(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Norme créée avec succès", norme));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<NormeDTO>> updateNorme(@PathVariable Long id, @Valid @RequestBody NormeCreateDTO dto) {
        try {
            NormeDTO norme = normeService.updateNorme(id, dto);
            return ResponseEntity.ok(ResponseDTO.success("Norme mise à jour avec succès", norme));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deleteNorme(@PathVariable Long id) {
        try {
            normeService.deleteNorme(id);
            return ResponseEntity.ok(ResponseDTO.success("Norme désactivée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @GetMapping("/count/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<Long>> countActiveNormes() {
        long count = normeService.countActiveNormes();
        return ResponseEntity.ok(ResponseDTO.success(count));
    }
}