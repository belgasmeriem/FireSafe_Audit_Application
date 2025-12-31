package org.xproce.firesafe_audit.dao.enums;

public enum Criticite {
    CRITIQUE(5, "Critique - Risque immédiat pour la vie"),
    IMPORTANTE(4, "Importante - Risque significatif"),
    NORMALE(3, "Normale - Amélioration nécessaire"),
    FAIBLE(2, "Faible - Optimisation recommandée");

    private final int ponderation;
    private final String description;

    Criticite(int ponderation, String description) {
        this.ponderation = ponderation;
        this.description = description;
    }

    public int getPonderation() {
        return ponderation;
    }

    public String getDescription() {
        return description;
    }
}