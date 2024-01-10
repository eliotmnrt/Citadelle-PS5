package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;

import static java.util.Collections.shuffle;

public class Pioche {
    private ArrayList<Quartier> pioche;

    public Pioche(){
        pioche = new ArrayList<>();
        for (Quartier quartier: Quartier.values()){
            for (int i=0; i<quartier.getNbCartes(); i++){
                pioche.add(quartier);
            }
        }
        shuffle(pioche);
    }
    public ArrayList<Quartier> getPioche() {
        return pioche;
    }


    /**
     * methode pour piocher 1 quartier
     * @return Quartier
     */
    public Quartier piocherQuartier(){
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
    public void remettreDansPioche(Quartier quartier){
        pioche.add(quartier);
    }
}
