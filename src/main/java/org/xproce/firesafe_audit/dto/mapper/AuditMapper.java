package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dto.audit.AuditDTO;
import org.xproce.firesafe_audit.dto.audit.AuditSummaryDTO;

@Component
public class AuditMapper {

    @Autowired
    private EtablissementMapper etablissementMapper;

    @Autowired
    private UserMapper userMapper;

    public AuditDTO toDTO(Audit audit) {
        if (audit == null) return null;

        return AuditDTO.builder()
                .id(audit.getId())
                .dateAudit(audit.getDateAudit())
                .type(audit.getType())
                .statut(audit.getStatut())
                .tauxConformite(audit.getTauxConformite())
                .tauxConformitePondere(audit.getTauxConformitePondere())
                .nbConformes(audit.getNbConformes())
                .nbNonConformes(audit.getNbNonConformes())
                .nbPartiels(audit.getNbPartiels())
                .nbNonApplicables(audit.getNbNonApplicables())
                .observationGenerale(audit.getObservationGenerale())
                .dureeEstimee(audit.getDureeEstimee())
                .dureeReelle(audit.getDureeReelle())
                .etablissement(etablissementMapper.toSummaryDTO(audit.getEtablissement()))
                .auditeur(userMapper.toSummaryDTO(audit.getAuditeur()))
                .validateur(userMapper.toSummaryDTO(audit.getValidateur()))
                .dateValidation(audit.getDateValidation())
                .dateCreation(audit.getDateCreation())
                .dateModification(audit.getDateModification())
                .nbTotalCriteres(audit.getNbTotalCriteres())
                .nbCriteresApplicables(audit.getNbCriteresApplicables())
                .build();
    }

    public AuditSummaryDTO toSummaryDTO(Audit audit) {
        if (audit == null) return null;

        return AuditSummaryDTO.builder()
                .id(audit.getId())
                .dateAudit(audit.getDateAudit())
                .type(audit.getType())
                .statut(audit.getStatut())
                .tauxConformite(audit.getTauxConformite())
                .etablissementNom(audit.getEtablissement() != null ? audit.getEtablissement().getNom() : null)
                .auditeurNom(audit.getAuditeur() != null ? audit.getAuditeur().getNomComplet() : null)
                .build();
    }
}