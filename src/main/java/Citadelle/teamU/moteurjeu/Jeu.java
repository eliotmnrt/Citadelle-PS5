package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.*;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import Citadelle.teamU.moteurjeu.bots.malin.BotFocusRoi;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import com.opencsv.CSVWriter;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu {

    private List<Bot> botListe;
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
        while(maxQuartiersConstruits < 8) {
            tour.prochainTour();
            for (Bot bot: botListe){
                if(bot.getQuartiersConstruits().size() > maxQuartiersConstruits){
                    maxQuartiersConstruits = bot.getQuartiersConstruits().size();
                }
            }
        }
    }
    public List<Bot> getBotListe() {
        return botListe;
    }


    public static void main (String... args){

        Args arg = new Args();
        JCommander.newBuilder()
                .addObject(arg)
                .build()
                .parse(args);

        if(arg.demo){
            System.out.println("demo detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.ALL);
            //Faire une demo
        }else if(arg.two){
            System.out.println("2 thousand detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            //faire 2 fois 1000 stats
        }else if(arg.csv){
            Path path = Paths.get("stats/gamestats.csv");

            try {
                CSVWriter writer = new CSVWriter(new FileWriter(path.toFile()));
                // Remplire un fichier existant
            } catch (IOException e) {
                //crer un fichier
                System.out.println("aaaaah");
                throw new RuntimeException(e);
            }
        }

        Pioche pioche = new Pioche();
        Bot bot1 = new BotFocusRoi(pioche);
        Bot bot2 = new BotConstruitChere(pioche);
        Bot bot3 = new BotConstruitVite(pioche);
        Bot bot4 = new BotAleatoire(pioche);
        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        bot1.setOrdreChoixRole(1);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot4.setOrdreChoixRole(4);
        Jeu jeu = new Jeu(bot1, bot2, bot3, bot4);
        jeu.start();
    }
}
