package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.List;
import java.util.Objects;

public class Roi implements Role {

    private List<Bot> botListe;

    private final int ordre = 4;

    public Roi(List<Bot> botListe){
        this.botListe = botListe;
    }

    public void distributionCouronne(Bot notreBot) {
        notreBot.getAffichage().affichePrendreCouronne();
        for(Bot bot : this.botListe){
            bot.setCouronne(false);
        }
        notreBot.setCouronne(true);
        if (notreBot.isCouronne()){
            //notreBot.getAffichage().afficheCouronne();
        }
    }

    public void orQuartierJaune(Bot bot){
        int orGagneCapacite = 0;
        //On s'occupe de la couronne
        distributionCouronne(bot);

        //Les quartiers jaune :
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.JAUNE) || quartier.equals(Quartier.ECOLE_DE_MAGIE)){
                bot.changerOr(1);
                orGagneCapacite++;
            }
        }
        bot.getAffichage().afficheActionSpecialeRoi(orGagneCapacite);
    }
    public void actionSpeciale(Bot bot){
        orQuartierJaune(bot);
    }

    @Override
    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Roi";
    }
}
