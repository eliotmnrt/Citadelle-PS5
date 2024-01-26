package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.List;
import java.util.Objects;

public class Pretre implements Role {

    private final int ordre = 5;
    private List<Bot> botliste;

    public Pretre(List<Bot> botliste){
        this.botliste = botliste;
    }
    public void orQuartierBleue(Bot bot){
        int orGagneCapacite = 0;
        for(Quartier quartier: bot.getQuartiersConstruits()){
            if(Objects.equals(quartier.getCouleur(), TypeQuartier.BLEUE) || quartier.equals(Quartier.ECOLE_DE_MAGIE)){
                bot.changerOr(1);
                orGagneCapacite++;
            }
        }
        bot.getAffichage().afficheActionSpecialePretre(orGagneCapacite);
    }
    public void actionSpeciale(Bot bot){
        orQuartierBleue(bot);
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
