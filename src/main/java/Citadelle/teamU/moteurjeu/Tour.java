package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    Role[] roles = {new Roi()};
    public Tour(ArrayList<Bot> botListe){
        nbTour++;
        this.botListe = botListe;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        for (Bot bot: botListe){
            ArrayList<Quartier> choixDeBase=bot.faireActionDeBase();
            bot.faireActionSpecialRole();
            Affichage affiche=new Affichage(bot,choixDeBase);
            affiche.afficheBot();
            affiche.afficheChoixDeBase();
        }
    }

    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(roles);
        }
    }


}
