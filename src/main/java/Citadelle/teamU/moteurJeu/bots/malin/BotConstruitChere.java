package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.Pioche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotConstruitChere extends BotMalin {
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;

    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.name="BotConstruitChere"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }





    public void setRolesVisible(List<Role> rolesVisible) {
        this.rolesVisible = rolesVisible;
    }

}
