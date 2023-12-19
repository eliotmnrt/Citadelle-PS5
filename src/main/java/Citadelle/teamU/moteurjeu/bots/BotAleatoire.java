package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;
import java.util.Random;

public class BotAleatoire extends Bot{

    private static int numDuBotAleatoire = 1;
    private String name;

    public BotAleatoire(){
        super();
        this.name = "BotAleatoire"+numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot


    @Override
    public void faireActionDeBase(){
        Random aleatoire= new Random();
        //cree un nombre random soit 0 soit 1, selon le nombre aleatoire choisi, fait une action de base
        int intAleatoire= aleatoire.nextInt(2);

        if(intAleatoire == 0){
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1= Pioche.piocherQuartier();
            Quartier quartier2=Pioche.piocherQuartier();
            Affichage.afficheQuartiersPioches(this,quartier1,quartier2);
            int intAleatoire2= aleatoire.nextInt(2); // Choisi un int aléatoire 0 ou 1
            if (intAleatoire2 ==0){
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);
                Affichage.afficheQuartierChoisi(this,quartier1);

            }
            else{
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                Affichage.afficheQuartierChoisi(this,quartier2);


            }
        } else if (intAleatoire == 1){
            changerOr(2);
            Affichage.afficheChoixOr(this);
        }
    }

    @Override
    public void choisirRole(ArrayList<Role> roles){
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(roles.size());
        setRole(roles.remove(intAleatoire));
    }

    /**
     * Construi un quartier aléatoire parmis ceux qu'il peut construire
     */
    @Override
    public void construire(){
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr&&!quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        //a suffle
        if(quartiersPossible.size()>0){
            Random aleatoire= new Random();
            int nbContruit= aleatoire.nextInt(2);
            if(nbContruit==1){
                Quartier quartierConstruit = quartiersPossible.get(0);
                ajoutQuartierConstruit(quartierConstruit);
                Affichage.afficheQuartierConstruit(this,quartierConstruit);

            }
        }

    }

    @Override
    public String toString(){

        return name;
    }

}
