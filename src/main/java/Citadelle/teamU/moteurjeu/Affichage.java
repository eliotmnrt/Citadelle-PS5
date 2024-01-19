package Citadelle.teamU.moteurjeu;


import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;

public class Affichage {
    // classe de gestion de tout les prints
    private Bot bot;
    private ArrayList<Bot> botList;
    public Affichage(Bot bot){
        this.bot=bot;
    }
    public Affichage(ArrayList<Bot> botList){
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

    private void afficheCouronne() {
        if(bot.isCouronne()){
            System.out.println("Ce bot a la couronne");
        }
    }


    public void afficheChoixDeBase(ArrayList<Quartier> choix){
        if(choix.get(0) == null){
            System.out.println(bot.toString()+" a pris 2 pièces d'or");

        }
        else if(choix.get(0) != null && choix.size() == 3) {
            System.out.println(bot.toString() + " a pioché les quartiers " + choix.get(0) + " et " + choix.get(1));
            System.out.println(bot.toString() + " a choisi le quartier " + choix.get(2));
        }
        else {
            System.out.println(choix);
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

    public void afficheActionSpecialeMagicienAvecPioche(ArrayList<Quartier> cartesEchangees){
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

    public void afficheActionSpecialeArchitecte(ArrayList<Quartier> quartiersSupp) {
        System.out.println(bot.toString() + " a pioché 2 quartiers supplémentaires grâce à son role d'architecte : " + quartiersSupp.get(0).toString() + " et " + quartiersSupp.get(1).toString());
    }

    public void afficheActionSpecialeOrCondottiere(int nbOr) {
        System.out.println("Le " + bot.toString() + " a gagné " + nbOr + " or(s) grâce à sa capacité de marchand");
    }
    public void afficheActionSpecialeDestructionCondottiere(Bot botVise, Quartier quartierDetruit) {
        System.out.println("Le " + bot.toString() + " a detruit le " + quartierDetruit + " du " + botVise);
    }

    public void afficheLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        Bot botVainqueur=botList.get(0); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        System.out.println();
        for(Bot bot: botList){
            System.out.println(bot+" a un score de : "+bot.getScore());
            if (bot.getScore()>max){
             max= bot.getScore();
             botVainqueur=bot;
            }
        }

        System.out.println("\n\nLe vainqueur de la partie est "+botVainqueur.toString()+" avec un score de "+max+" points");
    }
}
