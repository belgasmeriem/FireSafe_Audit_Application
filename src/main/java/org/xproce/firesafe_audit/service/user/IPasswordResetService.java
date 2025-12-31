package org.xproce.firesafe_audit.service.user;

public interface IPasswordResetService {
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    boolean validateToken(String token);
    void cleanExpiredTokens();
}