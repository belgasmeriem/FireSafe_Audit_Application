package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditUpdateDTO {

    private LocalDate dateAudit;

    private StatutAudit statut;

    private String observationGenerale;

    private Integer dureeReelle;
}