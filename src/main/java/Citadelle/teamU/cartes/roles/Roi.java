package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Roi implements Role {


    private ArrayList<Bot> botListe;
    public Roi(ArrayList<Bot> botListe){
        this.botListe = botListe;
    }
    public void orQuartierJaune(Bot bot){
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.JAUNE)){
                bot.changerOr(1);
            }
        }
    }
    public void actionSpecial(Bot bot){
        orQuartierJaune(bot);
    }

    @Override
    public String toString() {
        return "Roi";
    }
}
