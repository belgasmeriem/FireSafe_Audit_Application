package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Audit;
import org.xproce.firesafe_audit.dto.audit.AuditDTO;
import org.xproce.firesafe_audit.dto.audit.AuditSummaryDTO;

import java.math.BigDecimal;

@Component
public class AuditMapper {

    @Autowired
    private EtablissementMapper etablissementMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NormeMapper normeMapper;

    public AuditDTO toDTO(Audit audit) {
        if (audit == null) return null;

        AuditSummaryDTO auditInitialSummary = null;
        if (audit.getAuditInitial() != null) {
            auditInitialSummary = toSummaryDTO(audit.getAuditInitial());
        }

        Integer progression = null;
        Integer ncCritiquesRestantes = null;
        BigDecimal evolutionConformite = null;
        Integer nbNCCorrigees = null;
        Integer nbNCTotales = null;

        if (audit.getAuditInitial() != null && audit.getTauxConformite() != null) {
            if (audit.getAuditInitial().getTauxConformite() != null) {
                evolutionConformite = audit.getTauxConformite()
                        .subtract(audit.getAuditInitial().getTauxConformite());
            }

            if (audit.getAuditInitial().getNbNonConformes() != null) {
                nbNCTotales = audit.getAuditInitial().getNbNonConformes();
                nbNCCorrigees = nbNCTotales - (audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0);
            }

            if (audit.getNbTotalCriteres() != null && audit.getNbTotalCriteres() > 0) {
                int evaluees = (audit.getNbConformes() != null ? audit.getNbConformes() : 0) +
                        (audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0) +
                        (audit.getNbPartiels() != null ? audit.getNbPartiels() : 0) +
                        (audit.getNbNonApplicables() != null ? audit.getNbNonApplicables() : 0);
                progression = (evaluees * 100) / audit.getNbTotalCriteres();
            }

            ncCritiquesRestantes = audit.getNbNonConformes() != null ? audit.getNbNonConformes() : 0;
        }

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
                .norme(audit.getEtablissement() != null && audit.getEtablissement().getNorme() != null ?
                        normeMapper.toDTO(audit.getEtablissement().getNorme()) : null)
                .auditInitial(auditInitialSummary)
                .progression(progression)
                .ncCritiquesRestantes(ncCritiquesRestantes)
                .evolutionConformite(evolutionConformite)
                .nbNCCorrigees(nbNCCorrigees)
                .nbNCTotales(nbNCTotales)
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