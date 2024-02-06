package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.List;

public class Magicien implements Role {

    // on part du principe que si on choisit le magicien, on VEUT changer ses cartes

    private List<Bot> botListe;
    private final int ordre = 3;


    public Magicien(List<Bot> botListe){
        this.botListe = botListe;
    }

    @Override
    public List<Bot> getBotliste() {
        return botListe;
    }

    /**
     *  permet au bot d'echanger sa main avec la pioche
     * @param bot Bot
     * @param carteEchangees quartiers à échanger
     */
    public void changeAvecPioche(Bot bot, List<Quartier> carteEchangees){
        int nbQuartierEchanges = carteEchangees.size() - 1;
        for (int i=nbQuartierEchanges; i>=0; i--){
            int rang = bot.getQuartierMain().indexOf(carteEchangees.get(i));
            bot.getPioche().remettreDansPioche(bot.getQuartierMain().remove(rang));
        }
        for(int i=0; i<=nbQuartierEchanges; i++){
            bot.ajoutQuartierMain(bot.getPioche().piocherQuartier());
        }
    }

    /**
     * permet d'echanger les mains de 2 bots
     * @param bot Bot a l'initiative de l'échange
     * @param botEchange bot qui subit l'échange
     */
    public void changeAvecBot(Bot bot, Bot botEchange){
        List<Quartier> tmpList = new ArrayList<>(bot.getQuartierMain());
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
}
