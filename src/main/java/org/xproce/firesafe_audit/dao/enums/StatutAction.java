package org.xproce.firesafe_audit.dao.enums;

public enum StatutAction {
    A_FAIRE("À faire"),
    EN_COURS("En cours de réalisation"),
    TERMINE("Terminée et vérifiée"),
    ANNULE("Annulée"),
    EN_RETARD("En retard");

    private final String libelle;

    StatutAction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}