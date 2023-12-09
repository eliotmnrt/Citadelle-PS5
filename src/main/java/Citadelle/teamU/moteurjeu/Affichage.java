package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot1;
    private ArrayList<Quartier> choixDeBase;
    private int nbTour;
    public Affichage(Bot bot, int nbTour, ArrayList<Quartier> choixDeBase){
        this.bot1=bot;
        this.nbTour=nbTour;
        this.choixDeBase=choixDeBase;
    }
    public void afficheBot(){
        System.out.println("Tour "+nbTour);
        System.out.println("--------------Bot Aleatoire------------------");
        System.out.println("Role: "+bot1.getRole()+"; or: "+bot1.getOr()+"; score: "+bot1.getScore());
        System.out.println("Main: "+bot1.getQuartierMain());
        System.out.println("Quartier construits "+bot1.getQuartiersConstruits());
        //System.out.println("Nombre d'or du BOT aléatoire: ");
        //System.out.println("Points de victore du BOT aléatoire: "+bot1.getScore());
    }
    public void afficheChoixDeBase(){
        if(choixDeBase.size()<2){
            System.out.println("Bot aléatoire a pris 2 pièces d'or");
            if (choixDeBase.size()==1){
                System.out.println("Bot aléatoire a construit "+choixDeBase.get(0));
            }
        }
        else{
            System.out.println("Bot aléatoire a pioché les quartiers "+choixDeBase.get(0)+" et "+choixDeBase.get(1));
            System.out.println("Bot aléatoire a choisis le quartier "+choixDeBase.get(2));
            if (choixDeBase.size()==4){
                System.out.println("Bot aléatoire a construit "+choixDeBase.get(3));
            }


        }
        System.out.println("\n");
    }
}
