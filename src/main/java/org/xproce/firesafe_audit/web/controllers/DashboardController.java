package org.xproce.firesafe_audit.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.dto.dashboard.DashboardDTO;
import org.xproce.firesafe_audit.service.dashboard.IDashboardService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final IDashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<DashboardDTO>> getDashboardData() {
        DashboardDTO dashboard = dashboardService.getDashboardData();
        return ResponseEntity.ok(ResponseDTO.success(dashboard));
    }

    @GetMapping("/evolution-mensuelle")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getEvolutionMensuelle(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut) {
        List<Map<String, Object>> evolution = dashboardService.getEvolutionMensuelle(dateDebut);
        return ResponseEntity.ok(ResponseDTO.success(evolution));
    }

    @GetMapping("/repartition-par-type")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getRepartitionParType() {
        List<Map<String, Object>> repartition = dashboardService.getRepartitionParType();
        return ResponseEntity.ok(ResponseDTO.success(repartition));
    }

    @GetMapping("/top-etablissements")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getTopEtablissements(
            @RequestParam(defaultValue = "10") int limit) {
        List<Map<String, Object>> top = dashboardService.getTopEtablissements(limit);
        return ResponseEntity.ok(ResponseDTO.success(top));
    }

    @GetMapping("/etablissements-a-risque")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ResponseDTO<List<Map<String, Object>>>> getEtablissementsARisque(
            @RequestParam(defaultValue = "70.0") Double seuilTaux) {
        List<Map<String, Object>> risques = dashboardService.getEtablissementsARisque(seuilTaux);
        return ResponseEntity.ok(ResponseDTO.success(risques));
    }
}