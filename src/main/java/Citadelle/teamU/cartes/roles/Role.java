package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public interface Role {

    int ordre = 0;

    //Les actions dépendent du role
    void actionSpeciale(Bot bot);
    String toString();
    String actionToString(Bot bot);
    int getOrdre();
}
