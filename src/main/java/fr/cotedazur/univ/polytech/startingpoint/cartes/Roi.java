package fr.cotedazur.univ.polytech.startingpoint.cartes;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;

import java.util.Objects;

public class Roi extends Role{


    public Roi(Bot bot) {
        super(bot);
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
}
