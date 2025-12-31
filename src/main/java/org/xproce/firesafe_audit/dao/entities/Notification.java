package org.xproce.firesafe_audit.dao.entities;

import org.xproce.firesafe_audit.dao.enums.TypeNotification;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_destinataire", columnList = "destinataire_id"),
        @Index(name = "idx_lu", columnList = "lu"),
        @Index(name = "idx_type_notif", columnList = "type"),
        @Index(name = "idx_date_creation_notif", columnList = "dateCreation")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String titre;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TypeNotification type;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    @Builder.Default
    private Boolean lu = false;

    private LocalDateTime dateLecture;

    @Size(max = 500)
    @Column(length = 500)
    private String lien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User destinataire;

    public void marquerCommeLu() {
        this.lu = true;
        this.dateLecture = LocalDateTime.now();
    }

    public void marquerCommeNonLu() {
        this.lu = false;
        this.dateLecture = null;
    }

    public boolean isNonLu() {
        return !lu;
    }
}