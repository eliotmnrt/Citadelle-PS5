package Citadelle.teamU.cartes;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.Objects;

public class Marchand extends Role {
    public Marchand() {
        super.ordre = 6;
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
    public String toString() {
        return "Marchand";
    }
}
   