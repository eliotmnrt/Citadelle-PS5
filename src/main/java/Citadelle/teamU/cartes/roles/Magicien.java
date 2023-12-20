package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Magicien implements Role {

    // on part du principe que si on choisit le magicien, on VEUT changer ses cartes

    private ArrayList<Bot> botListe;
    private final int ordre = 3;

    private String choix;

    public Magicien(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public ArrayList<Bot> getBotListe() {
        return botListe;
    }

    public void echangeDeCartes(Bot bot){
        int nbQuartierMain = bot.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: botListe){  //on regarde qui a le plus de cartes dans sa main
            if(botAdverse.getQuartierMain().size() > nbQuartierMain){
                botAvecQuiEchanger = botAdverse;
                nbQuartierMain = botAvecQuiEchanger.getQuartierMain().size();
            }
        }
        if(botAvecQuiEchanger != null){ // si un bot a plus de cartes que nous, on échange avec lui
            changeAvecBot(bot, botAvecQuiEchanger);

        } else {    // sinon on échange avec la pioche
            changeAvecPioche(bot);
        }
    }

    /**
     * permet au bot d'echanger sa main avec la pioche
     * @param bot Bot
     */

    public void changeAvecPioche(Bot bot){
        choix = "la pioche";
        int nbQuartierMain = bot.getQuartierMain().size();
        for(int i=0; i<nbQuartierMain; i++){
            Pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        }
        for(int i=0; i<nbQuartierMain; i++){
            bot.ajoutQuartierMain(Pioche.piocherQuartier());
        }
    }

    /**
     * permet d'echanger les mains de 2 bots
     * @param bot Bot a l'initiative de l'échange
     * @param botEchange bot qui subit l'échange
     */
    public void changeAvecBot(Bot bot, Bot botEchange){
        choix = "le " + botEchange.toString();
        ArrayList<Quartier> tmpList = new ArrayList<>(bot.getQuartierMain());
        bot.getQuartierMain().clear();
        bot.getQuartierMain().addAll(botEchange.getQuartierMain());
        botEchange.getQuartierMain().clear();
        botEchange.getQuartierMain().addAll(tmpList);
    }


    public void actionSpeciale(Bot bot){
        bot.actionSpecialeMagicien(this);
    }

    @Override
    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Magicien";
    }

    public String actionToString(Bot bot){
        return "Le " + bot.toString() +" a échangé ses carte avec " + choix + ".\nMain actuelle : " + bot.getQuartierMain();
    }


}
