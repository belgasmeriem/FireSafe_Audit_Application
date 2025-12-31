package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dto.common.PreuveDTO;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.file.IPreuveService;
import java.util.List;

@RestController
@RequestMapping("/api/preuves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PreuveController {

    private final IPreuveService preuveService;

    @GetMapping("/evaluation/{evaluationId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<PreuveDTO>>> getPreuvesByEvaluation(@PathVariable Long evaluationId) {
        List<PreuveDTO> preuves = preuveService.getPreuvesByEvaluation(evaluationId);
        return ResponseEntity.ok(ResponseDTO.success(preuves));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<PreuveDTO>> getPreuveById(@PathVariable Long id) {
        try {
            PreuveDTO preuve = preuveService.getPreuveById(id);
            return ResponseEntity.ok(ResponseDTO.success(preuve));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "PREUVE_NOT_FOUND"));
        }
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ByteArrayResource> downloadPreuve(@PathVariable Long id) {
        try {
            byte[] data = preuveService.downloadPreuve(id);
            PreuveDTO preuve = preuveService.getPreuveById(id);

            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + preuve.getNomFichier() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/evaluation/{evaluationId}")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER')")
    public ResponseEntity<ResponseDTO<PreuveDTO>> uploadPreuve(
            @PathVariable Long evaluationId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) {
        try {
            PreuveDTO preuve = preuveService.uploadPreuve(evaluationId, file, description);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Preuve téléversée avec succès", preuve));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPLOAD_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUDITOR', 'MANAGER', 'ADMIN')")
    public ResponseEntity<ResponseDTO<String>> deletePreuve(@PathVariable Long id) {
        try {
            preuveService.deletePreuve(id);
            return ResponseEntity.ok(ResponseDTO.success("Preuve supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @GetMapping("/evaluation/{evaluationId}/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<Long>> countByEvaluation(@PathVariable Long evaluationId) {
        long count = preuveService.countByEvaluation(evaluationId);
        return ResponseEntity.ok(ResponseDTO.success(count));
    }
}