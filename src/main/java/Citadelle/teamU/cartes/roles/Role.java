package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.List;


public interface Role {
    
    //Les actions dépendent du role
    void actionSpeciale(Bot bot);
    String toString();
    int getOrdre();
    List<Bot> getBotliste();
}
