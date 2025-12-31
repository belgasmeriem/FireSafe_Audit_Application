package org.xproce.firesafe_audit.dto.auth;

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
public class LoginResponse {

    private String token;

    private String type = "Bearer";

    private Long id;

    private String username;

    private String email;

    private String nom;

    private String prenom;

    private Set<RoleType> roles;
}