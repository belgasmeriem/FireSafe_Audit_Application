
package org.xproce.firesafe_audit.dto.norme;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDTO {

    private Long id;

    private String code;

    private String titre;

    private String description;

    private Integer ordre;

    private Long normeId;

    private String normeNom;

    private List<CritereDTO> criteres;

    private Integer nombreCriteres;
}