package org.xproce.firesafe_audit.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RapportGlobalDTO {

    private String numeroRapport;
    private LocalDate dateGeneration;
    private EtablissementInfoDTO etablissement;
    private AuditInfoDTO audit;
    private ConformiteResumeDTO conformite;
    private List<NonConformiteDTO> nonConformites;
    private EvaluationRisqueDTO evaluationRisque;
    private List<PreuveAnnexeDTO> preuves;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EtablissementInfoDTO {
        private String nom;
        private String type;
        private String classification;
        private String adresse;
        private String ville;
        private String codePostal;
        private String pays;
        private Integer capaciteAccueil;
        private Integer nombreEtages;
        private Double surfaceTotale;
        private String responsableNom;
        private String responsableEmail;
        private String responsableTelephone;
        private String normeApplicable;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuditInfoDTO {
        private Long auditId;
        private LocalDate dateAudit;
        private String typeAudit;
        private String statutAudit;
        private String auditeurNom;
        private String auditeurQualification;
        private Integer dureeAudit;
        private LocalDateTime dateValidation;
        private String validateurNom;
        private String observationGenerale;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConformiteResumeDTO {
        private Integer nbTotalCriteres;
        private Integer nbConformes;
        private Integer nbNonConformes;
        private Integer nbPartiels;
        private Integer nbNonApplicables;
        private Double tauxConformite;
        private Double tauxConformitePondere;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NonConformiteDTO {
        private String critereCode;
        private String critereLibelle;
        private String categorie;
        private String criticite;
        private String statut;
        private String observation;
        private String actionCorrective;
        private String responsableAction;
        private LocalDate dateEcheance;
        private String urgence;
        private Double coutEstime;
        private Integer nombrePreuves;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationRisqueDTO {
        private String niveauRisque;
        private Double scoreGlobal;
        private Integer nbCritiquesNC;
        private Integer nbImportantesNC;
        private Integer nbNormalesNC;
        private String interpretation;
        private String recommandationPrincipale;
        private List<String> pointsCritiques;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreuveAnnexeDTO {
        private String critereCode;
        private String critereLibelle;
        private String nomFichier;
        private String typeFichier;
        private String description;
        private LocalDateTime dateUpload;
        private String cheminFichier;
    }
}