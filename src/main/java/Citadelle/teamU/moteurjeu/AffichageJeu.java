package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class AffichageJeu {
    private Tour tour;

    public AffichageJeu(Tour tour){
        this.tour = tour;
    }

    public void affichageNbTour(){
        System.out.println("Tour "+ tour.getNbTour());
    }

    public void affichageOrdre(ArrayList<Bot> ordre){
        System.out.println("\n\n\nOrdre dans lequel les bots choissent leurs role : " + ordre);
    }

    public Bot afficheLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        Bot botVainqueur =tour.getBotListe().get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        for(Bot bot1: tour.getBotListe()){
            if (bot1.getScore()>max){
                max= bot1.getScore();
                botVainqueur=bot1;
            }
        }

        System.out.println("\n\nTour " + tour.getNbTour() +": Le vainqueur de la partie est " + botVainqueur.toString() + " avec un score de " + max + " points");
        return botVainqueur;
    }

}
