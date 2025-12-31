package org.xproce.firesafe_audit.service.etablissement;

import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementCreateDTO;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementDTO;
import org.xproce.firesafe_audit.dto.etablissement.EtablissementUpdateDTO;

import java.util.List;

public interface IEtablissementService {
    List<EtablissementDTO> getAllEtablissements();
    List<EtablissementDTO> getActiveEtablissements();
    EtablissementDTO getEtablissementById(Long id);
    List<EtablissementDTO> getEtablissementsByType(TypeEtablissement type);
    List<EtablissementDTO> getEtablissementsByVille(String ville);
    List<EtablissementDTO> searchEtablissements(String search);
    EtablissementDTO createEtablissement(EtablissementCreateDTO dto);
    EtablissementDTO updateEtablissement(Long id, EtablissementUpdateDTO dto);
    void deleteEtablissement(Long id);
    long countActiveEtablissements();
    long countByType(TypeEtablissement type);
}