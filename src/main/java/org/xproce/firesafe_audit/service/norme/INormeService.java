package org.xproce.firesafe_audit.service.norme;

import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.norme.NormeCreateDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;

import java.util.List;

public interface INormeService {
    List<NormeDTO> getAllNormes();
    List<NormeDTO> getActiveNormes();
    NormeDTO getNormeById(Long id);
    List<NormeDTO> getNormesByPays(String pays);
    List<NormeDTO> getNormesByTypeEtablissement(TypeEtablissement type);
    List<NormeDTO> searchNormes(String search);
    NormeDTO createNorme(NormeCreateDTO dto);
    NormeDTO updateNorme(Long id, NormeCreateDTO dto);
    void deleteNorme(Long id);
    long countActiveNormes();
}