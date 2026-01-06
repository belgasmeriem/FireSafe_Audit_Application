package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.Role;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.dto.user.UserSummaryDTO;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .telephone(user.getTelephone())
                .actif(user.getActif())
                .roles(extractRoleTypes(user.getRoles()))
                .dateCreation(user.getDateCreation())
                .derniereConnexion(user.getDerniereConnexion())
                .nomComplet(user.getNomComplet())
                .build();
    }

    public UserSummaryDTO toSummaryDTO(User user) {
        if (user == null) return null;

        return UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nom(user.getNom())
                .prenom(user.getPrenom())
                .nomComplet(user.getNomComplet())
                .email(user.getEmail())
                .roles(extractRoleTypes(user.getRoles()))
                .build();
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setTelephone(dto.getTelephone());
        user.setActif(dto.getActif());

        return user;
    }


    private Set<RoleType> extractRoleTypes(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }

        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}