package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;
import java.util.Random;

public class BotAleatoire extends Bot{

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot


    @Override
    public ArrayList<Quartier> faireActionDeBase(){
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
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
                //quartierChoisi=quartier1;

            }
            else if (intAleatoire2==1){
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                choixDeBase.add(quartier2);
                //quartierChoisi=quartier2;

            }
        } else if (intAleatoire == 1){
            changerOr(2);
        }

        // construire un quartier parmis ceux qu'il peux construire
        Quartier quartierConstruire= construireQuartierAleatoire();
        if (quartierConstruire!=null){
            choixDeBase.add(quartierConstruire);
        }

        return choixDeBase;
    }

    @Override
    public void choisirRole(Role[] roles){
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(1); //Pour l'instant y'a qu'un role
        setRole(roles[intAleatoire]);
    }

    /**
     * Construi un quartier aléatoire parmis ceux qu'il peut construire
     */
    public Quartier construireQuartierAleatoire(){
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr&&!quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(quartiersPossible.size()>0){
            Random aleatoire= new Random();
            int intAleatoire= aleatoire.nextInt(quartiersPossible.size()); //Pour l'instant y'a qu'un role
            Quartier quartierConstruire=quartiersPossible.get(intAleatoire);
            ajoutQuartierConstruit(quartierConstruire);
            return quartierConstruire;
        }
        return null;
    }

}
