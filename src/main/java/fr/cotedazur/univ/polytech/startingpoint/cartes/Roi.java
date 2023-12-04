package fr.cotedazur.univ.polytech.startingpoint.cartes;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;

import java.util.Objects;

public class Roi extends Role{


    public Roi(Bot bot) {
        super(bot);
    }
    public void orQuartierJaune(){
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(quartier.getCouleur()){
                bot.setOr(bot.getOr() + 1);
            }
        }
    }
}
