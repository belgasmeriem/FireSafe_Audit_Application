package org.xproce.firesafe_audit.dao.enums;

public enum NiveauUrgence {
    IMMEDIATE("Immédiate - Sous 48h", 2),
    COURT_TERME("Court terme - Sous 1 mois", 30),
    MOYEN_TERME("Moyen terme - Sous 3 mois", 90),
    LONG_TERME("Long terme - Sous 6 mois", 180);

    private final String libelle;
    private final int delaiJours;

    NiveauUrgence(String libelle, int delaiJours) {
        this.libelle = libelle;
        this.delaiJours = delaiJours;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getDelaiJours() {
        return delaiJours;
    }
}