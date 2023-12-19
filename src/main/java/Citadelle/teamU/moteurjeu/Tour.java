package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.cartes.roles.Role;
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
        roles.add(new Pretre());
        roles.add(new Marchand());
        roles.add(new Architecte());

        boolean dernierTour=false;
        nbTour++;
        this.botListe = botListe;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        System.out.println(botListe);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            Affichage affiche=new Affichage(bot);
            affiche.afficheBot();
            bot.faireActionSpecialRole();
            ArrayList<Quartier> choixDeBase=bot.faireActionDeBase();
            affiche.afficheChoixDeBase(choixDeBase.size() >= 2 ? "piocher" : "prendreOr");

            if(bot.getQuartiersConstruits().size()==8) dernierTour=true;

        }
        if (dernierTour){
            Affichage affichageFin=new Affichage(botListe);
            affichageFin.afficheLeVainqueur();

        }


    }

    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(roles);
        }
    }


}
