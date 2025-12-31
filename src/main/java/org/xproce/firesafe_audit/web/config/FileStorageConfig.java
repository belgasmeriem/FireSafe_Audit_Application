package org.xproce.firesafe_audit.web.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class FileStorageConfig {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadDir));
            Files.createDirectories(Paths.get(uploadDir, "preuves"));
            Files.createDirectories(Paths.get(uploadDir, "etablissements"));
            Files.createDirectories(Paths.get(uploadDir, "rapports"));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer les répertoires d'upload", e);
        }
    }
}