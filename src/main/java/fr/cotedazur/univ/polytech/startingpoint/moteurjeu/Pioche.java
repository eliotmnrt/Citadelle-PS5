package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import static java.util.Collections.shuffle;

public class Pioche {
    private static ArrayList<Quartier> pioche;

    public Pioche(){
        pioche = new ArrayList<>();
        for (Quartier quartier: Quartier.values()){
            for (int i=0; i<quartier.getNbCartes(); i++){
                pioche.add(quartier);
            }
        }
        shuffle(pioche);
    }
    public static ArrayList<Quartier> getPioche() {
        return pioche;
    }


    /**
     * methode pour piocher 1 quartier
     * @return Quartier
     */
    public static Quartier piocherQuartier(){
        return pioche.remove(0);
    }
    public static void remettreDansPioche(Quartier quartier){
        pioche.add(quartier);
    }
}
