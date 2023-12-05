package fr.cotedazur.univ.polytech.startingpoint.cartes;

import java.awt.*;

public enum Quartier {

    TEMPLE("temple",1,TypeQuartier.BLEUE),EGLISE("église",2,TypeQuartier.BLEUE),MONASTERE("monastère",3,TypeQuartier.BLEUE),CATHEDRALE("cathédrale",5,TypeQuartier.BLEUE),

    MANOIR("manoir",3,TypeQuartier.JAUNE),CHATEAU("château",4,TypeQuartier.JAUNE),PALAIS("palais",5,TypeQuartier.JAUNE),

    TAVERNE("taverne",1,TypeQuartier.VERT),ECHOPPE("échoppe",2,TypeQuartier.VERT),MARCHE("marché",2,TypeQuartier.VERT),COMPTOIR("comptoir",3,TypeQuartier.VERT),PORT("port",4,TypeQuartier.VERT),HOTEL_DE_VILLE("hôtel de ville",5,TypeQuartier.VERT),

    TOUR_DE_GUET("tour de guet",1,TypeQuartier.ROUGE),PRISON("prison",2,TypeQuartier.ROUGE),TERRAIN_DE_BATAILLE("terrain de bataille", 3,TypeQuartier.ROUGE),FORTERESSE("forteresse",5,TypeQuartier.ROUGE),

    COUR_DES_MIRACLES("cour des miracles",2,TypeQuartier.VIOLET),DONJON("donjon",3,TypeQuartier.VIOLET),LABORATOIRE("laboratoire",5,TypeQuartier.VIOLET),MANUFACTURE("manufacture",5,TypeQuartier.VIOLET),OBSERVATOIRE("observatoire",5,TypeQuartier.VIOLET),
    CIMITIERE("cimitière",5,TypeQuartier.VIOLET),BIBLIOTHEQUE("bibliothèque",6,TypeQuartier.VIOLET),ECOLE_DE_MAGIE("école de magie",6,TypeQuartier.VIOLET),UNIVERSITE("université",6,TypeQuartier.VIOLET),DRAGOPORT("dragoport",6,TypeQuartier.VIOLET);


    private final String nom;
    private final int cout;
    private final TypeQuartier typeQuartier;

    Quartier(String nom, int cout, TypeQuartier typeQuartier){
        this.nom = nom;
        this.cout = cout;
        this.typeQuartier = typeQuartier;
    }

    public String getNom() {
        return nom;
    }

    public int getCout() {
        return cout;
    }

    public TypeQuartier getCouleur() {
        return typeQuartier;
    }

    @Override
    public String toString(){
        return this.nom;
    }
}
