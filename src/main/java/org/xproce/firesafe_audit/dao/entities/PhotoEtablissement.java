package org.xproce.firesafe_audit.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "photos_etablissement")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoEtablissement implements Serializable {

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

    @Size(max = 500)
    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    @Builder.Default
    private Boolean principale = false;

    private Long tailleFichier;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateUpload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etablissement_id", nullable = false)
    private Etablissement etablissement;
}