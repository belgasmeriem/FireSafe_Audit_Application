package org.xproce.firesafe_audit.dao.enums;

public enum Categorie {
    DETECTION("Détection incendie"),
    ALARME("Alarme et alerte"),
    EXTINCTION("Moyens d'extinction"),
    DESENFUMAGE("Désenfumage"),
    EVACUATION("Évacuation et dégagements"),
    ECLAIRAGE_SECOURS("Éclairage de sécurité"),
    FORMATION("Formation du personnel"),
    MAINTENANCE("Maintenance et vérifications"),
    STRUCTURE("Réaction au feu des matériaux"),
    COMPARTIMENTAGE("Compartimentage"),
    CONSIGNES("Consignes de sécurité"),
    REGISTRES("Registres et documents"),
    AUTRE("Autre");

    private final String libelle;

    Categorie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}