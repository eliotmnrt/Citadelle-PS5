package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Architecte;
import Citadelle.teamU.cartes.Quartier;

import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    ArrayList<Role> roles = new ArrayList<>(Arrays.asList(new Roi(),new Roi(), new Architecte(), new Architecte()));
    public Tour(ArrayList<Bot> botListe){
        boolean dernierTour=false;
        nbTour++;
        this.botListe = botListe;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            ArrayList<Quartier> choixDeBase=bot.faireActionDeBase();
            bot.faireActionSpecialRole();
            Affichage affiche=new Affichage(bot,choixDeBase);
            affiche.afficheBot();
            affiche.afficheChoixDeBase();
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
    public ArrayList<Bot> getBotListe(){
        return botListe;
    }


}
