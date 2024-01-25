package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Roi implements Role {

    private ArrayList<Bot> botListe;
    private int orGagneCapacite = 0;
    private final int ordre = 4;

    public Roi(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }

    public void distributionCouronne(Bot notreBot) {
        System.out.println("Le "+notreBot+" prend la couronne");
        for(Bot bot : this.botListe){
            bot.setCouronne(false);
        }
        notreBot.setCouronne(true);
        // notreBot.getAffichage().afficheCouronne();
    }

    public void orQuartierJaune(Bot bot){
        //On s'occupe de la couronne
        distributionCouronne(bot);

        //Les quartiers jaune :
        int comp = 0;
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.JAUNE)){
                bot.changerOr(1);
                comp++;
            }
        }
        orGagneCapacite = comp;
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
