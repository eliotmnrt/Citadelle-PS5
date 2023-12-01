package fr.cotedazur.univ.polytech.startingpoint.cartes;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Bot;

import java.util.*;
public class Role {
    protected Bot bot;
    public Role(Bot bot){
        this.bot=bot;
    }

    public Quartier piocheQuartier(Quartier[] quartiersAPiocher) {
        //premierement piocher simplement un quartier aleatoire
        // eventuellement il faudrait en piocher 2 et faire un choix sur le meilleur des deux
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(quartiersAPiocher.length);
        return quartiersAPiocher[intAleatoire];
    }
    public void prendreOr(){
        bot.setOr(bot.getOr()+2);
    }
    
}
