package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Tour;
import Citadelle.teamU.moteurjeu.bots.Bot;
import java.util.ArrayList;
import java.util.Random;
import java.util.Objects;

public class Condottière {
    public void orQuartierRouge(Bot bot) {
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.ROUGE)) {
                bot.changerOr(1);
            }
        }
    }

    public void quartierDestruction(Bot bot, Tour tour) {
        ArrayList<Bot> botListe = tour.getBotListe();
        ArrayList<Quartier> QuartiersTour = new ArrayList<>();
        for (Bot bots : botListe) {
            for (Quartier quartier : bots.getQuartiersConstruits()) {
                QuartiersTour.add(quartier);
            }
        }
        Random random = new Random();
        int indiceRandom = random.nextInt(QuartiersTour.size());
        while (bot.getOr() < QuartiersTour.get(indiceRandom).getCout() - 1) {
            indiceRandom = random.nextInt(QuartiersTour.size());
        }
        ArrayList<Bot> BotsAvecCequartier = new ArrayList<>();
        for (Bot bots : botListe) {
            for (Quartier quartier : bots.getQuartiersConstruits()) {
                if (quartier == QuartiersTour.get(indiceRandom)) {
                    BotsAvecCequartier.add(bots);
                }
            }
        }
        int indiceBot = random.nextInt(BotsAvecCequartier.size());
        Bot botChoisi=BotsAvecCequartier.get(indiceBot);
        ArrayList<Quartier> QuartierduBot=(botChoisi.getQuartiersConstruits());
        ArrayList<Quartier> nouveauxQuartiers= new ArrayList<>();
        for(Quartier quartier: QuartierduBot){
            if(quartier!=QuartiersTour.get(indiceRandom)){
                nouveauxQuartiers.add(quartier);
            }
        }
        botChoisi.setQuartierConstruit(nouveauxQuartiers);
    }
}