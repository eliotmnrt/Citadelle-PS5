package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.ArrayList;
import java.util.List;

public class Architecte implements Role {

    private final List<Bot> botListe;
    private final List<Quartier> quartiersSupplementaires;



    public Architecte(List<Bot> botListe){
        this.botListe = botListe;
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
        return 7;
    }

    @Override
    public List<Bot> getBotliste() {
        return botListe;
    }


    @Override
    public String toString() {
        return "Architecte";
    }
}
