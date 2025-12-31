package org.xproce.firesafe_audit.dao.enums;

public enum TypeFichier {
    IMAGE("Image", new String[]{"jpg", "jpeg", "png", "gif", "bmp"}),
    PDF("Document PDF", new String[]{"pdf"}),
    WORD("Document Word", new String[]{"doc", "docx"}),
    EXCEL("Feuille Excel", new String[]{"xls", "xlsx", "csv"}),
    VIDEO("Vidéo", new String[]{"mp4", "avi", "mov"}),
    AUTRE("Autre fichier", new String[]{});

    private final String libelle;
    private final String[] extensions;

    TypeFichier(String libelle, String[] extensions) {
        this.libelle = libelle;
        this.extensions = extensions;
    }

    public String getLibelle() {
        return libelle;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public boolean accepteExtension(String extension) {
        if (extension == null || extension.isEmpty()) return false;
        String ext = extension.toLowerCase().replace(".", "");
        for (String e : extensions) {
            if (e.equals(ext)) return true;
        }
        return false;
    }

    public static TypeFichier fromExtension(String extension) {
        if (extension == null || extension.isEmpty()) return AUTRE;
        String ext = extension.toLowerCase().replace(".", "");

        for (TypeFichier type : values()) {
            if (type.accepteExtension(ext)) return type;
        }
        return AUTRE;
    }
}