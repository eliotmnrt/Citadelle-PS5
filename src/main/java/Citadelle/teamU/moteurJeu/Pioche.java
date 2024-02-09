package Citadelle.teamU.moteurJeu;

import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.shuffle;

public class Pioche {
    private final List<Quartier> laPioche;

    public Pioche(){
        laPioche = new ArrayList<>();
        for (Quartier quartier: Quartier.values()){
            for (int i=0; i<quartier.getNbCartes(); i++){
                laPioche.add(quartier);
            }
        }
        shuffle(laPioche);
    }
    public List<Quartier> getPioche() {
        return laPioche;
    }


    /**
     * methode pour piocher 1 quartier
     * @return Quartier
     */
    public Quartier piocherQuartier(){
        if(laPioche.isEmpty()){
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
