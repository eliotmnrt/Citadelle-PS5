package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.roles.Role;
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
    public ArrayList<Quartier> faireActionDeBase(){
        //une arrayList qui en 0 contient null si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        //en indice 3 le quartier construit si un quartier a été construit
        ArrayList<Quartier> choixDeBase=new ArrayList<>();
        Random aleatoire= new Random();
        //cree un nombre random soit 0 soit 1, selon le nombre aleatoire choisi, fait une action de base
        int intAleatoire= aleatoire.nextInt(2);
        //Quartier quartierChoisi=null;
        if(intAleatoire == 0){
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1= Pioche.piocherQuartier();
            Quartier quartier2=Pioche.piocherQuartier();
            choixDeBase.add(quartier1);
            choixDeBase.add(quartier2);

            int intAleatoire2= aleatoire.nextInt(2); // Choisi un int aléatoire 0 ou 1
            if (intAleatoire2 ==0){
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);
                choixDeBase.add(quartier1);
            }
            else{
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                choixDeBase.add(quartier2);
            }
        } else if (intAleatoire == 1){
            choixDeBase.add(null);
            changerOr(2);
        }
        choixDeBase.add(construire());
        return choixDeBase;
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
    public Quartier construire(){
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr  &&  !quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(quartiersPossible.size()>0){
            Random aleatoire= new Random();
            int intAleatoire= aleatoire.nextInt(quartiersPossible.size());
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
}
