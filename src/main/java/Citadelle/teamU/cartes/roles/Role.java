package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.moteurjeu.bots.Bot;


public interface Role {
    
    //Les actions dépendent du role
    void actionSpeciale(Bot bot);
    String toString();
    int getOrdre();
}
