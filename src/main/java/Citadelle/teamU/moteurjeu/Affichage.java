package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot;
    private ArrayList<Quartier> choixDeBase;
    private Quartier construction;
    private ArrayList<Bot> botList;

    public Affichage(Bot bot){
        this.bot=bot;
    }
    public Affichage(ArrayList<Bot> botList){
        this.botList=botList;
    }
    public void afficheBot(){

        System.out.println("--------------"+bot.toString()+"------------------");
        System.out.println("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore()+"position du bot : "+bot.getOrdreChoixRole());
        afficheCouronne();
        System.out.println("Main: "+bot.getQuartierMain());
        System.out.println("Quartiers construits "+bot.getQuartiersConstruits());
    }

    private void afficheCouronne() {
        if(bot.isCouronne()){
            System.out.println("Ce bot a la couronne");
        }
    }

    public void setChoixDeBase(ArrayList<Quartier> choixDeBase) {
        this.choixDeBase = choixDeBase;
        afficheChoixDeBase(choixDeBase);
    }

    public void setConstruction(Quartier construction) {
        this.construction = construction;
        afficheConstruction(construction);
    }

    public void afficheChoixDeBase(ArrayList<Quartier> choix){
        if(choix.get(0) == null){
            System.out.println(bot.toString()+" a pris 2 pièces d'or");

        }
        else if(choix.get(0) != null && choix.size() == 3) {
            System.out.println(bot.toString() + " a pioché les quartiers " + choixDeBase.get(0) + " et " + choixDeBase.get(1));
            System.out.println(bot.toString() + " a choisi le quartier " + choixDeBase.get(2));
        }
        else {
            System.out.println(choix);
            throw new IllegalArgumentException(); // a l'aide
        }
    }

    public void afficheConstruction(Quartier construction){
        if (construction != null){
            System.out.println(bot.toString() + " a construit " + construction);
        }
        System.out.println("\n");
    }


    public void afficheActionSpeciale(Bot bot){
        System.out.println(bot.getRole().actionToString(bot));
    }

    public void afficheLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        Bot botVainqueur=botList.get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        for(Bot bot1: botList){
            if (bot1.getScore()>max){
             max= bot1.getScore();
             botVainqueur=bot1;
            }
        }

        System.out.println("Le vainqueur de la partie est "+botVainqueur.toString()+" avec un score de "+max+" points");
    }
}
