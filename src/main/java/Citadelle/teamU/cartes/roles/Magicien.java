package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Magicien implements Role {

    // on part du principe que si on choisit le magicien, on VEUT changer ses cartes

    private ArrayList<Bot> botListe;

    public Magicien(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public void echangeDeCartes(Bot bot){
        int nbQuartierMain = bot.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: botListe){
            if(botAdverse.getQuartierMain().size() > nbQuartierMain){
                botAvecQuiEchanger = botAdverse;
            }
        }
        if(botAvecQuiEchanger != null){
            System.out.println("echange avec " + botAvecQuiEchanger.toString());
            System.out.println("nouvelle main: "+bot.getQuartierMain());
            changeAvecBot(bot, botAvecQuiEchanger);

        } else {
            System.out.println("echange avec pioche");
            System.out.println("nouvelle main: "+bot.getQuartierMain());
            changeAvecPioche(bot);

        }
    }

    private void changeAvecPioche(Bot bot){
        int nbQuartierMain = bot.getQuartierMain().size();
        for(int i=0; i<nbQuartierMain; i++){
            Pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        }
        for(int i=0; i<nbQuartierMain; i++){
            bot.ajoutQuartierMain(Pioche.piocherQuartier());
        }
    }

    private void changeAvecBot(Bot bot, Bot botEchange){
        ArrayList<Quartier> tmpList = new ArrayList<>(bot.getQuartierMain());
        bot.getQuartierMain().clear();
        bot.getQuartierMain().addAll(botEchange.getQuartierMain());
        botEchange.getQuartierMain().clear();
        botEchange.getQuartierMain().addAll(tmpList);
    }


    public void actionSpecial(Bot bot){
        echangeDeCartes(bot);
    }

    @Override
    public String toString() {
        return "Magicien";
    }

}
