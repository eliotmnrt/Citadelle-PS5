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

    /**
     *  permet au bot d'echanger sa main avec la pioche
     * @param bot
     * @param carteEchangees quartiers à échanger
     */
    public void changeAvecPioche(Bot bot, ArrayList<Quartier> carteEchangees){
        choix = "la pioche";
        int nbQuartierEchanges = carteEchangees.size() - 1;
        for (int i=nbQuartierEchanges; i>0; i--){
            int rang = bot.getQuartierMain().indexOf(carteEchangees.get(i));
            Pioche.remettreDansPioche(bot.getQuartierMain().remove(rang));
        }
        for(int i=0; i<nbQuartierEchanges; i++){
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
