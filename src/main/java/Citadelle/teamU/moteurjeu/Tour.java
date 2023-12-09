package Citadelle.teamU.moteurjeu;

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
        for (Bot bot: botListe){
            bot.faireActionDeBase();
            bot.faireActionSpecialRole();
            System.out.println("Tour "+ nbTour);
            System.out.println(bot.toString());
        }
    }

    private void distributionRoles(){
        for (Bot bot: botListe){
            bot.choisirRole(roles);
        }
    }

}
