package org.xproce.firesafe_audit.dto.mapper;

import org.springframework.stereotype.Component;
import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.dto.user.UserSummaryDTO;

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
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()))
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
                .nomComplet(user.getNomComplet())
                .email(user.getEmail())
                .build();
    }
}