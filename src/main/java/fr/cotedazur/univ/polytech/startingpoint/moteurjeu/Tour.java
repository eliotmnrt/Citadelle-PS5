package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Roi;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private Bot bot1;
    private static int nbTour = 0;
    Role[] roles = {new Roi(bot1)};
    public <T extends Bot> Tour(T bot){
        nbTour++;
        this.bot1 = bot;

        distributionRoles();
        bot1.faireActionDeBase();
        bot1.faireActionSpecialRole();

        System.out.println("Tour "+nbTour);
        System.out.println("Quartier dans le main du BOT1: "+bot1.getQuartierMain());
        System.out.println("Quartier construit du BOT1: "+bot1.getQuartiersConstruits());
        System.out.println("Nombre d'or du BOT1: "+bot1.getOr()+"\n");
    }
    private void distributionRoles(){

        bot1.choisirRole(roles);
    }


}
