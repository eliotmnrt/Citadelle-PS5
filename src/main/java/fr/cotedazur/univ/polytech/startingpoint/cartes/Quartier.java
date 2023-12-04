package fr.cotedazur.univ.polytech.startingpoint.cartes;

import java.awt.*;

public enum Quartier {

    PRISON("prison", 2, null), TEMPLE("temple", 5, TypeQuartier.BLEUE), TERRAIN_DE_BATAILLE("terrain de bataille", 3,TypeQuartier.ROUGE),
    TAVERNE("taverne", 1, TypeQuartier.VERT), CHATEAU("chateau",4,TypeQuartier.JAUNE);

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

    public String getCouleur() {
        return typeQuartier.toString();
    }
}
