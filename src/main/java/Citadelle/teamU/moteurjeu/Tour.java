package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.roles.*;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private ArrayList<Bot> botListe;
    private static int nbTour = 0;
    ArrayList<Role> roles = new ArrayList<>();

    ArrayList<Role> rolesTemp = new ArrayList<>();
    public Tour(ArrayList<Bot> botListe){
        roles.add(new Voleur(botListe, roles));
        roles.add(new Magicien(botListe));
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        this.botListe = botListe;
    }


    public void prochainTour(){
        rolesTemp = new ArrayList<>(roles);
        boolean dernierTour=false;
        nbTour++;
        distributionRoles();
        System.out.println("Tour "+ nbTour);
        System.out.println(botListe);
        Collections.sort(botListe, Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            Affichage affiche = new Affichage(bot);
            affiche.afficheBot();
            bot.faireActionSpecialRole();
            affiche.afficheActionSpeciale(bot);
            affiche.setChoixDeBase(bot.faireActionDeBase());
            affiche.afficheConstruction(bot.construire());
            if(bot.getQuartiersConstruits().size()==7) dernierTour=true;
        }
        if (dernierTour){
            Affichage affichageFin = new Affichage(botListe);
            affichageFin.afficheLeVainqueur();

        }

    }

    public ArrayList<Bot> distributionRoles(){
        ArrayList<Bot> listeDistribution = botListe;
        //On met celui avec la couronne devant, et après on met ceux dans le bonne ordre
        Collections.sort(listeDistribution, Comparator.comparingInt(Bot::getOrdreChoixRole)); //Ordonne en fonction de leur ordre dans la partie
        for(int i = 0 ; i<listeDistribution.size() ; i++){
            if(listeDistribution.get(i).isCouronne()){
                //celui qui a la couronne choisi son role en premier puis celui après lui.. etc
                for(int j= 0; j < i ; j++){
                    listeDistribution.add(listeDistribution.get(j));
                    listeDistribution.remove(j);
                }
                break;
            }
        }
        System.out.println("Ordre dans lequel les bots choissent leurs role : "+listeDistribution);
        for (Bot bot: listeDistribution){
            bot.choisirRole(rolesTemp);
        }
        return listeDistribution;
    }


}
