package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Voleur implements Role{

    private ArrayList<Bot> botListe;
    private final int ordre = 2;
    private String choix = "";
    ArrayList<Role> roles = new ArrayList<>();


    public Voleur(ArrayList<Bot> botListe, ArrayList<Role> roles){
        this.botListe = botListe;
        this.roles = roles;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void voler(Bot botVoleur, Role roleVole){
        choix = roleVole.toString();
        for (Bot bot:botListe){
            if(bot.getRole().toString().equals(roleVole.toString())){
                botVoleur.changerOr(bot.getOr());
                bot.voleDOrParVoleur();
                return;
            }
        }
        choix += " + rien";
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

    public String actionToString(Bot bot){
        return "Le " + bot.toString() +" a volé le " + choix;
    }


}
