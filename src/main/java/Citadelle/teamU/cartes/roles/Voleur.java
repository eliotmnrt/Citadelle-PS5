package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurJeu.bots.Bot;

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
        choix = roleVole.toString();
        for (Bot bot:botListe){
            if(bot.getRole().toString().equals(roleVole.toString())){
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
    public List<Bot> getBotliste() {
        return botListe;
    }

    @Override
    public String toString() {
        return "Voleur";
    }
}
