package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;

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
        if(pioche.size()<=0){
            for (Quartier quartier: Quartier.values()){
                for (int i=0; i<quartier.getNbCartes(); i++){
                    pioche.add(quartier);
                }
            }
            shuffle(pioche);
        }
        return pioche.remove(0);
    }
    public static void remettreDansPioche(Quartier quartier){
        pioche.add(quartier);
    }
}
