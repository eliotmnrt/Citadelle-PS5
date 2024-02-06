package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("java:S106")
public class AffichageJeu {
    private Tour tour;
    private final static Logger LOGGER = Logger.getLogger(AffichageJeu.class.getName());

    public AffichageJeu(Tour tour){
        this.tour = tour;
        LOGGER.setLevel(Level.OFF);
    }

    public void affichageNbTour(){
        LOGGER.info("Tour "+ tour.getNbTour());
    }

    public void affichageOrdre(List<Bot> ordre){
        LOGGER.info("\n\n\nOrdre dans lequel les bots choissent leurs role : " + ordre);
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

        LOGGER.info("\n\nTour " + tour.getNbTour() +": Le vainqueur de la partie est " + botVainqueur.toString() + " avec un score de " + max + " points");
        return botVainqueur;
    }

    public void afficheCartesVisible(Role role1, Role role2) {
        LOGGER.info("Les rôles face visible sont : "+role1+" et "+role2);
    }
}
