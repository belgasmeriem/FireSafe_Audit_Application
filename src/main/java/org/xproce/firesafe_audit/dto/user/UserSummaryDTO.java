package org.xproce.firesafe_audit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.RoleType;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDTO {

    private Long id;

    private String username;
    private String nom;
    private String prenom;
    private String nomComplet;

    private String email;
    private Set<RoleType> roles;

}