package org.xproce.firesafe_audit.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.PasswordResetToken;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.repositories.PasswordResetTokenRepository;
import org.xproce.firesafe_audit.dao.repositories.UserRepository;
import org.xproce.firesafe_audit.service.notification.IEmailService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements IPasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final IEmailService emailService;

    @Override
    @Transactional
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aucun utilisateur avec cet email"));

        PasswordResetToken token = PasswordResetToken.creer(user, 24);
        tokenRepository.save(token);

        String resetLink = "http://localhost:8080/firesafe/reset-password?token=" + token.getToken();
        emailService.sendPasswordResetEmail(user.getEmail(), user.getNomComplet(), resetLink);
    }

    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findValidToken(token, LocalDateTime.now())
                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.marquerCommeUtilise();
        tokenRepository.save(resetToken);
    }

    @Override
    public boolean validateToken(String token) {
        return tokenRepository.findValidToken(token, LocalDateTime.now()).isPresent();
    }

    @Override
    @Transactional
    public void cleanExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.deleteByDateExpirationBefore(now);
        tokenRepository.deleteOldUsedTokens(now.minusDays(7));
    }
}