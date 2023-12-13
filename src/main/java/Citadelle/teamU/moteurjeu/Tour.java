package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;

import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    private Role[] roles;
    public Tour(ArrayList<Bot> botListe){
        boolean dernierTour=false;
        nbTour++;
        this.botListe = botListe;
        this.roles = new Role[]{new Roi(botListe), new Magicien(botListe)};
        distributionRoles();
        System.out.println("Tour "+ nbTour);
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
