package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Objects;

public class Marchand implements Role {

    private int ordre;
    public Marchand(ArrayList<Bot> botListe) {
        this.ordre = 6;
    }

    public void OrQuartierVert(Bot bot) {
        for (Quartier quartier : bot.getQuartiersConstruits()) {
            if (Objects.equals(quartier.getCouleur(), TypeQuartier.VERT)) {
                bot.changerOr(1);
            }
        }
    }

    public void actionSpecial(Bot bot) {
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
}
   