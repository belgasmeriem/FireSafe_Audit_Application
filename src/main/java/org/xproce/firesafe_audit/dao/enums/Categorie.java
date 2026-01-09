
package org.xproce.firesafe_audit.dao.enums;

public enum Categorie {

    ACCESSIBILITE("Accessibilité"),
    ISOLEMENT_TIERS("Isolement par rapport aux tiers"),
    RESISTANCE_FEU_STRUCTURES("Résistance au feu des structures"),
    FACADE("Façades"),
    DISTRIBUTION_INTERIEURE("Distribution intérieure"),
    LOCAUX_RISQUE("Locaux à risque"),
    CONDUITS_GAINES("Conduits et gaines"),
    DEGAGEMENTS("Dégagements"),
    AMENAGEMENTS_INTERIEURS("Aménagements intérieurs"),
    DESENFUMAGE("Désenfumage"),
    CHAUFFAGE("Chauffage"),
    INSTALLATIONS_ELECTRIQUES("Installations électriques"),
    ECLAIRAGE_SECURITE("Éclairage de sécurité"),
    MOYENS_SECOURS("Moyens de secours"),

    DETECTION_ALARME("Détection automatique d'incendie et alarme"),
    SYSTEME_EXTINCTION_AUTOMATIQUE("Système d'extinction automatique"),
    EXTINCTEURS("Extincteurs"),
    ROBINETS_INCENDIE_ARMEE("Robinets d'incendie armés"),
    COLONNES_SECHES("Colonnes sèches"),
    COLONNES_HUMIDES("Colonnes humides"),

    CIRCULATION_HORIZONTALE("Circulations horizontales"),
    ESCALIERS("Escaliers"),
    ISSUES_SORTIES("Issues et sorties"),
    ASCENSEURS("Ascenseurs"),

    COMPARTIMENTAGE("Compartimentage"),
    CLOISONNEMENT("Cloisonnement"),
    COUVERTURES("Couvertures"),
    PLANCHERS("Planchers"),
    PLAFONDS_SUSPENDUS("Plafonds suspendus"),

    REVETEMENTS_MURAUX("Revêtements muraux"),
    REVETEMENTS_SOL("Revêtements de sol"),
    REACTION_FEU_MATERIAUX("Réaction au feu des matériaux"),

    VENTILATION("Ventilation"),
    LOCAUX_TECHNIQUES("Locaux techniques"),
    STOCKAGE_PRODUITS_DANGEREUX("Stockage produits dangereux"),

    FORMATION_PERSONNEL("Formation du personnel"),
    EXERCICES_EVACUATION("Exercices d'évacuation"),
    CONSIGNES_SECURITE("Consignes de sécurité"),
    EXTINCTEURS_PORTATIFS("Extincteurs portatifs"),
    PLANS_EVACUATION("Plans d'évacuation"),

    REGISTRE_SECURITE("Registre de sécurité"),
    VERIFICATION_PERIODIQUE("Vérifications périodiques"),
    MAINTENANCE_EQUIPEMENTS("Maintenance des équipements"),
    CONTROLES_REGLEMENTAIRES("Contrôles réglementaires"),

    PARCS_STATIONNEMENT("Parcs de stationnement"),

    AUTRE("Autre");

    private final String libelle;

    Categorie(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}