package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Marchand implements Role {

    private final int ordre = 6;
    private int orGagneCapacite = 0;
    ArrayList<Bot> botliste;
    public Marchand(ArrayList<Bot> botListe) {
        this.botliste = botListe;
    }

    public void OrQuartierVert(Bot bot) {
        int comp =0;
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.VERT)) {
                bot.changerOr(1);
                comp++;
            }
        }
        orGagneCapacite = comp;
    }

    public void actionSpeciale(Bot bot) {
        bot.changerOr(1);
        OrQuartierVert(bot);
    }

    @Override
    public int getOrdre() {
        return ordre;
    }


    @Override
    public String toString() {
        return "Marchand";
    }

    @Override
    public String actionToString(Bot bot) {
        return "Le " + bot.toString() + " a gagné " + (orGagneCapacite+1) + " or(s) grâce à sa capacité de marchand";
    }
}
   