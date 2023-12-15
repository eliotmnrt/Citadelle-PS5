package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.Objects;

public class Architecte extends Role{
    public Architecte(){
        super.ordre = 7;
        super.nbQuartierConstructible = 3;
    }
    public void piocheDeuxCartes(Bot bot){
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
    }
    public void actionSpecial(Bot bot){
        piocheDeuxCartes(bot);
        bot.construire();
        bot.construire();
    }
    @Override
    public String toString() {
        return "Architecte";
    }
}
