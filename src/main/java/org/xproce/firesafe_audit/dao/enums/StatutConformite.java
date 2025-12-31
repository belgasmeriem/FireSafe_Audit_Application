package org.xproce.firesafe_audit.dao.enums;

public enum StatutConformite {
    CONFORME("Conforme", "✅"),
    NON_CONFORME("Non conforme", "❌"),
    PARTIELLEMENT_CONFORME("Partiellement conforme", "⚠️"),
    NON_APPLICABLE("Non applicable", "⭕"),
    NON_EVALUE("Non évalué", "❔");

    private final String libelle;
    private final String icone;

    StatutConformite(String libelle, String icone) {
        this.libelle = libelle;
        this.icone = icone;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getIcone() {
        return icone;
    }
}