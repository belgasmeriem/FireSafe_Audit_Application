package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Table(name = "normes", indexes = {
        @Index(name = "idx_code_norme", columnList = "code"),
        @Index(name = "idx_pays", columnList = "pays")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Norme implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String nom;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 100)
    @Column(length = 100)
    private String pays;

    @Size(max = 20)
    @Column(length = 20)
    private String version;

    private LocalDate dateVigueur;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "norme_types_etablissements",
            joinColumns = @JoinColumn(name = "norme_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "type_etablissement")
    @Builder.Default
    private Set<TypeEtablissement> typesEtablissements = new HashSet<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean actif = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "norme", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("code ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Critere> criteres = new ArrayList<>();

    @OneToMany(mappedBy = "norme")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Etablissement> etablissements = new HashSet<>();

    public int getNombreCriteres() {
        return criteres != null ? criteres.size() : 0;
    }

    public void addCritere(Critere critere) {
        criteres.add(critere);
        critere.setNorme(this);
    }

    public void removeCritere(Critere critere) {
        criteres.remove(critere);
        critere.setNorme(null);
    }
}