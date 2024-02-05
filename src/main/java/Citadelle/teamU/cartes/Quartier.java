package Citadelle.teamU.cartes;


public enum Quartier implements Comparable<Quartier> {

    TEMPLE("temple",1,TypeQuartier.BLEUE, 3),EGLISE("église",2,TypeQuartier.BLEUE, 4),MONASTERE("monastère",3,TypeQuartier.BLEUE, 3),CATHEDRALE("cathédrale",5,TypeQuartier.BLEUE, 2),

    MANOIR("manoir",3,TypeQuartier.JAUNE, 5),CHATEAU("château",4,TypeQuartier.JAUNE, 4),PALAIS("palais",5,TypeQuartier.JAUNE, 2),

    TAVERNE("taverne",1,TypeQuartier.VERT, 5),ECHOPPE("échoppe",2,TypeQuartier.VERT, 3),MARCHE("marché",2,TypeQuartier.VERT, 4),COMPTOIR("comptoir",3,TypeQuartier.VERT, 3),PORT("port",4,TypeQuartier.VERT, 3),HOTEL_DE_VILLE("hôtel de ville",5,TypeQuartier.VERT, 2),

    TOUR_DE_GUET("tour de guet",1,TypeQuartier.ROUGE, 3),PRISON("prison",2,TypeQuartier.ROUGE, 3),TERRAIN_DE_BATAILLE("terrain de bataille", 3,TypeQuartier.ROUGE, 3),FORTERESSE("forteresse",5,TypeQuartier.ROUGE, 2),

    COUR_DES_MIRACLES("cour des miracles",2,TypeQuartier.VIOLET, 1),DONJON("donjon",3,TypeQuartier.VIOLET,2),LABORATOIRE("laboratoire",5,TypeQuartier.VIOLET, 1),MANUFACTURE("manufacture",5,TypeQuartier.VIOLET, 1),OBSERVATOIRE("observatoire",5,TypeQuartier.VIOLET, 1),
    CIMETIERE("cimetière",5,TypeQuartier.VIOLET, 1),BIBLIOTHEQUE("bibliothèque",6,TypeQuartier.VIOLET, 1),ECOLE_DE_MAGIE("école de magie",6,TypeQuartier.VIOLET, 1),UNIVERSITE("université",6,TypeQuartier.VIOLET, 1),DRACOPORT("dragoport",6,TypeQuartier.VIOLET, 1);


    private final String nom;
    private final int cout;
    private final TypeQuartier typeQuartier;
    private  int nbCartes;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_WHITE = "\033[0;37m";

    Quartier(String nom, int cout, TypeQuartier typeQuartier, int nbCartes){
        this.nom = nom;
        this.cout = cout;
        this.typeQuartier = typeQuartier;
        this.nbCartes = nbCartes;
    }

    public int getCout() {
        return cout;
    }

    public TypeQuartier getCouleur() {
        return typeQuartier;
    }
    public int getNbCartes(){ return nbCartes;}

    @Override
    public String toString(){
        String color ="";
        switch (this.typeQuartier){
            case VERT -> color = ANSI_GREEN;
            case ROUGE -> color = ANSI_RED;
            case JAUNE -> color = ANSI_YELLOW;
            case VIOLET -> color = ANSI_PURPLE;
            case BLEUE -> color = ANSI_BLUE;
        }
        return color+this.nom+ " ("+this.cout+")"+ANSI_WHITE ;
    }
    public TypeQuartier getTypeQuartier() {
        return typeQuartier;
    }
}
