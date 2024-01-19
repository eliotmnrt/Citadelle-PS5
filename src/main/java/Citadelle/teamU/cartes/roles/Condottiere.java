package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Condottiere implements Role{
    private ArrayList<Bot> botListe;
    private final int ordre = 8;
    private Quartier quartierAdetruire;
    private int orQuartierRouge = 0;
    private Bot botAdetruire;
    public Condottiere(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public void orQuartierRouge(Bot bot) {
        int cpt=0;
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.ROUGE)) {
                bot.changerOr(1);
                cpt++;
            }
        }
        orQuartierRouge=cpt;
    }

    public void destructionQuartier(Bot bot,Bot botAdetruire,Quartier quartier){
        this.quartierAdetruire=quartier;
        this.botAdetruire=botAdetruire;
        botAdetruire.getQuartiersConstruits().remove(quartier);
        bot.changerOr(-(quartierAdetruire.getCout() - 1)); //perd l'argent a cause de la detruction de quartier
    }
    @Override
    public void actionSpeciale(Bot bot) {
        orQuartierRouge(bot);
        bot.actionSpecialeCondottiere(this);


    }
    @Override
    public int getOrdre() {
        return ordre;
    }
    public ArrayList<Bot> getBotListe() {
        return botListe;
    }
    @Override
    public String toString(){
        return "Condottiere";
    }
    public Quartier getQuartierAdetruire(){
        return quartierAdetruire;
    }
    public Bot getBotAdetruire(){
        return botAdetruire;
    }
    /*@Override
    public String actionToString(Bot bot) {
        String str="";
        if(orQuartierRouge>0){
            str+="Le "+ bot.toString()+" a gagné "+orQuartierRouge+" pièces pour ses quartiers rouges\n";
        }
        if (quartierAdetruire != null) {
            str+="Le " + bot.toString() + " a detruit le quartier "+quartierAdetruire.toString()+" du bot "+botAdetruire.toString();
        }
        else {
            str += bot.toString() + " n'a rien detruit";
        }
        return str;
    }*/
}
