package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot;
    private ArrayList<Quartier> choixDeBase;
    private ArrayList<Bot> botList;

    public Affichage(Bot bot, ArrayList<Quartier> choixDeBase){
        this.bot=bot;

        this.choixDeBase=choixDeBase;
    }
    public Affichage(ArrayList<Bot> botList){
        this.botList=botList;
    }
    public void afficheBot(){

        System.out.println("--------------"+bot.toString()+"------------------");
        System.out.println("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore());
        System.out.println("Main: "+bot.getQuartierMain());
        System.out.println("Quartier construits "+bot.getQuartiersConstruits());

    }
    public void afficheChoixDeBase(){
        if(choixDeBase.get(0)==null){
            System.out.println(bot.toString()+" a pris 2 pièces d'or");
            for(int i=1 ; i<choixDeBase.size() ; i++){
                System.out.println(bot.toString()+" a construit "+choixDeBase.get(i));
            }
        }
        else{

            System.out.println(bot.toString()+" a pioché les quartiers "+choixDeBase.get(0)+" et "+choixDeBase.get(1));
            System.out.println(bot.toString()+" a choisis le quartier "+choixDeBase.get(2));
            for(int i=3 ; i<choixDeBase.size() ; i++){
                System.out.println(bot.toString()+" a construit "+choixDeBase.get(i));
            }
        }
        System.out.println("\n");
    }
    
    public void afficheLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        Bot botVainqueur=botList.get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        for(Bot bot: botList){
            if (bot.getScore()>max){
             max= bot.getScore();
             botVainqueur=bot;
            }
        }

        System.out.println("Le vainqueur de la partie est "+botVainqueur.toString()+" avec un score de "+max+" points");
    }
}
