package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Architecte implements Role {

    private final int ordre = 7;
    private ArrayList<Bot> botListe;
    private int nbQuartierConstructible;
    private ArrayList<Quartier> quartiersSupplementaires;



    public Architecte(ArrayList<Bot> botListe){
        this.botListe = botListe;
        this.nbQuartierConstructible = 3;
        quartiersSupplementaires = new ArrayList<>();
    }
    public void piocheDeuxCartes(Bot bot){
        quartiersSupplementaires.clear();
        Quartier quartier1 = bot.getPioche().piocherQuartier();
        Quartier quartier2 = bot.getPioche().piocherQuartier();
        bot.ajoutQuartierMain(quartier1);
        bot.ajoutQuartierMain(quartier2);
        quartiersSupplementaires.add(quartier1);
        quartiersSupplementaires.add(quartier2);
    }
    public void actionSpeciale(Bot bot){
        piocheDeuxCartes(bot);
        bot.getAffichage().afficheActionSpecialeArchitecte(quartiersSupplementaires);
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
