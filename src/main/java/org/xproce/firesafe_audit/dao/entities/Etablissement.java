package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "etablissements", indexes = {
        @Index(name = "idx_nom", columnList = "nom"),
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_ville", columnList = "ville")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Etablissement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TypeEtablissement type;

    @Size(max = 255)
    @Column(length = 255)
    private String adresse;

    @Size(max = 100)
    @Column(length = 100)
    private String ville;

    @Size(max = 20)
    @Column(length = 20)
    private String codePostal;

    @Size(max = 100)
    @Column(length = 100)
    private String pays;

    private Integer capaciteAccueil;

    private Integer nombreEtages;

    private Double surfaceTotale;

    @Size(max = 100)
    @Column(length = 100)
    private String responsableNom;

    @Size(max = 100)
    @Column(length = 100)
    private String responsableEmail;

    @Size(max = 20)
    @Column(length = 20)
    private String responsableTelephone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "norme_id", nullable = false)
    private Norme norme;

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "etablissement", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateAudit DESC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Audit> audits = new ArrayList<>();

    @OneToMany(mappedBy = "etablissement", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<PhotoEtablissement> photos = new HashSet<>();

    public String getAdresseComplete() {
        StringBuilder sb = new StringBuilder();
        if (adresse != null) sb.append(adresse);
        if (codePostal != null) sb.append(", ").append(codePostal);
        if (ville != null) sb.append(" ").append(ville);
        if (pays != null) sb.append(", ").append(pays);
        return sb.toString();
    }

    public void addAudit(Audit audit) {
        audits.add(audit);
        audit.setEtablissement(this);
    }

    public void removeAudit(Audit audit) {
        audits.remove(audit);
        audit.setEtablissement(null);
    }

    public void addPhoto(PhotoEtablissement photo) {
        photos.add(photo);
        photo.setEtablissement(this);
    }

    public void removePhoto(PhotoEtablissement photo) {
        photos.remove(photo);
        photo.setEtablissement(null);
    }
}