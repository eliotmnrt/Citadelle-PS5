package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.List;
import java.util.Objects;

public class Pretre implements Role {

    private final List<Bot> botliste;

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
        return 5;
    }

    @Override
    public List<Bot> getBotliste() {
        return botliste;
    }


    @Override
    public String toString() {
        return "Pretre";
    }
}
