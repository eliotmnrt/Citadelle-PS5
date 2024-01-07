package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Assassin {


    private ArrayList<Bot> botListe;
    private final int ordre = 1;



    public Assassin(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public ArrayList<Bot> getBotListe() {
        return botListe;
    }

    /**
     *  permet de tuer
     */
    public void tuer(Role role){
        for(Bot bot: botListe){
            if (bot.getRole()==role){
                botListe.remove(bot);
            }
        }
    }


    public void actionSpeciale(Bot bot){

    }


    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Assassin";
    }




}
