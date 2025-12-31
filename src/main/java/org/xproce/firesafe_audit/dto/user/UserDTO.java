package org.xproce.firesafe_audit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.RoleType;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String nom;

    private String prenom;

    private String telephone;

    private Boolean actif;

    private Set<RoleType> roles;

    private LocalDateTime dateCreation;

    private LocalDateTime derniereConnexion;

    private String nomComplet;
}