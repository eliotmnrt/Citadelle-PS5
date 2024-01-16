package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;
import java.util.Random;

public class BotAleatoire extends Bot{

    private static int numDuBotAleatoire = 1;
    private String name;

    public BotAleatoire(Pioche pioche){
        super(pioche);
        this.name = "BotAleatoire"+numDuBotAleatoire;
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
        //Quartier quartierChoisi=null;
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
        return choixDeBase;
    }

    @Override
    public void choisirRole(ArrayList<Role> roles){
        nbOr += orProchainTour;         //on recupere l'or du vol
        orProchainTour = 0;
        System.out.println(this.name + roles);
        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
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
        if (quartiersPossible.size() > 0) {
            int intAleatoire = randInt(quartiersPossible.size());
            Quartier quartierConstruire = quartiersPossible.get(intAleatoire);
            ajoutQuartierConstruit(quartierConstruire);
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
            magicien.changeAvecBot(this, magicien.getBotListe().get(aleat));
        } else {                                                        //aleatoire correspondant à la pioche
            magicien.changeAvecPioche(this, this.getQuartierMain());
        }
    }


    @Override           // UPDATE QUAND AJOUT DE CLASSES
    public void actionSpecialeVoleur(Voleur voleur){
        int rang = randInt(6) + 1;       // pour un nb aleatoire hors assassin et voleur
        voleur.voler(this, voleur.getRoles().get(rang));
    }

    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {
        Random random=new Random();
        ArrayList<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        int indiceRandomBot = random.nextInt(botList.size());
        Bot botAdetruire = (botList.get(indiceRandomBot));
        if(!botAdetruire.getQuartiersConstruits().isEmpty()) {
            int indiceRandomQuartier = randInt(botAdetruire.getQuartiersConstruits().size() );
            Quartier quartierAdetruire = botAdetruire.getQuartiersConstruits().get(indiceRandomQuartier);

            if(quartierAdetruire!=null){
                if (this.getOr() >= quartierAdetruire.getCout() - 1) {

                    condottiere.destructionQuartier(this, botAdetruire, quartierAdetruire);


                }
            }
        }
    }
}
