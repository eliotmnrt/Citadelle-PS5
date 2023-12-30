package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.cartes.Quartier;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    ArrayList<Role> roles = new ArrayList<>();
    public Tour(ArrayList<Bot> botListe){
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        roles.add(new Magicien(botListe));

        boolean dernierTour=false;
        nbTour++;
        this.botListe = botListe;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        System.out.println(botListe);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            Affichage affiche = new Affichage(bot);
            affiche.afficheBot();
            bot.faireActionSpecialRole();
            affiche.afficheActionSpeciale(bot);
            ArrayList<Quartier> choixDeBase = bot.faireActionDeBase();
            affiche.setChoixDeBase(choixDeBase);
            affiche.afficheChoixDeBase(choixDeBase);

            if(bot.getQuartiersConstruits().size()==8) dernierTour=true;

        }
        if (dernierTour){
            Affichage affichageFin = new Affichage(botListe);
            affichageFin.afficheLeVainqueur();

        }


    }

    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(roles);
        }
    }


}
