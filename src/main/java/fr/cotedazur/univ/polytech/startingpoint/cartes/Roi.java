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
                bot.setOr(bot.getOr() + 1);
            }
        }
    }
    @Override
    public void actionSpecial(Bot bot){
        orQuartierJaune(bot);
    }
}
