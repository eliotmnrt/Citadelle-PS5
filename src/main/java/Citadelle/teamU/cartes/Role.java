package Citadelle.teamU.cartes;
import Citadelle.teamU.moteurjeu.bots.Bot;

public class Role {
    protected int nbQuartierConstructible=1;
    protected int ordre = 0;
    public Role(){

    }
    public int getNbQuartierConstructible() {
        return nbQuartierConstructible;
    }
    public void actionSpecial(Bot bot){
        //Les actions dépendent du role
    }
    public int getOrdre() {
        return ordre;
    }
}
