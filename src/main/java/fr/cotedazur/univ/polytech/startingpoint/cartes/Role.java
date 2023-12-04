package fr.cotedazur.univ.polytech.startingpoint.cartes;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Pioche;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.BotAleatoire;

import java.util.*;
public class Role {
    protected Bot bot;
    public <T extends  Bot> Role(T bot){
        this.bot=bot;
    }

    public Quartier piocheQuartier(){
        // premièrement piocher simplement un quartier aléatoire
        // éventuellement, il faudrait en piocher 2 et faire un choix sur le meilleur des deux
        return Pioche.piocherQuartier();
    }
    public void prendreOr(){
        bot.setOr(bot.getOr()+2);
    }
}
