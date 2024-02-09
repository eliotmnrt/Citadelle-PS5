package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.List;
import java.util.Objects;

public class Marchand implements Role {


    private final List<Bot> botliste;
    public Marchand(List<Bot> botListe) {
        this.botliste = botListe;
    }

    public void orQuartierVert(Bot bot) {
        int orGagneCapacite = 0;
        int comp = 0;
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.VERT) || quartier.equals(Quartier.ECOLE_DE_MAGIE)) {
                bot.changerOr(1);
                comp++;
            }
        }
        orGagneCapacite = comp;
        bot.getAffichage().afficheActionSpecialeMarchand(orGagneCapacite + 1);
    }

    public void actionSpeciale(Bot bot) {
        bot.changerOr(1);
        orQuartierVert(bot);
    }

    @Override
    public int getOrdre() {
        return 6;
    }

    @Override
    public List<Bot> getBotliste() {
        return botliste;
    }

    @Override
    public String toString() {
        return "Marchand";
    }
}
   