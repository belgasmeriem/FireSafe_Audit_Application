package org.xproce.firesafe_audit.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_reset_tokens", indexes = {
        @Index(name = "idx_token", columnList = "token"),
        @Index(name = "idx_user_token", columnList = "user_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 255)
    private String token;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private LocalDateTime dateExpiration;

    @Column(nullable = false)
    @Builder.Default
    private Boolean utilise = false;

    private LocalDateTime dateUtilisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static PasswordResetToken creer(User user, int expirationHeures) {
        return PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .dateExpiration(LocalDateTime.now().plusHours(expirationHeures))
                .utilise(false)
                .build();
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(dateExpiration);
    }

    public boolean isValide() {
        return !utilise && !isExpire();
    }

    public void marquerCommeUtilise() {
        this.utilise = true;
        this.dateUtilisation = LocalDateTime.now();
    }
}