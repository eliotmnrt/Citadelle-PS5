package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints


    public static void separation(){
        System.out.println("\n");
    }

    public static void afficheTour(int nbTour){
        System.out.println("Tour "+ nbTour);
    }
    public static void afficheBot(Bot bot){

        System.out.println("--------------"+bot.toString()+"------------------");
        System.out.println("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore());
        System.out.println("Main: "+bot.getQuartierMain());
        System.out.println("Quartier construits "+bot.getQuartiersConstruits());

    }
    public static void afficheChoixOr(Bot bot){
        System.out.println(bot.toString()+" a pris 2 pièces d'or");
    }
    public static void afficheQuartiersPioches(Bot bot,Quartier quartier1, Quartier quartier2){
        System.out.println(bot.toString()+" a pioché les quartiers "+quartier1+" et "+quartier2);
    }
    public static void afficheQuartierChoisi(Bot bot, Quartier quartierChoisi) {
        System.out.println(bot.toString()+" a choisis le quartier "+ quartierChoisi);
    }
    public static void afficheQuartierConstruit(Bot bot,Quartier... quartiersConstruit){
        for(Quartier quartier: quartiersConstruit){
            System.out.println(bot.toString()+" a construit: "+quartier);
        }

    }
    public static void afficheActionSpecialArchitecte(Bot bot, Quartier quartier1, Quartier quartier2){
        System.out.println(bot.toString() +" pioche deux quartiers supplémentaires "+quartier1+" et "+quartier2);
    }
    public static void afficheVainqueur(Bot bot, int max){
        System.out.println("Le vainqueur de la partie est "+bot.toString()+" avec un score de "+max+" points");
    }
    public static void afficheAvantageQuartierNoble(Bot bot, Quartier quartier){
        System.out.println(bot.toString()+ " obtient 1 pièce d'or supplémentaire pour son quartier Noble "+quartier);
    }
    public static void afficheAvantageQuartierMarchand(Bot bot, Quartier quartier){
        System.out.println(bot.toString()+ " obtient 1 pièce d'or supplémentaire pour son quartier Marchand "+quartier);
    }
    public static void afficheAvantageQuartierReligieux(Bot bot, Quartier quartier){
        System.out.println(bot.toString()+ " obtient 1 pièce d'or supplémentaire pour son quartier Religieux "+quartier);
    }
}
