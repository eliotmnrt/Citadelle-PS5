package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Condottiere implements Role{
    private final List<Bot> botListe;


    public Condottiere(List<Bot> botListe){
        this.botListe = botListe;
    }

    public void orQuartierRouge(Bot bot) {
        int orQuartierRouge = 0;
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.ROUGE) || quartier.equals(Quartier.ECOLE_DE_MAGIE)) {
                bot.changerOr(1);
                orQuartierRouge++;
            }
        }
        bot.getAffichage().afficheActionSpecialeOrCondottiere(orQuartierRouge);
    }

    public void destructionQuartier(Bot bot,Bot botAdetruire,Quartier quartier){
        Quartier quartierAdetruire;
        quartierAdetruire = quartier;
        if(botAdetruire.getQuartiersConstruits().size()<8&&!(botAdetruire.getRole() instanceof Pretre)){
            botAdetruire.getQuartiersConstruits().remove(quartier);
            bot.changerOr(-(quartierAdetruire.getCout() - 1)); //perd l'argent a cause de la destruction de quartier
            if (bot.getOr() < 0){
                throw new IllegalArgumentException();
            }
            botAdetruire.setScore(botAdetruire.getScore() - quartier.getCout());
            bot.getAffichage().afficheActionSpecialeDestructionCondottiere(botAdetruire, quartierAdetruire);
        }else{
            throw new IllegalArgumentException("le bot visé n'est pas compatible");
        }

        List<Bot> autresBots = new ArrayList<>(botListe);
        autresBots.remove(bot);            //copie de la liste pour proposer le quartier à celui qui a le cimetiere
        for (Bot botCimetiere: autresBots){
            for (Quartier quartier2: botCimetiere.getQuartiersConstruits()){
                if (quartier2.equals(Quartier.CIMETIERE)){
                    botCimetiere.quartierCimetiere(quartier);
                    return;
                }
            }
        }
    }
    @Override
    public void actionSpeciale(Bot bot) {
        orQuartierRouge(bot);
        bot.actionSpecialeCondottiere(this);
    }
    @Override
    public int getOrdre() {
        return 8;
    }

    @Override
    public List<Bot> getBotliste() {
        return botListe;
    }

    @Override
    public String toString(){
        return "Condottiere";
    }
}
