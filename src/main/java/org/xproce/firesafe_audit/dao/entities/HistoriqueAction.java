package org.xproce.firesafe_audit.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_actions", indexes = {
        @Index(name = "idx_utilisateur_histo", columnList = "utilisateur_id"),
        @Index(name = "idx_action_histo", columnList = "action"),
        @Index(name = "idx_entite_histo", columnList = "entite"),
        @Index(name = "idx_date_action", columnList = "dateAction")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoriqueAction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String action;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String entite;

    private Long entiteId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String anciensValeurs;

    @Column(columnDefinition = "TEXT")
    private String nouvellesValeurs;

    @Size(max = 45)
    @Column(length = 45)
    private String adresseIP;

    @Size(max = 500)
    @Column(length = 500)
    private String userAgent;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    public static HistoriqueAction creer(User utilisateur, String action, String entite, Long entiteId, String description) {
        return HistoriqueAction.builder()
                .utilisateur(utilisateur)
                .action(action)
                .entite(entite)
                .entiteId(entiteId)
                .description(description)
                .build();
    }
}