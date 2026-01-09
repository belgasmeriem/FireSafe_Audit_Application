package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dto.auth.*;
import org.xproce.firesafe_audit.service.auth.IAuthService;
import org.xproce.firesafe_audit.service.user.IPasswordResetService;

import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;
    private final IPasswordResetService passwordResetService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }
        return "auth/login";
    }

    private String getRedirectUrlByRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                return "/dashboard";
            } else if (role.equals("ROLE_MANAGER")) {
                return "/dashboard";
            } else if (role.equals("ROLE_AUDITOR")) {
                return "/audits/mes-audits";
            }
        }

        return "/dashboard";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        if (!model.containsAttribute("registerRequest")) {
            model.addAttribute("registerRequest", new RegisterRequest());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerRequest", result);
            redirectAttributes.addFlashAttribute("registerRequest", request);
            return "redirect:/auth/register";
        }

        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("success", "Compte créé avec succès !");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/register";
        }
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());
        model.addAttribute("pageTitle", "Mot de passe oublié");
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(
            @Valid @ModelAttribute("passwordResetRequest") PasswordResetRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "auth/forgot-password";
        }

        try {
            passwordResetService.requestPasswordReset(request.getEmail());
            redirectAttributes.addFlashAttribute("success",
                    "Un email avec les instructions de réinitialisation a été envoyé à votre adresse email.");
            return "redirect:/auth/login";
        } catch (Exception e) {
            log.error("Erreur lors de la demande de réinitialisation", e);
            redirectAttributes.addFlashAttribute("error",
                    "Une erreur est survenue. Veuillez réessayer.");
            return "redirect:/auth/forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(
            @RequestParam String token,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!passwordResetService.validateToken(token)) {
            redirectAttributes.addFlashAttribute("error",
                    "Le lien de réinitialisation est invalide ou a expiré.");
            return "redirect:/auth/login";
        }

        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Nouveau mot de passe");
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {

        if (newPassword == null || newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("error",
                    "Le mot de passe doit contenir au moins 6 caractères");
            return "redirect:/auth/reset-password?token=" + token;
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error",
                    "Les mots de passe ne correspondent pas");
            return "redirect:/auth/reset-password?token=" + token;
        }

        try {
            // Récupérer le username AVANT de réinitialiser
            String username = passwordResetService.getUsernameFromToken(token);

            // Réinitialiser le mot de passe
            passwordResetService.resetPassword(token, newPassword);

            // Rediriger avec le username en paramètre GET
            redirectAttributes.addFlashAttribute("success",
                    "Votre mot de passe a été réinitialisé avec succès. Vous pouvez maintenant vous connecter.");

            return "redirect:/auth/login?username=" + username;

        } catch (Exception e) {
            log.error("Erreur lors de la réinitialisation", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/reset-password?token=" + token;
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }
}