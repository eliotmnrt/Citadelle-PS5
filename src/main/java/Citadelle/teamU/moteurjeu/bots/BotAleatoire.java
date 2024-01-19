package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;


public class BotAleatoire extends Bot{

    private static int numDuBotAleatoire = 1;
    private String name;

    public BotAleatoire(Pioche pioche){
        super(pioche);
        this.name = "BotAleatoire"+numDuBotAleatoire;
        this.affichage = new Affichage(this);
        numDuBotAleatoire++;
    }

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot


    @Override
    public ArrayList<Quartier> faireActionDeBase(){
        //une arrayList qui en 0 contient null si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        ArrayList<Quartier> choixDeBase=new ArrayList<>();
        //cree un nombre random soit 0 soit 1, selon le nombre aleatoire choisi, fait une action de base
        int intAleatoire= randInt(2);
        if(intAleatoire == 0){
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1 = pioche.piocherQuartier();
            Quartier quartier2 = pioche.piocherQuartier();
            choixDeBase.add(quartier1);
            choixDeBase.add(quartier2);

            int intAleatoire2 = randInt(2); // Choisi un int aléatoire 0 ou 1
            if (intAleatoire2 == 0) {
                ajoutQuartierMain(quartier1);
                pioche.remettreDansPioche(quartier2);
                choixDeBase.add(quartier1);
            } else {
                ajoutQuartierMain(quartier2);
                pioche.remettreDansPioche(quartier1);
                choixDeBase.add(quartier2);
            }
        } else {
            choixDeBase.add(null);
            changerOr(2);
        }
        affichage.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }


    @Override
    public void choisirRole(ArrayList<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;         //on recupere l'or du vol
        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
    }

    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        int rang = randInt(6)+ 1  ;     // pour un nb aleatoire hors assassin et condottiere prsq on il y est pas dans ma branche
        assassin.tuer(assassin.getRoles().get(rang));
    }

    /**
     * Construit un quartier aléatoire parmis ceux qu'il peut construire
     */
    @Override
    public Quartier construire(){
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr  &&  !quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(!quartiersPossible.isEmpty()){
            int intAleatoire = randInt(quartiersPossible.size());
            Quartier quartierConstruire = quartiersPossible.get(intAleatoire);
            ajoutQuartierConstruit(quartierConstruire);
            affichage.afficheConstruction(quartierConstruire);
            return quartierConstruire;
        }
        return null;
    }

    @Override
    public String toString(){
        return name;
    }


    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int aleat = randInt(magicien.getBotListe().size() + 1);        // tire un chiffre aleatoire pour 4 bots et la pioche
        while (aleat == magicien.getBotListe().indexOf(this)){          //on l'empêche d'échanger avec lui meme
            aleat = randInt(magicien.getBotListe().size() + 1);
        }
        if(aleat < magicien.getBotListe().size()){                      // aleatoire correspondant à un bot
            affichage.afficheActionSpecialeMagicienAvecBot(magicien.getBotListe().get(aleat));
            magicien.changeAvecBot(this, magicien.getBotListe().get(aleat));
            affichage.afficheNouvelleMainMagicien();
        } else {                                                        //aleatoire correspondant à la pioche
            affichage.afficheActionSpecialeMagicienAvecPioche(this.getQuartierMain());
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichage.afficheNouvelleMainMagicien();
        }
    }

    // UPDATE QUAND AJOUT DE CLASSES
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        int rang = randInt(6) + 2;       // pour un nb aleatoire hors assassin et voleur
        affichage.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
        voleur.voler(this, voleur.getRoles().get(rang) );
    }



    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {
        ArrayList<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        int indiceRandomBot = randInt(botList.size());
        Bot botADetruire = (botList.get(indiceRandomBot));
        if(!botADetruire.getQuartiersConstruits().isEmpty()) {
            int indiceRandomQuartier = randInt(botADetruire.getQuartiersConstruits().size() );
            Quartier quartierAdetruire = botADetruire.getQuartiersConstruits().get(indiceRandomQuartier);

            if(quartierAdetruire!=null){
                if (this.getOr() >= quartierAdetruire.getCout() - 1) {
                    condottiere.destructionQuartier(this, botADetruire, quartierAdetruire);
                }
            }
        }
    }
}
