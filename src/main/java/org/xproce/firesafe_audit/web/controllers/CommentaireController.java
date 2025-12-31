package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dto.common.CommentaireDTO;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.audit.ICommentaireService;
import java.util.List;

@RestController
@RequestMapping("/api/commentaires")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class CommentaireController {

    private final ICommentaireService commentaireService;

    @GetMapping("/audit/{auditId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<List<CommentaireDTO>>> getCommentairesByAudit(@PathVariable Long auditId) {
        List<CommentaireDTO> commentaires = commentaireService.getCommentairesByAudit(auditId);
        return ResponseEntity.ok(ResponseDTO.success(commentaires));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<CommentaireDTO>> getCommentaireById(@PathVariable Long id) {
        try {
            CommentaireDTO commentaire = commentaireService.getCommentaireById(id);
            return ResponseEntity.ok(ResponseDTO.success(commentaire));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseDTO.error(e.getMessage(), "COMMENTAIRE_NOT_FOUND"));
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<CommentaireDTO>> createCommentaire(
            @RequestParam Long auditId,
            @RequestParam Long auteurId,
            @RequestParam String contenu,
            @RequestParam(required = false) Long parentId) {
        try {
            CommentaireDTO commentaire = commentaireService.createCommentaire(auditId, auteurId, contenu, parentId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Commentaire ajouté avec succès", commentaire));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CREATE_ERROR"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<CommentaireDTO>> updateCommentaire(@PathVariable Long id, @RequestParam String contenu) {
        try {
            CommentaireDTO commentaire = commentaireService.updateCommentaire(id, contenu);
            return ResponseEntity.ok(ResponseDTO.success("Commentaire modifié avec succès", commentaire));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "UPDATE_ERROR"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<String>> deleteCommentaire(@PathVariable Long id) {
        try {
            commentaireService.deleteCommentaire(id);
            return ResponseEntity.ok(ResponseDTO.success("Commentaire supprimé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "DELETE_ERROR"));
        }
    }

    @GetMapping("/audit/{auditId}/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'AUDITOR')")
    public ResponseEntity<ResponseDTO<Long>> countByAudit(@PathVariable Long auditId) {
        long count = commentaireService.countByAudit(auditId);
        return ResponseEntity.ok(ResponseDTO.success(count));
    }
}