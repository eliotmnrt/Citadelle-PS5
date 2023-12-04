package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Roi;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private Bot bot1;
    Role[] roles = {new Roi(bot1)};
    public <T extends Bot> Tour(T bot){
        this.bot1 = bot;
        distributionRoles();
        bot1.faireActionDeBase();
        System.out.println(bot1.getQuartierMain());
        System.out.println(bot1.getOr());
    }
    private void distributionRoles(){
        bot1.choisirRole(roles);
    }


}
