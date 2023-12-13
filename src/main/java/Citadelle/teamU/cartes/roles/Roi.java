package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.Objects;

public class Roi extends Role{


    public Roi() {
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
    }

    @Override
    public String toString() {
        return "Roi";
    }
}
