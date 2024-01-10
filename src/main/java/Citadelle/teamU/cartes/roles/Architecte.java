package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Architecte implements Role {

    private final int ordre = 7;
    private ArrayList<Bot> botListe;
    private int nbQuartierConstructible;
    private ArrayList<Quartier> quartierSupplementaire;



    public Architecte(ArrayList<Bot> botListe){
        this.botListe = botListe;
        this.nbQuartierConstructible = 3;
        quartierSupplementaire = new ArrayList<>();
    }
    public void piocheDeuxCartes(Bot bot){
        quartierSupplementaire.clear();
        Quartier quartier1 = bot.getPioche().piocherQuartier();
        Quartier quartier2 = bot.getPioche().piocherQuartier();
        bot.ajoutQuartierMain(quartier1);
        bot.ajoutQuartierMain(quartier2);
        quartierSupplementaire.add(quartier1);
        quartierSupplementaire.add(quartier2);
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
        return bot.toString() + " a pioché 2 quartiers supplémentaires grâce à son role d'architecte : " + quartierSupplementaire.get(0).toString() + " et " + quartierSupplementaire.get(1).toString();
    }
}
