package org.xproce.firesafe_audit.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections",
        uniqueConstraints = @UniqueConstraint(columnNames = {"norme_id", "code"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String code;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer ordre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "norme_id", nullable = false)
    private Norme norme;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("code  ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<Critere> criteres = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    private LocalDateTime dateModification;

    public void addCritere(Critere critere) {
        criteres.add(critere);
        critere.setSection(this);
    }

    public void removeCritere(Critere critere) {
        criteres.remove(critere);
        critere.setSection(null);
    }

    public int getNombreCriteres() {
        return criteres != null ? criteres.size() : 0;
    }
}