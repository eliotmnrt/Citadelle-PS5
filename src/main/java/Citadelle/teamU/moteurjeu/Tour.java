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

    ArrayList<Role> rolesTemp = new ArrayList<>();
    public Tour(ArrayList<Bot> botListe){
        roles.add(new Voleur(botListe, roles));
        roles.add(new Magicien(botListe));
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        roles.add(new Condottiere(botListe));
        this.botListe = botListe;
    }


    public void prochainTour(){

        rolesTemp = new ArrayList<>(roles);
        boolean dernierTour=false;
        nbTour++;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        System.out.println(botListe);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            Affichage affiche = new Affichage(bot);
            affiche.afficheBot();
            bot.faireActionSpecialRole();
            affiche.afficheActionSpeciale(bot);
            affiche.setChoixDeBase(bot.faireActionDeBase());
            affiche.afficheConstruction(bot.construire());

            if(bot.getQuartiersConstruits().size()==7) dernierTour=true;

        }
        if (dernierTour){
            Affichage affichageFin = new Affichage(botListe);
            affichageFin.afficheLeVainqueur();

        }

    }


    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(rolesTemp);
        }
    }


}
