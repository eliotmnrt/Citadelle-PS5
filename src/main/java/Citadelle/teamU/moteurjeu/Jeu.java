package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.BotConstruitVite;

import java.util.ArrayList;
import java.util.Arrays;

public class Jeu {

    private ArrayList<Bot> botListe;
    private Tour tour;
    public Jeu(Bot...bots) {
        if(bots.length == 0){
            throw new IllegalArgumentException();
        }
        botListe = new ArrayList<>();
        botListe.addAll(Arrays.asList(bots));
        this.tour = new Tour(botListe);
    }

    public void start(){
        int maxQuartiersConstruits = 0;
        while(maxQuartiersConstruits < 7) {
            tour.prochainTour();
            for (Bot bot: botListe){
                if(bot.getQuartiersConstruits().size() > maxQuartiersConstruits){
                    maxQuartiersConstruits = bot.getQuartiersConstruits().size();
                }
            }
        }
    }
    public ArrayList<Bot> getBotListe() {
        return botListe;
    }


    public static void main (String... args){
        Pioche pioche = new Pioche();
        Bot bot1 = new BotAleatoire();
        Bot bot2 = new BotAleatoire();
        Bot bot3 = new BotAleatoire();
        Bot bot4 = new BotAleatoire();
        Jeu jeu = new Jeu(bot1, bot2, bot3, bot4);
        jeu.start();
    }
}
