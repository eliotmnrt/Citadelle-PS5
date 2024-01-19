package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Voleur implements Role{

    private ArrayList<Bot> botListe;
    private final int ordre = 2;
    private Role choix;
    private ArrayList<Role> roles = new ArrayList<>();


    public Voleur(ArrayList<Bot> botListe, ArrayList<Role> roles){
        this.botListe = botListe;
        this.roles = roles;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void voler(Bot botVoleur, Role roleVole){
        // a régler dans Affichage
        choix = roleVole;
        for (Bot bot:botListe){
            if(bot.getRole().toString().equals(roleVole.toString())){
                //choix += "(" + bot.toString() + " " + bot.getOr() + " ors)";
                botVoleur.setOrProchainTour(bot.getOr());
                bot.voleDOrParVoleur();
                return;
            }
        }
        //choix += " (personne)";
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
