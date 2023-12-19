package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Architecte implements Role {

    private final int ordre = 7;
    private ArrayList<Bot> botListe;
    private int nbQuartierConstructible;



    public Architecte(ArrayList<Bot> botListe){
        this.botListe = botListe;
        this.nbQuartierConstructible = 3;
    }
    public void piocheDeuxCartes(Bot bot){
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
    }
    public void actionSpeciale(Bot bot){
        piocheDeuxCartes(bot);
        bot.construire();
        bot.construire();
    }

    @Override
    public int getOrdre() {
        return ordre;
    }


    @Override
    public String toString() {
        return "Architecte";
    }

    @Override
    public String actionToString(Bot bot) {
        return "print de l'archi a faire";
    }
}
