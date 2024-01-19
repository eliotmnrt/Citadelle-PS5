package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Pretre implements Role {

    private final int ordre = 5;
    private ArrayList<Bot> botliste;
    private int orGagneCapacite = 0;
    public Pretre(ArrayList<Bot> botliste){
        this.botliste = botliste;
    }
    public void OrQuartierBleue(Bot bot){
        int comp = 0;
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.BLEUE) || quartier.equals(Quartier.ECOLE_DE_MAGIE)){
                bot.changerOr(1);
                comp++;
            }
        }
        orGagneCapacite = comp;
        bot.getAffichage().afficheActionSpecialePretre(orGagneCapacite);
    }
    public void actionSpeciale(Bot bot){
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
