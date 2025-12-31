package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.NiveauUrgence;
import org.xproce.firesafe_audit.dao.enums.StatutConformite;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "evaluations_criteres", indexes = {
        @Index(name = "idx_statut_conformite", columnList = "statut"),
        @Index(name = "idx_audit", columnList = "audit_id"),
        @Index(name = "idx_critere", columnList = "critere_id"),
        @Index(name = "idx_urgence", columnList = "urgence")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationCritere implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private StatutConformite statut;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateEvaluation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NiveauUrgence urgence;

    @Column(columnDefinition = "TEXT")
    private String actionCorrective;

    @Column(length = 200)
    private String responsableAction;

    private LocalDate dateEcheance;

    private Double coutEstime;

    @Column(nullable = false)
    @Builder.Default
    private Boolean corrigee = false;

    private LocalDateTime dateCorrectionSignalee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "critere_id", nullable = false)
    private Critere critere;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Preuve> preuves = new HashSet<>();

    public void addPreuve(Preuve preuve) {
        preuves.add(preuve);
        preuve.setEvaluation(this);
    }

    public void removePreuve(Preuve preuve) {
        preuves.remove(preuve);
        preuve.setEvaluation(null);
    }

    public boolean isConforme() {
        return statut == StatutConformite.CONFORME;
    }

    public boolean isNonConforme() {
        return statut == StatutConformite.NON_CONFORME;
    }

    public boolean isPartiellementConforme() {
        return statut == StatutConformite.PARTIELLEMENT_CONFORME;
    }

    public boolean isNonApplicable() {
        return statut == StatutConformite.NON_APPLICABLE;
    }

    public boolean needsAction() {
        return (isNonConforme() || isPartiellementConforme()) && !corrigee;
    }

    public void signalerCorrection() {
        this.corrigee = true;
        this.dateCorrectionSignalee = LocalDateTime.now();
    }
}