package org.xproce.firesafe_audit.dto.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementSummaryDTO;
import org.xproce.firesafe_audit.dto.user.UserSummaryDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditDTO {

    private Long id;

    private LocalDate dateAudit;

    private TypeAudit type;

    private StatutAudit statut;

    private Double tauxConformite;

    private Double tauxConformitePondere;

    private Integer nbConformes;

    private Integer nbNonConformes;

    private Integer nbPartiels;

    private Integer nbNonApplicables;

    private String observationGenerale;

    private Integer dureeEstimee;

    private Integer dureeReelle;

    private EtablissementSummaryDTO etablissement;

    private UserSummaryDTO auditeur;

    private UserSummaryDTO validateur;

    private LocalDateTime dateValidation;

    private LocalDateTime dateCreation;

    private LocalDateTime dateModification;

    private Integer nbTotalCriteres;

    private Integer nbCriteresApplicables;
}