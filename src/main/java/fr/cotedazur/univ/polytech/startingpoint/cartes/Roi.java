package fr.cotedazur.univ.polytech.startingpoint.cartes;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Bot;

import java.util.Objects;

public class Roi extends Role{


    public Roi(Bot bot) {
        super(bot);
    }
    public void orQuartierJaune(){
        for(Quartier quartier:this.bot.getQuartierConstruit()){
            if(Objects.equals(quartier.getCouleur(), "jaune")){
                bot.setOr(bot.getOr() + 1);
            }
        }

    }
}
