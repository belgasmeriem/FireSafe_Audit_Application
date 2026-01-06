package org.xproce.firesafe_audit.web.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dto.user.*;
import org.xproce.firesafe_audit.service.user.IUserService;

@Slf4j
@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public String list(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "role", required = false) RoleType role,
            Model model) {

        var users = search != null && !search.isEmpty()
                ? userService.searchUsers(search)
                : role != null
                ? userService.getUsersByRole(role)
                : userService.getActiveUsers();

        model.addAttribute("users", users);
        model.addAttribute("roles", RoleType.values());
        model.addAttribute("search", search);
        model.addAttribute("selectedRole", role);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("pageTitle", "Gestion des Utilisateurs");

        model.addAttribute("totalAdmins", userService.countByRole(RoleType.ADMIN));
        model.addAttribute("totalManagers", userService.countByRole(RoleType.MANAGER));
        model.addAttribute("totalAuditors", userService.countByRole(RoleType.AUDITOR));

        return "user/list";
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("user", new UserCreateDTO());
        model.addAttribute("roles", RoleType.values());
        model.addAttribute("pageTitle", "Nouvel Utilisateur");
        return "user/add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(
            @Valid @ModelAttribute("user") UserCreateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("roles", RoleType.values());
            return "user/add";
        }

        try {
            UserDTO created = userService.createUser(dto);
            redirectAttributes.addFlashAttribute("success",
                    "Utilisateur créé avec succès");
            return "redirect:/users";
        } catch (Exception e) {
            log.error("Erreur lors de la création", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", RoleType.values());
            return "user/add";
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @userSecurityService.isOwner(#id)")
    public String profile(@PathVariable Long id, Model model) {
        try {
            UserDTO user = userService.getUserById(id);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isOwnProfile = false;

            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                try {
                    UserDTO currentUser = userService.getUserByUsername(auth.getName());
                    isOwnProfile = currentUser != null && currentUser.getId().equals(id);
                    log.info("User {} consulte le profil de {} - isOwnProfile: {}",
                            currentUser.getUsername(), user.getUsername(), isOwnProfile);
                } catch (Exception e) {
                    log.warn("Impossible de récupérer l'utilisateur courant", e);
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("isOwnProfile", isOwnProfile);
            model.addAttribute("pageTitle", user.getNomComplet());

            return "user/profile";
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du profil utilisateur {}", id, e);
            return "redirect:/users?error=" + e.getMessage();
        }
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            UserDTO user = userService.getUserById(id);

            UserUpdateDTO updateDTO = UserUpdateDTO.builder()
                    .email(user.getEmail())
                    .nom(user.getNom())
                    .prenom(user.getPrenom())
                    .telephone(user.getTelephone())
                    .actif(user.getActif())
                    .roles(user.getRoles())
                    .build();

            model.addAttribute("user", updateDTO);
            model.addAttribute("userId", id);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("roles", RoleType.values());
            model.addAttribute("pageTitle", "Modifier " + user.getNomComplet());
            return "user/edit";
        } catch (Exception e) {
            log.error("Erreur lors de la récupération", e);
            return "redirect:/users?error=" + e.getMessage();
        }
    }

    @PostMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute("user") UserUpdateDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("userId", id);
            model.addAttribute("roles", RoleType.values());
            return "user/edit";
        }

        try {
            userService.updateUser(id, dto);
            redirectAttributes.addFlashAttribute("success",
                    "Utilisateur mis à jour avec succès");
            return "redirect:/users/" + id;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour", e);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("userId", id);
            model.addAttribute("roles", RoleType.values());
            return "user/edit";
        }
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Utilisateur désactivé avec succès");
        } catch (Exception e) {
            log.error("Erreur lors de la suppression", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/users";
    }

    @PostMapping("/{id}/change-password")
    @PreAuthorize("hasRole('ADMIN')")
    public String changePassword(
            @PathVariable Long id,
            @RequestParam String newPassword,
            RedirectAttributes redirectAttributes) {

        try {
            userService.changePassword(id, newPassword);
            redirectAttributes.addFlashAttribute("success", "Mot de passe modifié avec succès");
        } catch (Exception e) {
            log.error("Erreur lors du changement de mot de passe", e);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/users/" + id;
    }
}