package org.xproce.firesafe_audit.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.PasswordResetToken;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.repositories.PasswordResetTokenRepository;
import org.xproce.firesafe_audit.dao.repositories.UserRepository;
import org.xproce.firesafe_audit.service.notification.IEmailService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements IPasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Value("${app.password-reset.token-validity-minutes:30}")
    private int tokenValidityMinutes;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        log.info("Demande de réinitialisation de mot de passe pour l'email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun compte trouvé avec cet email"));

        if (!user.getActif()) {
            throw new RuntimeException("Ce compte est désactivé");
        }

        tokenRepository.deleteByUserId(user.getId());

        PasswordResetToken token = PasswordResetToken.creer(user, tokenValidityMinutes);
        tokenRepository.save(token);

        String resetLink = baseUrl + "/auth/reset-password?token=" + token.getToken();

        emailService.sendPasswordResetEmail(
                user.getEmail(),
                user.getNomComplet(),
                resetLink
        );

        log.info("Email de réinitialisation envoyé à: {}", email);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        log.info("Tentative de réinitialisation avec le token: {}", token);

        PasswordResetToken resetToken = tokenRepository.findValidToken(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.marquerCommeUtilise();
        tokenRepository.save(resetToken);

        log.info("Mot de passe réinitialisé avec succès pour l'utilisateur: {}", user.getUsername());
    }

    public String getUsernameFromToken(String token) {
        try {
            PasswordResetToken resetToken = tokenRepository.findValidToken(token, LocalDateTime.now())
                    .orElseThrow(() -> new RuntimeException("Token invalide"));
            return resetToken.getUser().getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        return tokenRepository.findValidToken(token, LocalDateTime.now()).isPresent();
    }

    @Override
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteExpiredTokens(now);
        log.info("Tokens expirés supprimés");
    }


}