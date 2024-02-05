package Citadelle.teamU.cartes.roles;


import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.List;

public class Assassin implements Role {


    private List<Bot> botListe;
    private final int ordre = 1;
    private List<Role> roles = new ArrayList<>();

    public Assassin(List<Bot> botListe,List<Role> roles){
        this.botListe = botListe;
        this.roles=roles;
    }
    public List<Bot> getBotListe() {
        return botListe;
    }

    /**
     *  permet de tuer
     */
    public void tuer( Role roleTue){
        for(Bot bot: botListe){
            if (bot.getRole()==roleTue){
                bot.setMort(true);
            }
        }
    }


    public void actionSpeciale(Bot bot){
        bot.actionSpecialeAssassin(this);
    }


    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Assassin";
    }
    public List<Role> getRoles() {
        return roles;
    }

}
