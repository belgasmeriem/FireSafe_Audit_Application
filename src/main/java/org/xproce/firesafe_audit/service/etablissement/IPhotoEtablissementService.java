package org.xproce.firesafe_audit.service.etablissement;

import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.entities.PhotoEtablissement;

import java.util.List;

public interface IPhotoEtablissementService {
    List<PhotoEtablissement> getPhotosByEtablissement(Long etablissementId);
    PhotoEtablissement uploadPhoto(Long etablissementId, MultipartFile file, String description, Boolean principale);
    void deletePhoto(Long photoId);
    void setPhotoPrincipale(Long photoId);
}