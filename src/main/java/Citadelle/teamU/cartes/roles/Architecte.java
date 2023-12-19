package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Architecte implements Role {

    private int ordre;
    private ArrayList<Bot> botListe;
    private int nbQuartierConstructible;



    public Architecte(ArrayList<Bot> botListe){
        this.botListe = botListe;
        this.ordre = 7;
        this.nbQuartierConstructible = 3;
    }
    public void piocheDeuxCartes(Bot bot){
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
    }
    public void actionSpecial(Bot bot){
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
}
