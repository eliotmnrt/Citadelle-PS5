package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Pioche;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BotAleatoire extends Bot{

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot


    @Override
    public void faireActionDeBase(){
        Random aleatoire= new Random();
        //cree un nombre random soit 0 soit 1, selon le nombre aleatoire choisi, fait une action de base
        int intAleatoire= aleatoire.nextInt(2);
        if(intAleatoire == 0){
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1=Pioche.piocherQuartier();
            Quartier quartier2=Pioche.piocherQuartier();

            int intAleatoire2= aleatoire.nextInt(2); // Choisi un int aléatoire 0 ou 1
            if (intAleatoire2 ==0){
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);

            }
            else if (intAleatoire2==1){
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);

            }
        } else if (intAleatoire == 1){
            changerOr(2);
        }

        // construire un quartier parmis ceux qu'il peux construire
        construireQuartierAleatoire();
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
    public void construireQuartierAleatoire(){
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr&&!quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(quartiersPossible.size()>0){
            Random aleatoire= new Random();
            int intAleatoire= aleatoire.nextInt(quartiersPossible.size()); //Pour l'instant y'a qu'un role
            ajoutQuartierConstruit(quartiersPossible.get(intAleatoire));
        }
    }

}
