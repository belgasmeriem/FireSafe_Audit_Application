package org.xproce.firesafe_audit.dao.enums;

public enum StatutAudit {
    PLANIFIE("Planifié"),
    EN_COURS("En cours de réalisation"),
    TERMINE("Terminé - En attente de validation"),
    VALIDE("Validé"),
    REJETE("Rejeté - Nécessite révision"),
    ARCHIVE("Archivé");

    private final String libelle;

    StatutAudit(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}