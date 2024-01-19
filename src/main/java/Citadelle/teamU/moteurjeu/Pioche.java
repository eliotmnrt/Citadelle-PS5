package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;

import static java.util.Collections.shuffle;

public class Pioche {
    private ArrayList<Quartier> laPioche;

    public Pioche(){
        laPioche = new ArrayList<>();
        for (Quartier quartier: Quartier.values()){
            for (int i=0; i<quartier.getNbCartes(); i++){
                laPioche.add(quartier);
            }
        }
        shuffle(laPioche);
    }
    public ArrayList<Quartier> getPioche() {
        return laPioche;
    }


    /**
     * methode pour piocher 1 quartier
     * @return Quartier
     */
    public Quartier piocherQuartier(){
        if(laPioche.size()<=0){
            for (Quartier quartier: Quartier.values()){
                for (int i=0; i<quartier.getNbCartes(); i++){
                    laPioche.add(quartier);
                }
            }
            shuffle(laPioche);
        }
        return laPioche.remove(0);
    }
    public void remettreDansPioche(Quartier quartier){
        laPioche.add(quartier);
    }
}
