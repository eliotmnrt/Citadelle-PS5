package fr.cotedazur.univ.polytech.startingpoint.cartes;

import java.awt.*;

public enum Quartier {

    PRISON("prison", 2, "none"), TEMPLE("temple", 5, "blue"), TERRAIN_DE_BATAILLE("terrain de bataille", 3,"rouge"),
    TAVERNE("taverne", 1, "vert");

    private final String nom;
    private final int cout;
    private final String couleur;

    Quartier(String nom, int cout, String couleur){
        this.nom = nom;
        this.cout = cout;
        this.couleur = couleur;
    }

    public String getNom() {
        return nom;
    }

    public int getCout() {
        return cout;
    }

    public String getCouleur() {
        return couleur;
    }
}
