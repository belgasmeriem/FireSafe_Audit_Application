package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.entities.PhotoEtablissement;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.etablissement.IPhotoEtablissementService;
import java.util.List;

@RestController
@RequestMapping("/api/photos-etablissement")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class PhotoEtablissementController {

    private final IPhotoEtablissementService photoService;

    @GetMapping("/etablissement/{etablissementId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<PhotoEtablissement>>> getPhotosByEtablissement(
            @PathVariable Long etablissementId) {
        List<PhotoEtablissement> photos = photoService.getPhotosByEtablissement(etablissementId);
        return ResponseEntity.ok(ResponseDTO.success(photos));
    }

    @PostMapping("/etablissement/{etablissementId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<PhotoEtablissement>> uploadPhoto(
            @PathVariable Long etablissementId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Boolean principale) {
        try {
            PhotoEtablissement photo = photoService.uploadPhoto(etablissementId, file, description, principale);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Photo téléversée avec succès", photo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPLOAD_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<String>> deletePhoto(@PathVariable Long id) {
        try {
            photoService.deletePhoto(id);
            return ResponseEntity.ok(ResponseDTO.success("Photo supprimée avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @PutMapping("/{id}/set-principale")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<String>> setPhotoPrincipale(@PathVariable Long id) {
        try {
            photoService.setPhotoPrincipale(id);
            return ResponseEntity.ok(ResponseDTO.success("Photo définie comme principale"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "SET_PRINCIPALE_ERROR"));
        }
    }
}