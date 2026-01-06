package org.xproce.firesafe_audit.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Role;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dao.repositories.RoleRepository;
import org.xproce.firesafe_audit.dao.repositories.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("=== DÉBUT INITIALISATION DES DONNÉES ===");

        createRoles();
        createAdminUser();
        createTestUsers();

        System.out.println("=== FIN INITIALISATION DES DONNÉES ===");
    }

    private void createRoles() {
        System.out.println("Création des rôles...");

        for (RoleType roleType : RoleType.values()) {
            if (!roleRepository.existsByName(roleType)) {
                Role role = Role.builder()
                        .name(roleType)
                        .description(roleType.getLibelle())
                        .build();
                roleRepository.save(role);
                System.out.println("✅ Rôle créé: " + roleType.name() + " - " + roleType.getLibelle());
            } else {
                System.out.println("➡️ Rôle existe déjà: " + roleType.name());
            }
        }
    }

    private void createAdminUser() {

        String adminUsername = "superadmin";
        String adminEmail = "admin@firesafe.com";
        String adminPassword = "Admin123!";

        if (userRepository.findByUsername(adminUsername).isPresent()) {
            System.out.println("✅ Administrateur existe déjà: " + adminUsername);
            return;
        }

        Role adminRole = roleRepository.findByName(RoleType.ADMIN)
                .orElseThrow(() -> new RuntimeException("Rôle ADMIN non trouvé"));

        User admin = User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .nom("Admin")
                .prenom("System")
                .telephone("0600000000")
                .actif(true)
                .build();

        admin.setRoles(Set.of(adminRole));

        userRepository.save(admin);

        System.out.println("✅ ADMIN créé : username=" + adminUsername);
    }


    private void createTestUsers() {
        System.out.println("\nCréation des utilisateurs de test...");

        createUser("manager", "manager@firesafe.com", "Manager", "Test",
                "Manager123!", RoleType.MANAGER);
        createUser("auditor1", "auditor@firesafe.com", "Auditor", "Senior",
                "Auditor123!", RoleType.AUDITOR);
        createUser("user1", "user@firesafe.com", "User", "Regular",
                "User123!", RoleType.AUDITOR);
        createUser("auditor2", "auditor2@firesafe.com", "Auditor", "Junior",
                "Auditor456!", RoleType.AUDITOR);
    }

    private void createUser(String username, String email, String nom, String prenom,
                            String plainPassword, RoleType roleType) {
        try {
            if (userRepository.findByEmail(email).isPresent()) {
                System.out.println("➡️ Utilisateur existe déjà: " + email);
                return;
            }

            Role role = roleRepository.findByName(roleType)
                    .orElseThrow(() -> new RuntimeException("Rôle " + roleType + " non trouvé"));

            User user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(plainPassword))
                    .nom(nom)
                    .prenom(prenom)
                    .telephone("0611111111")
                    .actif(true)
                    .build();

            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);

            userRepository.save(user);

            System.out.println("✅ " + roleType.getLibelle() + " créé: " + email + " / " + plainPassword);

        } catch (Exception e) {
            System.err.println("❌ Erreur création " + email + ": " + e.getMessage());
        }
    }
}