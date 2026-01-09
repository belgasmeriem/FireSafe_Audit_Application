package org.xproce.firesafe_audit.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xproce.firesafe_audit.dao.entities.Role;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dao.repositories.RoleRepository;
import org.xproce.firesafe_audit.dao.repositories.UserRepository;
import org.xproce.firesafe_audit.dto.user.UserCreateDTO;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.dto.user.UserUpdateDTO;
import org.xproce.firesafe_audit.dto.mapper.UserMapper;
import org.xproce.firesafe_audit.service.notification.IEmailService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final IEmailService emailService;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getActiveUsers() {
        return userRepository.findByActifTrue().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getUsersByRole(RoleType roleType) {
        return userRepository.findByRole(roleType).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> searchUsers(String search) {
        return userRepository.searchUsers(search).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .telephone(dto.getTelephone())
                .actif(true)
                .build();

        Set<Role> roles = new HashSet<>();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            for (RoleType roleType : dto.getRoles()) {
                Role role = roleRepository.findByName(roleType)
                        .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                roles.add(role);
            }
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        try {
            emailService.sendWelcomeEmail(savedUser);
            log.info("Email de bienvenue envoyé à {}", savedUser.getEmail());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de bienvenue à {}: {}",
                    savedUser.getEmail(), e.getMessage());
        }

        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email déjà utilisé");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getNom() != null) user.setNom(dto.getNom());
        if (dto.getPrenom() != null) user.setPrenom(dto.getPrenom());
        if (dto.getTelephone() != null) user.setTelephone(dto.getTelephone());
        if (dto.getActif() != null) user.setActif(dto.getActif());

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (RoleType roleType : dto.getRoles()) {
                Role role = roleRepository.findByName(roleType)
                        .orElseThrow(() -> new RuntimeException("Rôle non trouvé"));
                roles.add(role);
            }
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setActif(false);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public long countByRole(RoleType roleType) {
        return userRepository.countByRole(roleType);
    }
}