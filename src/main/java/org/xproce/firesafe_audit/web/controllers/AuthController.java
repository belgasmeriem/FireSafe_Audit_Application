package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dto.auth.*;
import org.xproce.firesafe_audit.service.auth.IAuthService;
import org.xproce.firesafe_audit.service.user.IPasswordResetService;

import java.util.Collection;

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
        model.addAttribute("resetRequest", new PasswordResetRequest());
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@Valid @ModelAttribute("resetRequest") PasswordResetRequest request,
                                        BindingResult result,
                                        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/forgot-password";
        }

        try {
            passwordResetService.requestPasswordReset(request.getEmail());
            redirectAttributes.addFlashAttribute("success",
                    "Email de réinitialisation envoyé !");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/forgot-password";
        }
    }

    @GetMapping("/reset-password/{token}")
    public String showResetPasswordForm(@PathVariable String token, Model model) {
        boolean isValid = passwordResetService.validateToken(token);
        if (!isValid) {
            model.addAttribute("error", "Token invalide ou expiré");
            return "auth/reset-password-error";
        }

        PasswordResetConfirm resetConfirm = new PasswordResetConfirm();
        resetConfirm.setToken(token);
        model.addAttribute("resetConfirm", resetConfirm);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@Valid @ModelAttribute("resetConfirm") PasswordResetConfirm resetConfirm,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/reset-password";
        }

        try {
            passwordResetService.resetPassword(resetConfirm.getToken(), resetConfirm.getNewPassword());
            redirectAttributes.addFlashAttribute("success",
                    "Mot de passe réinitialisé avec succès !");
            return "redirect:/auth/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/auth/reset-password";
        }
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout";
    }
}