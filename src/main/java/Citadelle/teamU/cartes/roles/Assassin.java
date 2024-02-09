package Citadelle.teamU.cartes.roles;


import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.List;

public class Assassin implements Role {


    private final List<Bot> botListe;
    private final List<Role> roles;

    public Assassin(List<Bot> botListe,List<Role> roles){
        this.botListe = botListe;
        this.roles=roles;
    }

    /**
     *  permet de tuer
     */
    public void tuer(Role roleTue){
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
        return 1;
    }

    @Override
    public List<Bot> getBotliste() {
        return botListe;
    }

    @Override
    public String toString() {
            return "Assassin";
    }
    public List<Role> getRoles() {
        return roles;
    }

}
