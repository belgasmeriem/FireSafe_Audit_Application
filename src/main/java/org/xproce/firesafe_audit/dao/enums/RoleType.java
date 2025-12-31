package org.xproce.firesafe_audit.dao.enums;

public enum RoleType {
    ADMIN("Administrateur"),
    MANAGER("Manager"),
    AUDITOR("Auditeur");

    private final String libelle;

    RoleType(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
