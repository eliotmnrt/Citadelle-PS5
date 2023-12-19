package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public interface Role {

    int ordre = 0;

    //Les actions dépendent du role
    void actionSpecial(Bot bot);

    int getOrdre();
}
