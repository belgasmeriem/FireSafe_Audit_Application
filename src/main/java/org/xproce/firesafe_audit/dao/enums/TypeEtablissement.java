package org.xproce.firesafe_audit.dao.enums;

public enum TypeEtablissement {

    ERP_TYPE_J("Type J - Structures d'accueil pour personnes âgées et personnes handicapées", "J"),
    ERP_TYPE_L("Type L - Salles d'audition, de conférences, de spectacles ou à usages multiples", "L"),
    ERP_TYPE_M("Type M - Magasins de vente, centres commerciaux", "M"),
    ERP_TYPE_N("Type N - Restaurants et débits de boissons", "N"),
    ERP_TYPE_O("Type O - Hôtels et pensions de famille", "O"),
    ERP_TYPE_P("Type P - Salles de danse et salles de jeux", "P"),
    ERP_TYPE_R("Type R - Établissements d'enseignement", "R"),
    ERP_TYPE_S("Type S - Bibliothèques, centres de documentation et de consultation d'archives", "S"),
    ERP_TYPE_T("Type T - Salles d'expositions", "T"),
    ERP_TYPE_U("Type U - Établissements sanitaires", "U"),
    ERP_TYPE_V("Type V - Établissements de culte", "V"),
    ERP_TYPE_W("Type W - Administrations, banques, bureaux", "W"),
    ERP_TYPE_X("Type X - Établissements sportifs couverts", "X"),
    ERP_TYPE_Y("Type Y - Musées", "Y"),

    ERP_TYPE_PA("Type PA - Établissements de plein air", "PA"),
    ERP_TYPE_CTS("Type CTS - Chapiteaux, tentes et structures", "CTS"),
    ERP_TYPE_SG("Type SG - Structures gonflables", "SG"),
    ERP_TYPE_OA("Type OA - Hôtels-restaurants d'altitude", "OA"),
    ERP_TYPE_PS("Type PS - Parcs de stationnement couverts", "PS"),
    ERP_TYPE_GA("Type GA - Gares accessibles au public", "GA"),
    ERP_TYPE_EF("Type EF - Établissements flottants", "EF"),
    ERP_TYPE_BM("Type BM - Bains maures", "BM"),

    BH_1ERE_FAMILLE("Bâtiments d'habitation - 1ère famille", "BH1"),
    BH_2EME_FAMILLE("Bâtiments d'habitation - 2ème famille", "BH2"),
    BH_3EME_FAMILLE("Bâtiments d'habitation - 3ème famille", "BH3"),
    BH_4EME_FAMILLE("Bâtiments d'habitation - 4ème famille", "BH4"),

    IGH_GHA("IGH à usage d'habitation", "GHA"),
    IGH_GHO("IGH à usage d'hôtel", "GHO"),
    IGH_GHR("IGH à usage d'enseignement", "GHR"),
    IGH_GHS("IGH à usage de dépôt d'archives", "GHS"),
    IGH_GHU("IGH à usage sanitaire", "GHU"),
    IGH_GHW("IGH à usage de bureaux", "GHW"),
    IGH_GHZ("IGH à usage d'habitation avec autres activités", "GHZ"),
    IGH_GHTC("IGH à usage de tours de contrôle", "GHTC"),
    IGH_ITGH("Immeubles de très grande hauteur", "ITGH"),

    ERT("Établissement recevant des travailleurs", "ERT"),

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