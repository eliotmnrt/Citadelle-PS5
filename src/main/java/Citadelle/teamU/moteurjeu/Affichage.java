package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.List;

@SuppressWarnings("java:S106")
public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot;
    private List<Bot> botList;
    public Affichage(Bot bot){
        this.bot=bot;
    }
    public Affichage(List<Bot> botList){
        this.botList=botList;
    }
    public void afficheBot(){
        System.out.println("\n\n--------------"+bot.toString()+"------------------");
        if (bot.getOrProchainTour() >= 0){
            afficheGainVoleur(bot.getOrProchainTour());
        }
        if (bot.getOrVole() >= 0){
            afficheVolDOr(bot.getOrVole());
        }
        System.out.println("Role: "+bot.getRole()+"; or: "+bot.getOr()+"; score: "+bot.getScore());
        System.out.println("Main: "+bot.getQuartierMain());
        System.out.println("Quartiers construits "+bot.getQuartiersConstruits());
    }

    public void afficheCouronne() {
        if(bot.isCouronne()){
            System.out.println("Ce bot a la couronne");
        } else {
            throw new IllegalArgumentException();
        }
    }


    public void afficheChoixDeBase(List<Quartier> choix){
        if(choix.get(0) == null){
            System.out.println(bot.toString()+" a pris 2 pièces d'or");
        }
        else if(choix.get(0) != null && choix.size() == 4 && choix.get(2) == null) {
            System.out.println(bot.toString() + " a pioché les quartiers " + choix.get(0) + " et " + choix.get(1));
            System.out.println(bot.toString() + " a choisi le quartier " + choix.get(3));
        } else if (choix.get(0) != null && choix.size() == 4) {
            System.out.println(bot.toString() + " a pioché 3 quartiers " + choix.get(0) + ", " + choix.get(1) + " et " + choix.get(2) + " grâce à sa carte Observatoire");
            System.out.println(bot.toString() + " a choisi le quartier " + choix.get(3));
        } else if (choix.get(0) != null && choix.size() == 6 && choix.get(2) == null) {
            System.out.println(bot.toString() + " a pioché les quartiers " + choix.get(0) + " et " + choix.get(1));
            System.out.println(bot.toString() + " a gardé les 2 quartiers " + choix.get(3) + " et " + choix.get(4) + " grâce à sa carte Bibliothèque");
        } else if (choix.get(0) != null && choix.size() == 6 && choix.get(2) != null) {
            System.out.println(bot.toString() + " a pioché 3 quartiers " + choix.get(0) + ", " + choix.get(1) + " et " + choix.get(2) + " grâce à sa carte Observatoire");
            System.out.println(bot.toString() + " a choisi les 2 quartiers " + choix.get(3) + ", " + choix.get(4) + " et " + choix.get(5) + " grâce à sa carte Bibliothèque");
        } else {
            System.out.println("erreur :" + choix);
            throw new IllegalArgumentException(); // a l'aide
        }
    }

    public void afficheConstruction(Quartier construction){
        if (construction != null){
            System.out.println(bot.toString() + " a construit " + construction);
        }
    }

    public void afficheActionSpecialeMagicienAvecBot(Bot echange){
        System.out.println( "Le " + bot.toString() +" a échangé ses carte avec " + echange.toString() );
    }

    public void afficheNouvelleMainMagicien(){
        System.out.println("Main actuelle : " + bot.getQuartierMain());
    }

    public void afficheActionSpecialeMagicienAvecPioche(List<Quartier> cartesEchangees){
        System.out.println("Le " + bot.toString() +" a échangé ses cartes: " + cartesEchangees + ", avec la pioche");
    }

    public void afficheActionSpecialeVoleur(Role role){
        System.out.println("Le " + bot.toString() +" a volé le " + role.toString());
    }

    public void afficheVolDOr(int nbOrVole){
        System.out.println("Le " + bot.toString() +" s'est fait voler " + nbOrVole + " or(s) par le voleur");
        bot.setOrVole(-1);
    }
    public void afficheGainVoleur(int orGagne){
        System.out.println("Le " + bot.toString() +" a gagné " + orGagne + " or(s) grâce à son role de Voleur au tour précédent");
        bot.setOrProchainTour(-1);
    }

    public void afficheActionSpecialeRoi(int or){
        System.out.println("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de roi");
    }

    public void afficheActionSpecialePretre(int or) {
        System.out.println("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de prêtre");
    }

    public void afficheActionSpecialeMarchand(int or) {
        System.out.println("Le " + bot.toString() + " a gagné " + or + " or(s) grâce à sa capacité de marchand");
    }

    public void afficheActionSpecialeArchitecte(List<Quartier> quartiersSupp) {
        System.out.println(bot.toString() + " a pioché 2 quartiers supplémentaires grâce à son role d'architecte : " + quartiersSupp.get(0).toString() + " et " + quartiersSupp.get(1).toString());
    }

    public void afficheActionSpecialeOrCondottiere(int nbOr) {
        System.out.println("Le " + bot.toString() + " a gagné " + nbOr + " or(s) grâce à sa capacité de condottière");
    }
    public void afficheActionSpecialeDestructionCondottiere(Bot botVise, Quartier quartierDetruit) {
        System.out.println("Le " + bot.toString() + " a detruit " + quartierDetruit + " du " + botVise);
    }
    public void afficheQuartierManufacture(List<Quartier> nvxQuartiers){
        System.out.println("Le " + bot.toString() + " a pioché " + nvxQuartiers + "en échange de 3ors, grâce à sa carte Manufacture");
    }

    public void afficheQuartierLaboratoire(Quartier quartier){
        System.out.println("Le " + bot.toString() + " a défaussé " + quartier + " grâce à sa carte Laboratoire pour gagner 1or");
    }

    public void afficheQuartierCimetiere(Quartier quartier){
        System.out.println("Le " + bot.toString() + " a recupéré " + quartier + " grâce à sa carte Cimetiere contre 1or");
    }

    public void afficheLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        Bot botVainqueur=botList.get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        System.out.println();
        for(Bot bot1: botList){
            System.out.println(bot1+" a un score de : "+bot1.getScore());
            if (bot1.getScore()>max){
             max= bot1.getScore();
             botVainqueur=bot1;
            }
        }

        System.out.println("\n\nLe vainqueur de la partie est "+botVainqueur.toString()+" avec un score de "+max+" points");
    }

    public void afficheBonusPremier() {
        System.out.println("\n\n"+bot+" gagne 4 points car il a fini avec "+bot.getQuartiersConstruits().size()+" quartiers en premier");
    }

    public void afficheBonusQuartier() {
        System.out.println(bot+" gagne 2 points car il a fini avec "+bot.getQuartiersConstruits().size()+" quartiers");
    }

    public void afficheBonusCouleur() {
        System.out.println(bot + " gagne 3 points car il a un quartier de chaque couleur");
    }

    public void afficheBonusQuartierViolet(Quartier quartier) {
        System.out.println(bot+" gagne 2 points en plus car "+quartier+" vaut 8 points au lieu de 6");
    }

    public void afficheBonusCouleurAvecQV() {
        System.out.println(bot+" gagne 3 points car il a un quartier de chaque couleur (la cour des miracles change de couleur)");
    }
}
