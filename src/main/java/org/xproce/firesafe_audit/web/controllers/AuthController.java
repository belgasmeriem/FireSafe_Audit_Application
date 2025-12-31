package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xproce.firesafe_audit.dto.auth.*;
import org.xproce.firesafe_audit.dto.common.ResponseDTO;
import org.xproce.firesafe_audit.service.auth.IAuthService;
import org.xproce.firesafe_audit.service.user.IPasswordResetService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final IAuthService authService;
    private final IPasswordResetService passwordResetService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ResponseDTO.success("Connexion réussie", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Identifiants incorrects", "AUTH_ERROR"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseDTO.success("Utilisateur créé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "REGISTER_ERROR"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<String>> forgotPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.requestPasswordReset(request.getEmail());
            return ResponseEntity.ok(ResponseDTO.success("Email de réinitialisation envoyé"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "RESET_ERROR"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO<String>> resetPassword(@Valid @RequestBody PasswordResetConfirm request) {
        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(ResponseDTO.success("Mot de passe réinitialisé avec succès"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseDTO.error(e.getMessage(), "RESET_ERROR"));
        }
    }

    @GetMapping("/validate-reset-token/{token}")
    public ResponseEntity<ResponseDTO<Boolean>> validateResetToken(@PathVariable String token) {
        boolean isValid = passwordResetService.validateToken(token);
        return ResponseEntity.ok(ResponseDTO.success("Token validé", isValid));
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseDTO<?>> getCurrentUser() {
        try {
            var user = authService.getCurrentUser();
            return ResponseEntity.ok(ResponseDTO.success("Utilisateur récupéré", user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDTO.error("Non authentifié", "AUTH_ERROR"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<String>> logout() {
        return ResponseEntity.ok(ResponseDTO.success("Déconnexion réussie"));
    }
}