package org.xproce.firesafe_audit.service.norme;

import org.xproce.firesafe_audit.dao.enums.TypeEtablissement;
import org.xproce.firesafe_audit.dto.norme.NormeCreateDTO;
import org.xproce.firesafe_audit.dto.norme.NormeDTO;

import java.util.List;

public interface INormeService {
    List<NormeDTO> getAllNormes();
    List<NormeDTO> getActiveNormes();
    List<NormeDTO> getNormesActives();
    NormeDTO getNormeById(Long id);
    List<NormeDTO> getNormesByPays(String pays);
    List<NormeDTO> getNormesByTypeEtablissement(TypeEtablissement type);
    List<NormeDTO> searchNormes(String search);
    List<NormeDTO> getNormesSansCriteres();
    NormeDTO createNorme(NormeCreateDTO dto);
    NormeDTO updateNorme(Long id, NormeCreateDTO dto);
    void deleteNorme(Long id);

    long countAllNormes();
    long countActiveNormes();
    long countNormesActives();
    long countNormesByPays(String pays);
    long countNormesSansCriteres();
}