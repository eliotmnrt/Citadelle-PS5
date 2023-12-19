package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Pretre implements Role {

    private int ordre;
    private ArrayList<Bot> botliste;
    public Pretre(ArrayList<Bot> botliste){
        this.botliste = botliste;
        this.ordre=5;
    }
    public void OrQuartierBleue(Bot bot){
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.BLEUE)){
                bot.changerOr(1);
            }
        }
    }
    public void actionSpecial(Bot bot){
        OrQuartierBleue(bot);
    }

    @Override
    public int getOrdre() {
        return ordre;
    }

    @Override
    public String toString() {
        return "Prêtre";
    }


}
