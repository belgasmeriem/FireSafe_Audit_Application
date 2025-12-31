package org.xproce.firesafe_audit.dao.enums;

public enum TypeAudit {
    INITIAL("Audit initial"),
    PERIODIQUE("Audit périodique réglementaire"),
    CONTRE_VISITE("Contre-visite de vérification"),
    EXCEPTIONNEL("Audit exceptionnel suite incident");

    private final String libelle;

    TypeAudit(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}