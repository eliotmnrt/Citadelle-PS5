package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot;
    private ArrayList<Quartier> choixDeBase;

    public Affichage(Bot bot, ArrayList<Quartier> choixDeBase){
        this.bot=bot;

        this.choixDeBase=choixDeBase;
    }
    public void afficheBot(){

        System.out.println("--------------"+bot.toString()+"------------------");
        System.out.println("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore());
        System.out.println("Main: "+bot.getQuartierMain());
        System.out.println("Quartier construits "+bot.getQuartiersConstruits());

    }
    public void afficheChoixDeBase(){
        if(choixDeBase.size()<2){
            System.out.println(bot.toString()+" a pris 2 pièces d'or");
            if (choixDeBase.size()==1){
                System.out.println(bot.toString()+" a construit "+choixDeBase.get(0));
            }
        }
        else{
            System.out.println(bot.toString()+" a pioché les quartiers "+choixDeBase.get(0)+" et "+choixDeBase.get(1));
            System.out.println(bot.toString()+" a choisis le quartier "+choixDeBase.get(2));
            if (choixDeBase.size()==4){
                System.out.println(bot.toString()+" a construit "+choixDeBase.get(3));
            }


        }
        System.out.println("\n");
    }
}
