package org.xproce.firesafe_audit.service.etablissement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xproce.firesafe_audit.dao.entities.Etablissement;
import org.xproce.firesafe_audit.dao.entities.PhotoEtablissement;
import org.xproce.firesafe_audit.dao.repositories.EtablissementRepository;
import org.xproce.firesafe_audit.dao.repositories.PhotoEtablissementRepository;
import org.xproce.firesafe_audit.service.file.IFileStorageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PhotoEtablissementServiceImpl implements IPhotoEtablissementService {

    private final PhotoEtablissementRepository photoRepository;
    private final EtablissementRepository etablissementRepository;
    private final IFileStorageService fileStorageService;

    @Override
    public List<PhotoEtablissement> getPhotosByEtablissement(Long etablissementId) {
        return photoRepository.findByEtablissementOrdered(etablissementId);
    }

    @Override
    @Transactional
    public PhotoEtablissement uploadPhoto(Long etablissementId, MultipartFile file, String description, Boolean principale) {
        Etablissement etablissement = etablissementRepository.findById(etablissementId)
                .orElseThrow(() -> new RuntimeException("Établissement non trouvé"));

        String filename = fileStorageService.storeFile(file, "etablissements");

        if (principale != null && principale) {
            photoRepository.findByEtablissementIdAndPrincipaleTrue(etablissementId)
                    .ifPresent(photo -> {
                        photo.setPrincipale(false);
                        photoRepository.save(photo);
                    });
        }

        PhotoEtablissement photo = PhotoEtablissement.builder()
                .nomFichier(file.getOriginalFilename())
                .cheminFichier(filename)
                .description(description)
                .principale(principale != null && principale)
                .tailleFichier(file.getSize())
                .etablissement(etablissement)
                .build();

        return photoRepository.save(photo);
    }

    @Override
    @Transactional
    public void deletePhoto(Long photoId) {
        PhotoEtablissement photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo non trouvée"));
        fileStorageService.deleteFile(photo.getCheminFichier());
        photoRepository.delete(photo);
    }

    @Override
    @Transactional
    public void setPhotoPrincipale(Long photoId) {
        PhotoEtablissement photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new RuntimeException("Photo non trouvée"));

        photoRepository.findByEtablissementIdAndPrincipaleTrue(photo.getEtablissement().getId())
                .ifPresent(p -> {
                    p.setPrincipale(false);
                    photoRepository.save(p);
                });

        photo.setPrincipale(true);
        photoRepository.save(photo);
    }
}