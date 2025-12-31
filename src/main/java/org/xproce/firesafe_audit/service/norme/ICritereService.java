package org.xproce.firesafe_audit.service.norme;

import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.enums.Categorie;
import org.xproce.firesafe_audit.dto.norme.CritereDTO;

import java.io.IOException;
import java.util.List;

public interface ICritereService {
    List<CritereDTO> getAllCriteres();
    CritereDTO getCritereById(Long id);
    List<CritereDTO> getCriteresByNorme(Long normeId);
    List<CritereDTO> getCriteresByNormeAndCategorie(Long normeId, Categorie categorie);
    List<CritereDTO> getCriteresByCategorie(Categorie categorie);
    List<CritereDTO> searchCriteres(String search);
    CritereDTO createCritere(CritereDTO dto);
    void deleteCritere(Long id);
    List<CritereDTO> importCriteresFromExcel(Long normeId, MultipartFile file) throws IOException;
}