package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

@SuppressWarnings("java:S106")

public class AffichageJoueur {
    private final static Logger LOGGER = Logger.getLogger(AffichageJoueur.class.getName());
    private Bot bot;
    public AffichageJoueur(Bot bot){
        this.bot=bot;
        LOGGER.setLevel(Level.OFF);
        //LOGGER.getParent().setLevel(Level.ALL);
        //LOGGER.getParent().getHandlers()[0].setLevel(Level.ALL);
        for(Handler handler : LOGGER.getParent().getHandlers()){
            handler.setFormatter(new CustomFormatter());
        }
    }
    public void afficheBot(){
        LOGGER.info("\n\n--------------"+bot.toString()+"------------------");
        if (bot.getOrProchainTour() >= 0){
            afficheGainVoleur(bot.getOrProchainTour());
        }
        if (bot.getOrVole() >= 0){
            afficheVolDOr(bot.getOrVole());
        }
        LOGGER.info("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore());
        LOGGER.info("Main: "+bot.getQuartierMain());
        LOGGER.info("Quartiers construits "+bot.getQuartiersConstruits());
    }

    public void afficheCouronne() {
        if(bot.isCouronne()){
            LOGGER.info("Ce bot a la couronne");
        } else {
            throw new IllegalArgumentException();
        }
    }


    public void afficheChoixDeBase(List<Quartier> choix){
        if(choix.get(0) == null){
            LOGGER.info(bot.toString()+" a pris 2 pièces d'or");
        }
        else if(choix.get(0) != null && choix.size() == 4 && choix.get(2) == null) {
            LOGGER.info(bot.toString() + " a pioché les quartiers " + choix.get(0) + " et " + choix.get(1));
            LOGGER.info(bot.toString() + " a choisi le quartier " + choix.get(3));
        } else if (choix.get(0) != null && choix.size() == 4) {
            LOGGER.info(bot.toString() + " a pioché 3 quartiers " + choix.get(0) + ", " + choix.get(1) + " et " + choix.get(2) + " grâce à sa carte Observatoire");
            LOGGER.info(bot.toString() + " a choisi le quartier " + choix.get(3));
        } else if (choix.get(0) != null && choix.size() == 6 && choix.get(2) == null) {
            LOGGER.info(bot.toString() + " a pioché les quartiers " + choix.get(0) + " et " + choix.get(1));
            LOGGER.info(bot.toString() + " a gardé les 2 quartiers " + choix.get(3) + " et " + choix.get(4) + " grâce à sa carte Bibliothèque");
        } else if (choix.get(0) != null && choix.size() == 6 && choix.get(2) != null) {
            LOGGER.info(bot.toString() + " a pioché 3 quartiers " + choix.get(0) + ", " + choix.get(1) + " et " + choix.get(2) + " grâce à sa carte Observatoire");
            LOGGER.info(bot.toString() + " a choisi les 2 quartiers " + choix.get(3) + ", " + choix.get(4) + " et " + choix.get(5) + " grâce à sa carte Bibliothèque");
        } else {
            LOGGER.info("erreur :" + choix);
            throw new IllegalArgumentException(); // a l'aide
        }
    }

    public void afficheConstruction(Quartier construction){
        if (construction != null){
            LOGGER.info(bot.toString() + " a construit " + construction);
        }
    }

    public void afficheActionSpecialeMagicienAvecBot(Bot echange){
        LOGGER.info( "Le " + bot.toString() +" a échangé ses carte avec " + echange.toString() );
    }

    public void afficheNouvelleMainMagicien(){
        LOGGER.info("Main actuelle : " + bot.getQuartierMain());
    }

    public void afficheActionSpecialeMagicienAvecPioche(List<Quartier> cartesEchangees){
        LOGGER.info("Le " + bot.toString() +" a échangé ses cartes: " + cartesEchangees + ", avec la pioche");
    }

    public void afficheActionSpecialeVoleur(Role role){
        LOGGER.info("Le " + bot.toString() +" a volé le " + role.toString());
    }

    public void afficheVolDOr(int nbOrVole){
        LOGGER.info("Le " + bot.toString() +" s'est fait voler " + nbOrVole + " or(s) par le voleur");
        bot.setOrVole(-1);
    }
    public void afficheGainVoleur(int orGagne){
        LOGGER.info("Le " + bot.toString() +" a gagné " + orGagne + " or(s) grâce à son role de Voleur au tour précédent");
        bot.setOrProchainTour(-1);
    }

    public void afficheActionSpecialeRoi(int or){
        LOGGER.info("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de roi");
    }

    public void afficheActionSpecialePretre(int or) {
        LOGGER.info("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de prêtre");
    }

    public void afficheActionSpecialeMarchand(int or) {
        LOGGER.info("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de marchand");
    }

    public void afficheActionSpecialeArchitecte(List<Quartier> quartiersSupp) {
        LOGGER.info(bot.toString() + " a pioché 2 quartiers supplémentaires grâce à son role d'architecte : " + quartiersSupp.get(0).toString() + " et " + quartiersSupp.get(1).toString());
    }

    public void afficheActionSpecialeOrCondottiere(int nbOr) {
        LOGGER.info("Le " + bot.toString() + " a gagné " + nbOr + " or(s) grâce à sa capacité de condottière");
    }
    public void afficheActionSpecialeDestructionCondottiere(Bot botVise, Quartier quartierDetruit) {
        LOGGER.info("Le " + bot.toString() + " a detruit " + quartierDetruit + " du " + botVise);
    }
    public void afficheQuartierManufacture(List<Quartier> nvxQuartiers){
        LOGGER.info("Le " + bot.toString() + " a pioché " + nvxQuartiers + "en échange de 3ors, grâce à sa carte Manufacture");
    }

    public void afficheQuartierLaboratoire(Quartier quartier){
        LOGGER.info("Le " + bot.toString() + " a défaussé " + quartier + " grâce à sa carte Laboratoire pour gagner 1or");
    }

    public void afficheQuartierCimetiere(Quartier quartier){
        LOGGER.info("Le " + bot.toString() + " a recupéré " + quartier + " grâce à sa carte Cimetiere contre 1or");
    }

    public void afficheBonusPremier() {
        LOGGER.info("\n\n"+bot+" gagne 4 points car il a fini avec "+bot.getQuartiersConstruits().size()+" quartiers en premier");
    }

    public void afficheBonusQuartier() {
        LOGGER.info(bot+" gagne 2 points car il a fini avec "+bot.getQuartiersConstruits().size()+" quartiers");
    }

    public void afficheBonusCouleur() {
        LOGGER.info(bot + " gagne 3 points car il a un quartier de chaque couleur");
    }

    public void afficheBonusQuartierViolet(Quartier quartier) {
        LOGGER.info(bot+" gagne 2 points en plus car "+quartier+" vaut 8 points au lieu de 6");
    }

    public void afficheBonusCouleurAvecQV() {
        LOGGER.info(bot+" gagne 3 points car il a un quartier de chaque couleur (la cour des miracles change de couleur)");
    }

    public void afficheMeurtre(Role role) {
        LOGGER.info("Ce bot tue le role : " + role);
    }

    public void afficheMort(Bot mort) {
        LOGGER.info("\n------- Le "+mort+" est mort car il était "+mort.getRole()+", il n'a pas joué pendant ce tour -------\n");
    }

    public void affichePrendreCouronne() {
        LOGGER.info("Le "+bot+" prend la couronne");
    }
}
