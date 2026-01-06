package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "audits", indexes = {
        @Index(name = "idx_date_audit", columnList = "dateAudit"),
        @Index(name = "idx_statut", columnList = "statut"),
        @Index(name = "idx_audit_type", columnList = "type"),
        @Index(name = "idx_etablissement", columnList = "etablissement_id"),
        @Index(name = "idx_auditeur", columnList = "auditeur_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Audit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateAudit;

    @Column(name = "date_debut")
    private LocalDateTime dateDebut;

    @Column(name = "date_cloture")
    private LocalDateTime dateCloture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private TypeAudit type = TypeAudit.PERIODIQUE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatutAudit statut = StatutAudit.PLANIFIE;

    @Column(precision = 5, scale = 2)
    private BigDecimal tauxConformite;

    @Column(precision = 5, scale = 2)
    private BigDecimal  tauxConformitePondere;

    @Builder.Default
    private Integer nbConformes = 0;

    @Builder.Default
    private Integer nbNonConformes = 0;

    @Builder.Default
    private Integer nbPartiels = 0;

    @Builder.Default
    private Integer nbNonApplicables = 0;

    @Column(columnDefinition = "TEXT")
    private String observationGenerale;

    private Integer dureeEstimee;

    private Integer dureeReelle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_id", nullable = false)
    private Etablissement etablissement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auditeur_id", nullable = false)
    private User auditeur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validateur_id")
    private User validateur;

    private LocalDateTime dateValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_initial_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Audit auditInitial;

    @OneToMany(mappedBy = "auditInitial", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Audit> contreVisites = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "audit", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<EvaluationCritere> evaluations = new ArrayList<>();


    @OneToMany(mappedBy = "audit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateCreation DESC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Commentaire> commentaires = new HashSet<>();

    public void addEvaluation(EvaluationCritere evaluation) {
        evaluations.add(evaluation);
        evaluation.setAudit(this);
    }

    public void removeEvaluation(EvaluationCritere evaluation) {
        evaluations.remove(evaluation);
        evaluation.setAudit(null);
    }

    public void addCommentaire(Commentaire commentaire) {
        commentaires.add(commentaire);
        commentaire.setAudit(this);
    }

    public void removeCommentaire(Commentaire commentaire) {
        commentaires.remove(commentaire);
        commentaire.setAudit(null);
    }

    public Integer getNbTotalCriteres() {
        return evaluations != null ? evaluations.size() : 0;
    }

    public Integer getNbCriteresApplicables() {
        return getNbTotalCriteres() - nbNonApplicables;
    }

    public boolean isContreVisite() {
        return type == TypeAudit.CONTRE_VISITE && auditInitial != null;
    }

    public void calculerStatistiques() {
        if (evaluations == null || evaluations.isEmpty()) {
            this.nbConformes = 0;
            this.nbNonConformes = 0;
            this.nbPartiels = 0;
            this.nbNonApplicables = 0;
            this.tauxConformite = BigDecimal.ZERO;
            this.tauxConformitePondere = BigDecimal.ZERO;
            return;
        }

        long conformes = evaluations.stream()
                .filter(e -> e.getStatut() == StatutConformite.CONFORME)
                .count();

        long nonConformes = evaluations.stream()
                .filter(e -> e.getStatut() == StatutConformite.NON_CONFORME)
                .count();

        long partiels = evaluations.stream()
                .filter(e -> e.getStatut() == StatutConformite.PARTIELLEMENT_CONFORME)
                .count();

        long nonApplicables = evaluations.stream()
                .filter(e -> e.getStatut() == StatutConformite.NON_APPLICABLE)
                .count();

        this.nbConformes = (int) conformes;
        this.nbNonConformes = (int) nonConformes;
        this.nbPartiels = (int) partiels;
        this.nbNonApplicables = (int) nonApplicables;

        int applicables = getNbCriteresApplicables();
        if (applicables > 0) {
            this.tauxConformite = BigDecimal.valueOf(conformes)
                    .divide(BigDecimal.valueOf(applicables), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));

            double pointsObtenus = evaluations.stream()
                    .filter(e -> e.getStatut() != StatutConformite.NON_APPLICABLE)
                    .mapToDouble(e -> {
                        int pond = e.getCritere().getPonderation();
                        switch (e.getStatut()) {
                            case CONFORME: return pond;
                            case PARTIELLEMENT_CONFORME: return pond * 0.5;
                            default: return 0;
                        }
                    })
                    .sum();

            double pointsPossibles = evaluations.stream()
                    .filter(e -> e.getStatut() != StatutConformite.NON_APPLICABLE)
                    .mapToDouble(e -> e.getCritere().getPonderation())
                    .sum();
            this.tauxConformitePondere = pointsPossibles > 0
                    ? BigDecimal.valueOf(pointsObtenus)
                    .divide(BigDecimal.valueOf(pointsPossibles), 2, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    : BigDecimal.ZERO;

        } else {
            this.tauxConformite = BigDecimal.ZERO;
            this.tauxConformitePondere = BigDecimal.ZERO;
        }
    }
}