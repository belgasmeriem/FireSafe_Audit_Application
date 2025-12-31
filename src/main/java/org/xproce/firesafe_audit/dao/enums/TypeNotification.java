package org.xproce.firesafe_audit.dao.enums;

public enum TypeNotification {
    AUDIT_PLANIFIE("Audit planifié"),
    AUDIT_TERMINE("Audit terminé"),
    VALIDATION_REQUISE("Validation requise"),
    NON_CONFORMITE_CRITIQUE("Non-conformité critique détectée"),
    AUDIT_EXPIRE("Audit non réalisé à la date prévue"),
    COMMENTAIRE_AJOUTE("Nouveau commentaire"),
    MENTION("Vous avez été mentionné"),
    RAPPORT_GENERE("Rapport généré et disponible"),
    SYSTEME("Notification système");

    private final String libelle;

    TypeNotification(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}