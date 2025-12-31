package org.xproce.firesafe_audit.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {
    String storeFile(MultipartFile file, String category);
    byte[] loadFile(String filePath);
    void deleteFile(String filePath);
    boolean fileExists(String filePath);
    String getContentType(String filePath);
}