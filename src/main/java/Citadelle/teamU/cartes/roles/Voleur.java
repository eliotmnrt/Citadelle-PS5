package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.bots.Bot;
import java.util.List;

public class Voleur implements Role{

    private List<Bot> botListe;
    private final int ordre = 2;
    private List<Role> roles;


    public Voleur(List<Bot> botListe, List<Role> roles){
        this.botListe = botListe;
        this.roles = roles;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void voler(Bot botVoleur, Role roleVole){
        for (Bot bot:botListe){
            if(bot.getRole().toString().equals(roleVole.toString())){
                botVoleur.setOrProchainTour(bot.getOr());
                bot.voleDOrParVoleur();
                return;
            }
        }
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
