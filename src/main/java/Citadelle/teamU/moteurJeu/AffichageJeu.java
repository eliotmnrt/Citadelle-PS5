package Citadelle.teamU.moteurJeu;

import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("java:S106")
public class AffichageJeu {
    private Tour tour;
    private static final Logger LOGGER = Logger.getLogger(AffichageJeu.class.getName());

    public AffichageJeu(Tour tour){
        this.tour = tour;
        //LOGGER.setLevel(Level.OFF);
    }

    public void affichageNbTour(){
        LOGGER.info("Tour "+ tour.getNbTour());
    }

    public void affichageOrdre(List<Bot> ordre){
        LOGGER.info("\n\n\nOrdre dans lequel les bots choissent leurs role : " + ordre);
    }

    public void afficheLeVainqueur(Bot botVainqueur){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        if (botVainqueur != null){
            LOGGER.info("\n\nTour " + tour.getNbTour() +": Le vainqueur de la partie est " + botVainqueur.toString() + " avec un score de " + botVainqueur.getScore() + " points");
        }else{
            LOGGER.info("\n\nTour " + tour.getNbTour() +"Egalité, deux bot on fini avec autant de point");
        }
    }

    public void afficheCartesVisible(List<Role> rolesVisible) {
        LOGGER.info("Les rôles face visible sont : "+rolesVisible.get(0)+" et "+rolesVisible.get(1));
    }
}
