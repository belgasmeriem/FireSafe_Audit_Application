package org.xproce.firesafe_audit.dto.norme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormeCreateDTO {

    @NotBlank(message = "Le code est obligatoire")
    @Size(max = 50)
    private String code;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 200)
    private String nom;

    private String description;

    @Size(max = 100)
    private String pays;

    @Size(max = 20)
    private String version;

    private LocalDate dateVigueur;

    private Set<TypeEtablissement> typesEtablissements;
}