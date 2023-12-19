package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.Objects;

public class Roi extends Role{


    public Roi() {
        super.ordre = 4;
    }
    public void orQuartierJaune(Bot bot){
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.JAUNE)){
                bot.changerOr(1);
            }
        }
    }
    public void actionSpecial(Bot bot){

        orQuartierJaune(bot);
        Affichage.afficheActionSpecialRoi(bot);
    }

    @Override
    public String toString() {
        return "Roi";
    }
}
