package org.xproce.firesafe_audit.dto.norme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NormeDTO {

    private Long id;

    private String code;

    private String nom;

    private String description;

    private String pays;

    private String version;

    private LocalDate dateVigueur;

    private Set<TypeEtablissement> typesEtablissements;

    private Boolean actif;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    private Integer nombreCriteres;
}