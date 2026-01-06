package org.xproce.firesafe_audit.web.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.service.user.IUserService;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final IUserService userService;

    @ModelAttribute
    public void addCurrentUserToModel(Model model) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                String username = auth.getName();
                UserDTO currentUser = userService.getUserByUsername(username);

                if (currentUser != null) {
                    model.addAttribute("currentUser", currentUser);
                    model.addAttribute("currentUserId", currentUser.getId());

                    String initiales = "";
                    if (currentUser.getPrenom() != null && !currentUser.getPrenom().isEmpty()) {
                        initiales += currentUser.getPrenom().charAt(0);
                    }
                    if (currentUser.getNom() != null && !currentUser.getNom().isEmpty()) {
                        initiales += currentUser.getNom().charAt(0);
                    }
                    if (initiales.isEmpty() && currentUser.getUsername() != null) {
                        initiales = currentUser.getUsername().substring(0, Math.min(2, currentUser.getUsername().length()));
                    }
                    model.addAttribute("userInitiales", initiales.toUpperCase());

                    if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
                        model.addAttribute("currentUserRole",
                                currentUser.getRoles().iterator().next().getLibelle());
                    } else {
                        model.addAttribute("currentUserRole", "Utilisateur");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'injection de l'utilisateur connecté", e);
        }
    }
}