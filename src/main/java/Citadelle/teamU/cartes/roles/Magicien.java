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
    private ArrayList<Quartier> cartesAEchangees;

    public Magicien(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public ArrayList<Bot> getBotListe() {
        return botListe;
    }

    /**
     *  permet au bot d'echanger sa main avec la pioche
     * @param bot
     * @param carteEchangees quartiers à échanger
     */
    public void changeAvecPioche(Bot bot, ArrayList<Quartier> carteEchangees){
        choix = "la pioche";
        this.cartesAEchangees = new ArrayList<>(carteEchangees);
        ArrayList<Quartier> nouvelleMain = new ArrayList<>();
        for (Quartier quartier: bot.getQuartierMain()){
            if (!carteEchangees.contains(quartier)){        //on garde les cartes da la main non échangées
                nouvelleMain.add(quartier);
            }
        }
        for(Quartier quartier: carteEchangees){
            bot.getPioche().remettreDansPioche(quartier);
        }
        for(Quartier quartier: carteEchangees){
            nouvelleMain.add(bot.getPioche().piocherQuartier());
        }
        bot.setQuartierMain(nouvelleMain);
    }

    /**
     * permet d'echanger les mains de 2 bots
     * @param bot Bot a l'initiative de l'échange
     * @param botEchange bot qui subit l'échange
     */
    public void changeAvecBot(Bot bot, Bot botEchange){
        choix = "le " + botEchange.toString();
        cartesAEchangees = new ArrayList<>(bot.getQuartierMain());
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
        return "Le " + bot.toString() +" a échangé ses cartes :" + cartesAEchangees + " avec " + choix + ".\nMain actuelle : " + bot.getQuartierMain();
    }


}
