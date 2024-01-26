package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.List;

public class Voleur implements Role{

    private List<Bot> botListe;
    private final int ordre = 2;
    private String choix = "";
    private List<Role> roles;


    public Voleur(List<Bot> botListe, List<Role> roles){
        this.botListe = botListe;
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void voler(Bot botVoleur, Role roleVole){
        System.out.println(roleVole.toString());
        choix = roleVole.toString();
        for (Bot bot:botListe){
            if(bot.getRole().toString().equals(roleVole.toString())){
                System.out.println(bot.getOr());
                choix += "(" + bot.toString() + " " + bot.getOr() + " ors)";
                botVoleur.setOrProchainTour(bot.getOr());
                bot.voleDOrParVoleur();
                return;
            }
        }
        choix += " (personne)";
    }

    public void actionSpeciale(Bot bot){
        bot.actionSpecialeVoleur(this);
    }

    @Override
    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Voleur";
    }
}
