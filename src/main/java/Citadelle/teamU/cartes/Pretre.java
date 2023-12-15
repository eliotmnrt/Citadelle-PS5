package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.Objects;

public class Pretre extends Role{
    public Pretre(){
        super.ordre=5;
    }
    public void OrQuartierBleue(Bot bot){
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.BLEUE)){
                bot.changerOr(1);
            }
        }
    }
    public void actionSpecial(Bot bot){
        OrQuartierBleue(bot);
    }
    public String toString() {
        return "Prêtre";
    }

}
