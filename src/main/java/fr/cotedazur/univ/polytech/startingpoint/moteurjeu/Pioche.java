package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Pioche {
    private static ArrayList<Quartier> pioche;

    public Pioche(){
        pioche = new ArrayList<>();
        pioche.addAll(Arrays.asList(Quartier.values()));
        Collections.shuffle(pioche);
    }

    /**
     * methode pour piocher 1 quartier
     * @return Quartier
     */
    public static Quartier piocherQuartier(){
        return pioche.remove(0);
    }
}
