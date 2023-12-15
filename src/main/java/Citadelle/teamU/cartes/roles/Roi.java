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
    public void orQuartierJaune(Bot bot){
        int comp = 0;
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.JAUNE)){
                bot.changerOr(1);
                comp++;
            }
        }
        orGagneCapacite = comp;
    }
    public void actionSpeciale(Bot bot){
        orQuartierJaune(bot);
    }

    @Override
    public String toString() {
        return "Roi";
    }

    public String actionToString(Bot bot){
        return "Le " + bot.toString() + " a gagné " + orGagneCapacite + " or(s) grâce à sa capacité";
    }
    public int getOrdre(){return ordre;}
}
