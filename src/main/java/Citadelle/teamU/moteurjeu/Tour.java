package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private Bot bot1;
    private static int nbTour = 0;
    Role[] roles = {new Roi()};
    public Tour(Bot bot){
        nbTour++;
        this.bot1 = bot;

        distributionRoles();
        ArrayList<Quartier> choixDeBase=bot1.faireActionDeBase();
        bot1.faireActionSpecialRole();
        Affichage affiche=new Affichage(bot1,nbTour, choixDeBase);
        affiche.afficheBot();
        affiche.afficheChoixDeBase();
        /*

        System.out.println("Tour "+nbTour);
        System.out.println("Role du BOT aléatoire : "+bot1.getRole());
        System.out.println("Quartier dans le main du BOT aléatoire: "+bot1.getQuartierMain());
        System.out.println("Quartier construit du BOT aléatoire: "+bot1.getQuartiersConstruits());
        System.out.println("Nombre d'or du BOT aléatoire: "+bot1.getOr());
        System.out.println("Points de victore du BOT aléatoire: "+bot1.getScore()+"\n");

         */
    }
    private void distributionRoles(){
        bot1.choisirRole(roles);
    }


}
