package org.xproce.firesafe_audit.dao.entities;

import com.lowagie.text.Section;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dao.enums.Criticite;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "criteres", indexes = {
        @Index(name = "idx_code_critere", columnList = "code"),
        @Index(name = "idx_categorie", columnList = "categorie"),
        @Index(name = "idx_criticite", columnList = "criticite")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Critere implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String code;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categorie categorie;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Criticite criticite;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    @Builder.Default
    private Integer ponderation = 3;

    @Column(nullable = false)
    @Builder.Default
    private Boolean obligatoire = true;

    @Column(columnDefinition = "TEXT")
    private String preuvesRequises;

    @Size(max = 200)
    @Column(length = 200)
    private String referenceTexteLoi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "norme_id", nullable = false)
    private Norme norme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private org.xproce.firesafe_audit.dao.entities.Section section;

    @OneToMany(mappedBy = "critere", cascade = CascadeType.ALL)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<EvaluationCritere> evaluations = new ArrayList<>();

    public String getCodeComplet() {
        return norme != null ? norme.getCode() + "-" + code : code;
    }
}
