package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.*;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    ArrayList<Role> roles = new ArrayList<>();
    public Tour(ArrayList<Bot> botListe){
        roles.add(new Roi());
        roles.add(new Roi());
        roles.add(new Roi());
        roles.add(new Roi());

        boolean dernierTour=false;
        nbTour++;
        this.botListe = botListe;
        distributionRoles();
        Affichage.afficheTour(nbTour);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            Affichage.afficheBot(bot);
            bot.faireActionDeBase();
            bot.construire();
            bot.faireActionSpecialRole();
            if(bot.getQuartiersConstruits().size()>=8) dernierTour=true;

        }
        if (dernierTour){
            Bot botVainqueur=CalculLeVainqueur();
            Affichage.afficheVainqueur(botVainqueur,botVainqueur.getScore());

        }


    }

    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(roles);
        }
    }
    private Bot CalculLeVainqueur() {
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max = 0;
        Bot botVainqueur = botListe.get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        for (Bot bot : botListe) {
            if (bot.getScore() > max) {
                max = bot.getScore();
                botVainqueur = bot;
            }
        }
        return botVainqueur;
    }

}
