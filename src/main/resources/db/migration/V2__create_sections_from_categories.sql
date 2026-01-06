-- Vérifier si des sections existent déjà
DELETE FROM sections WHERE id IN (
    SELECT DISTINCT s.id
    FROM sections s
             INNER JOIN criteres c ON s.code = c.categorie AND s.norme_id = c.norme_id
);


INSERT INTO sections (code, titre, description, norme_id, ordre)
SELECT DISTINCT
    c.categorie AS code,
    CASE
        -- Groupe Accessibilité
        WHEN c.categorie = 'ACCESSIBILITE' THEN 'Accessibilité'

        -- Groupe Structure et Isolation
        WHEN c.categorie = 'ISOLEMENT_TIERS' THEN 'Isolement par rapport aux tiers'
        WHEN c.categorie = 'RESISTANCE_FEU_STRUCTURES' THEN 'Résistance au feu des structures'
        WHEN c.categorie = 'FACADE' THEN 'Façades'
        WHEN c.categorie = 'DISTRIBUTION_INTERIEURE' THEN 'Distribution intérieure'
        WHEN c.categorie = 'LOCAUX_RISQUE' THEN 'Locaux à risque'
        WHEN c.categorie = 'CONDUITS_GAINES' THEN 'Conduits et gaines'

        -- Groupe Dégagements et Aménagements
        WHEN c.categorie = 'DEGAGEMENTS' THEN 'Dégagements'
        WHEN c.categorie = 'AMENAGEMENTS_INTERIEURS' THEN 'Aménagements intérieurs'

        -- Groupe Désenfumage et Ventilation
        WHEN c.categorie = 'DESENFUMAGE' THEN 'Désenfumage'
        WHEN c.categorie = 'VENTILATION' THEN 'Ventilation'

        -- Groupe Chauffage et Installations
        WHEN c.categorie = 'CHAUFFAGE' THEN 'Chauffage'
        WHEN c.categorie = 'INSTALLATIONS_ELECTRIQUES' THEN 'Installations électriques'
        WHEN c.categorie = 'ECLAIRAGE_SECURITE' THEN 'Éclairage de sécurité'

        -- Groupe Moyens de Secours
        WHEN c.categorie = 'MOYENS_SECOURS' THEN 'Moyens de secours'

        -- Groupe Détection et Extinction
        WHEN c.categorie = 'DETECTION_ALARME' THEN 'Détection automatique d''incendie et alarme'
        WHEN c.categorie = 'SYSTEME_EXTINCTION_AUTOMATIQUE' THEN 'Système d''extinction automatique'
        WHEN c.categorie = 'EXTINCTEURS' THEN 'Extincteurs'
        WHEN c.categorie = 'ROBINETS_INCENDIE_ARMEE' THEN 'Robinets d''incendie armés'
        WHEN c.categorie = 'COLONNES_SECHES' THEN 'Colonnes sèches'
        WHEN c.categorie = 'COLONNES_HUMIDES' THEN 'Colonnes humides'

        -- Groupe Circulations et Issues
        WHEN c.categorie = 'CIRCULATION_HORIZONTALE' THEN 'Circulations horizontales'
        WHEN c.categorie = 'ESCALIERS' THEN 'Escaliers'
        WHEN c.categorie = 'ISSUES_SORTIES' THEN 'Issues et sorties'
        WHEN c.categorie = 'ASCENSEURS' THEN 'Ascenseurs'

        -- Groupe Compartimentage et Cloisonnement
        WHEN c.categorie = 'COMPARTIMENTAGE' THEN 'Compartimentage'
        WHEN c.categorie = 'CLOISONNEMENT' THEN 'Cloisonnement'
        WHEN c.categorie = 'COUVERTURES' THEN 'Couvertures'
        WHEN c.categorie = 'PLANCHERS' THEN 'Planchers'
        WHEN c.categorie = 'PLAFONDS_SUSPENDUS' THEN 'Plafonds suspendus'

        -- Groupe Revêtements et Réaction au Feu
        WHEN c.categorie = 'REVETEMENTS_MURAUX' THEN 'Revêtements muraux'
        WHEN c.categorie = 'REVETEMENTS_SOL' THEN 'Revêtements de sol'
        WHEN c.categorie = 'REACTION_FEU_MATERIAUX' THEN 'Réaction au feu des matériaux'

        -- Groupe Locaux Techniques et Stockage
        WHEN c.categorie = 'LOCAUX_TECHNIQUES' THEN 'Locaux techniques'
        WHEN c.categorie = 'STOCKAGE_PRODUITS_DANGEREUX' THEN 'Stockage produits dangereux'

        -- Groupe Formation et Organisation
        WHEN c.categorie = 'FORMATION_PERSONNEL' THEN 'Formation du personnel'
        WHEN c.categorie = 'EXERCICES_EVACUATION' THEN 'Exercices d''évacuation'
        WHEN c.categorie = 'CONSIGNES_SECURITE' THEN 'Consignes de sécurité'
        WHEN c.categorie = 'PLANS_EVACUATION' THEN 'Plans d''évacuation'

        -- Groupe Gestion et Contrôles
        WHEN c.categorie = 'REGISTRE_SECURITE' THEN 'Registre de sécurité'
        WHEN c.categorie = 'VERIFICATION_PERIODIQUE' THEN 'Vérifications périodiques'
        WHEN c.categorie = 'MAINTENANCE_EQUIPEMENTS' THEN 'Maintenance des équipements'
        WHEN c.categorie = 'CONTROLES_REGLEMENTAIRES' THEN 'Contrôles réglementaires'

        -- Groupe Stationnement
        WHEN c.categorie = 'PARCS_STATIONNEMENT' THEN 'Parcs de stationnement'

        -- Autre
        WHEN c.categorie = 'AUTRE' THEN 'Autre'

        -- Fallback : utiliser le nom de la catégorie tel quel
        ELSE c.categorie
        END AS titre,
    CONCAT('Section regroupant les critères de type ', c.categorie) AS description,
    c.norme_id,
    ROW_NUMBER() OVER (PARTITION BY c.norme_id ORDER BY c.categorie) AS ordre
FROM criteres c
WHERE c.categorie IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM sections s
    WHERE s.code = c.categorie AND s.norme_id = c.norme_id
);

-- ========================================
-- ÉTAPE 2 : Lier tous les critères à leurs sections
-- ========================================
UPDATE criteres c
SET section_id = (
    SELECT s.id
    FROM sections s
    WHERE s.code = c.categorie
      AND s.norme_id = c.norme_id
)
WHERE c.categorie IS NOT NULL
  AND c.section_id IS NULL;

-- ========================================
-- ÉTAPE 3 : Vérification
-- ========================================
-- Voir les sections créées par catégorie
SELECT
    s.code,
    s.titre,
    s.norme_id,
    s.ordre,
    COUNT(c.id) as nb_criteres
FROM sections s
         LEFT JOIN criteres c ON c.section_id = s.id
GROUP BY s.id, s.code, s.titre, s.norme_id, s.ordre
ORDER BY s.norme_id, s.ordre;

-- Vérifier les critères sans section (devrait être 0)
SELECT COUNT(*) as criteres_sans_section
FROM criteres
WHERE section_id IS NULL AND categorie IS NOT NULL;

-- Voir la distribution des catégories utilisées
SELECT
    c.categorie,
    COUNT(*) as nb_criteres
FROM criteres c
WHERE c.categorie IS NOT NULL
GROUP BY c.categorie
ORDER BY nb_criteres DESC;