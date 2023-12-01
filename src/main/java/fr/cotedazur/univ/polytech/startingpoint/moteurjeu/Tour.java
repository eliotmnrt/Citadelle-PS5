package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Roi;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;

import java.util.ArrayList;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private Bot bot;
    Role[] roles = {new Roi()};
    public Tour(Bot bot){
        this.bot = bot;
        distributionRoles();
    }
    private void distributionRoles(){
        bot.setRole(roles);
    }


}
