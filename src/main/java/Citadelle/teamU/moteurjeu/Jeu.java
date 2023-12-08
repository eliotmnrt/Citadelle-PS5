package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;

import java.util.ArrayList;

public class Jeu {

    private ArrayList<Bot> bot;
    public Jeu() {
        Pioche pioche = new Pioche();
        bot = new ArrayList<>();
        Bot bot1 = new BotAleatoire();
        bot.add(bot1);

        while(bot.get(0).getQuartiersConstruits().size() < 8) {
            Tour tour = new Tour(bot.get(0));
        }
    }

    public ArrayList<Bot> getBot() {
        return bot;
    }

    public static void main (String... args){
        Jeu jeu = new Jeu();
    }
}
