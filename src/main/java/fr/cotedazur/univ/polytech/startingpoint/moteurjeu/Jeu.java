package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.BotAleatoire;

public class Jeu {

    public Jeu() {
        Pioche pioche = new Pioche();
        BotAleatoire bot1 = new BotAleatoire();

        while(bot1.getQuartiersConstruits().size() < 8) {
            Tour tour = new Tour(bot1);
        }
    }

    public static void main (String... args){
        Jeu jeu = new Jeu();

    }
}
