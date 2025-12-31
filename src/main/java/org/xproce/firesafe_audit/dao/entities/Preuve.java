package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.TypeFichier;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "preuves", indexes = {
        @Index(name = "idx_evaluation_preuve", columnList = "evaluation_id"),
        @Index(name = "idx_type_fichier", columnList = "typeFichier")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Preuve implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String nomFichier;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String cheminFichier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TypeFichier typeFichier = TypeFichier.AUTRE;

    private Long tailleFichier;

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false)
    private EvaluationCritere evaluation;

    public String getExtension() {
        if (nomFichier == null || !nomFichier.contains(".")) {
            return "";
        }
        return nomFichier.substring(nomFichier.lastIndexOf(".") + 1).toLowerCase();
    }

    public boolean isImage() {
        return typeFichier == TypeFichier.IMAGE;
    }

    public boolean isPDF() {
        return typeFichier == TypeFichier.PDF;
    }

    public String getTailleFichierFormatee() {
        if (tailleFichier == null) return "0 B";

        long bytes = tailleFichier;
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024));
        return String.format("%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }
}