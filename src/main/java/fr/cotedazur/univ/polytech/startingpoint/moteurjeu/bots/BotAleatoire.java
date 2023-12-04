package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BotAleatoire extends Bot{

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot


    @Override
    public void faireActionDeBase(){
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(2);
        if(intAleatoire == 0){
            ajoutQuartierMain(role.piocheQuartier());
        } else if (intAleatoire == 1){
            setOr(getOr() + 2);
        }
    }

    @Override
    public void choisirRole(Role[] roles){
        setRole(roles[0]);
    }
}
