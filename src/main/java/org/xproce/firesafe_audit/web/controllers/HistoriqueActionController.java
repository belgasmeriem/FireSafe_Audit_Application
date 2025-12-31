package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dao.entities.HistoriqueAction;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.history.IHistoriqueActionService;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/historique")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class HistoriqueActionController {

    private final IHistoriqueActionService historiqueService;

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<HistoriqueAction>>> getHistoriqueByUser(@PathVariable Long userId) {
        List<HistoriqueAction> historique = historiqueService.getHistoriqueByUser(userId);
        return ResponseEntity.ok(ResponseDTO.success(historique));
    }

    @GetMapping("/entite/{entite}/{entiteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<HistoriqueAction>>> getHistoriqueByEntite(
            @PathVariable String entite, @PathVariable Long entiteId) {
        List<HistoriqueAction> historique = historiqueService.getHistoriqueByEntite(entite, entiteId);
        return ResponseEntity.ok(ResponseDTO.success(historique));
    }

    @GetMapping("/period")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<HistoriqueAction>>> getHistoriqueByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<HistoriqueAction> historique = historiqueService.getHistoriqueByPeriod(debut, fin);
        return ResponseEntity.ok(ResponseDTO.success(historique));
    }

    @DeleteMapping("/clean/{days}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO<String>> cleanOldHistory(@PathVariable int days) {
        try {
            historiqueService.cleanOldHistory(days);
            return ResponseEntity.ok(ResponseDTO.success("Historique ancien nettoyé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "CLEAN_ERROR"));
        }
    }
}
