package org.xproce.firesafe_audit.dao.enums;

public enum TypeEtablissement {

    TYPE_J("Type J - Structures d'accueil pour personnes âgées et handicapées", "J"),
    TYPE_L("Type L - Salles de conférences, réunions, spectacles", "L"),
    TYPE_M("Type M - Magasins de vente, centres commerciaux", "M"),
    TYPE_N("Type N - Restaurants et débits de boissons", "N"),
    TYPE_O("Type O - Hôtels et pensions de famille", "O"),
    TYPE_P("Type P - Salles de danse et salles de jeux", "P"),
    TYPE_R("Type R - Établissements d'enseignement", "R"),
    TYPE_S("Type S - Bibliothèques, centres de documentation", "S"),
    TYPE_T("Type T - Salles d'expositions", "T"),
    TYPE_U("Type U - Établissements de soins (hôpitaux, cliniques)", "U"),
    TYPE_V("Type V - Établissements de culte", "V"),
    TYPE_W("Type W - Administrations, banques, bureaux", "W"),
    TYPE_X("Type X - Établissements sportifs couverts", "X"),
    TYPE_Y("Type Y - Musées", "Y"),
    TYPE_PA("Type PA - Établissements de plein air", "PA"),
    TYPE_CTS("Type CTS - Chapiteaux, tentes et structures", "CTS"),
    TYPE_SG("Type SG - Structures gonflables", "SG"),
    TYPE_PS("Type PS - Parcs de stationnement couverts", "PS"),
    TYPE_GA("Type GA - Gares accessibles au public", "GA"),
    TYPE_OA("Type OA - Hôtels-restaurants d'altitude", "OA"),
    TYPE_REF("Type REF - Refuges de montagne", "REF"),

    ETABLISSEMENT_INDUSTRIEL("Établissement industriel (ICPE)", "IND"),
    IGH("Immeuble de Grande Hauteur", "IGH"),
    ERT("Établissement Recevant des Travailleurs", "ERT"),
    AUTRE("Autre type d'établissement", "AUT");

    private final String libelle;
    private final String code;

    TypeEtablissement(String libelle, String code) {
        this.libelle = libelle;
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getCode() {
        return code;
    }
}
