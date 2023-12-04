package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.BotAleatoire;

public class Jeu {

    public Jeu() {
        BotAleatoire bot1 = new BotAleatoire();
        Pioche pioche = new Pioche();
        for(int i=0 ; i< 3 ; i++) {
            Tour tour = new Tour(bot1);
        }
    }

    public static void main (String... args){
        Jeu jeu = new Jeu();

    }
}
