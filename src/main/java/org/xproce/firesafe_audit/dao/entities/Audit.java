package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.StatutAudit;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import org.xproce.firesafe_audit.dao.enums.TypeAudit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
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
        @Index(name = "idx_type", columnList = "type"),
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private TypeAudit type = TypeAudit.PERIODIQUE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private StatutAudit statut = StatutAudit.PLANIFIE;

    @Column(precision = 5, scale = 2)
    private Double tauxConformite;

    @Column(precision = 5, scale = 2)
    private Double tauxConformitePondere;

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

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "audit", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("critere.code ASC")
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

    public void calculerStatistiques() {
        if (evaluations == null || evaluations.isEmpty()) {
            this.nbConformes = 0;
            this.nbNonConformes = 0;
            this.nbPartiels = 0;
            this.nbNonApplicables = 0;
            this.tauxConformite = 0.0;
            this.tauxConformitePondere = 0.0;
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
            this.tauxConformite = (double) conformes / applicables * 100;

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

            this.tauxConformitePondere = pointsPossibles > 0 ?
                    (pointsObtenus / pointsPossibles * 100) : 0.0;
        } else {
            this.tauxConformite = 0.0;
            this.tauxConformitePondere = 0.0;
        }
    }
}