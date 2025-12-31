package org.xproce.firesafe_audit.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "commentaires", indexes = {
        @Index(name = "idx_audit_commentaire", columnList = "audit_id"),
        @Index(name = "idx_auteur_commentaire", columnList = "auteur_id"),
        @Index(name = "idx_date_creation_commentaire", columnList = "dateCreation")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commentaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audit_id", nullable = false)
    private Audit audit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auteur_id", nullable = false)
    private User auteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Commentaire parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateCreation ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Commentaire> reponses = new HashSet<>();

    public void addReponse(Commentaire reponse) {
        reponses.add(reponse);
        reponse.setParent(this);
    }

    public void removeReponse(Commentaire reponse) {
        reponses.remove(reponse);
        reponse.setParent(null);
    }

    public boolean isReponse() {
        return parent != null;
    }

    public int getNombreReponses() {
        return reponses != null ? reponses.size() : 0;
    }
}