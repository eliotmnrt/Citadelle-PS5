package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.*;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import Citadelle.teamU.moteurjeu.bots.malin.BotFocusRoi;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.beust.jcommander.JCommander;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Jeu {

    private List<Bot> botListe;
    private static Tour tour;

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



    public static void simulation1(int nombre){
        int i=1;
        int cptConstruitChere=0;
        int cptConstruitVite=0;
        int cptAleatoire=0;
        int cptQuiFocusRoi=0;
        while(i<nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotConstruitChere(pioche);
            Bot bot3 = new BotConstruitVite(pioche);
            Bot bot4 = new BotAleatoire(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            //System.out.println(vainqueur);
            //System.out.println(vainqueur.toString());
            if(vainqueur.toString().contains("BotConstruitChere")){
                cptConstruitChere++;
            }
            if(vainqueur.toString().contains("Bot_qui_construit_vite")){
                cptConstruitVite++;
            }
            if(vainqueur.toString().contains("BotAleatoire")){
                cptAleatoire++;
            }
            if(vainqueur.toString().contains("BotQuiFocusRoi")){
                cptQuiFocusRoi++;
            }
            i++;
        }
        float pourcent1=((float)cptConstruitChere/nombre)*100;
        float pourcent2=((float)cptQuiFocusRoi/nombre)*100;
        float pourcent3=((float)cptConstruitVite/nombre)*100;
        float pourcent4=((float)cptAleatoire/nombre)*100;
        System.out.println( "BotConstruitChere: "+pourcent1+"% ,BotQuiFocusRoi: "+pourcent2+"% ,Bot_qui_construit_vite :"+pourcent3+",% BotAleatoire :"+pourcent4+"%");

    }
    public static void simulation2(int nombre){
        int i=1;
        int cptQuiFocusRoi1=0;
        int cptQuiFocusRoi2=0;
        int cptQuiFocusRoi3=0;
        int cptQuiFocusRoi4=0;

        while(i<=nombre){
            Pioche pioche = new Pioche();
            Bot bot1 = new BotFocusRoi(pioche);
            Bot bot2 = new BotFocusRoi(pioche);
            Bot bot3 = new BotFocusRoi(pioche);
            Bot bot4 = new BotFocusRoi(pioche);
            //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
            JouerPartie(bot1,bot2,bot3,bot4);
            Bot vainqueur=tour.getLeVainqueur();
            int parse=Integer.parseInt(vainqueur.toString().substring(14));
            if (parse%4==1) cptQuiFocusRoi1++;
            if (parse%4==2) cptQuiFocusRoi2++;
            if (parse%4==3) cptQuiFocusRoi3++;
            if (parse%4==0) cptQuiFocusRoi4++;
            i++;
        }

        float pourcent1=((float)cptQuiFocusRoi1/nombre)*100;
        float pourcent2=((float)cptQuiFocusRoi2/nombre)*100;
        float pourcent3=((float)cptQuiFocusRoi3/nombre)*100;
        float pourcent4=((float)cptQuiFocusRoi4/nombre)*100;
        System.out.println( "BotQuiFocusRoi1: "+pourcent1+"% ,BotQuiFocusRoi2: "+pourcent2+"% ,BotQuiFocusRoi3 :"+pourcent3+",% BotQuiFocusRoi4 :"+pourcent4+"%");

    }

    // j'ai mis en static parce que ça me faisait une erreur
    public static void JouerPartie(Bot bot1, Bot bot2, Bot bot3, Bot bot4){
        bot1.setOrdreChoixRole(1);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot4.setOrdreChoixRole(4);
        Jeu jeu = new Jeu(bot1, bot2, bot3, bot4);
        jeu.start();
    }
    public static void main (String... args){

        Args arg = new Args();
        JCommander.newBuilder()
                .addObject(arg)
                .build()
                .parse(args);

        if(arg.demo){
            //Faire une demo
            System.out.println("demo detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.ALL);

        }else if(arg.two){
            //faire 2 fois 1000 stats
            System.out.println("2 thousand detecté");
            Logger.getLogger("LOGGER").getParent().setLevel(Level.OFF);
            simulation1(1000);
            simulation2(1000);
        }

        Pioche pioche = new Pioche();
        Bot bot1 = new BotFocusRoi(pioche);
        Bot bot2 = new BotConstruitChere(pioche);
        Bot bot3 = new BotConstruitVite(pioche);
        Bot bot4 = new BotAleatoire(pioche);
        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        //JouerPartie(bot1,bot2,bot3,bot4);



        
    }
}
