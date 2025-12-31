package org.xproce.firesafe_audit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
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
public class UserUpdateDTO {

    @Email(message = "Email invalide")
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String nom;

    @Size(max = 50)
    private String prenom;

    @Size(max = 20)
    private String telephone;

    private Boolean actif;

    private Set<RoleType> roles;
}