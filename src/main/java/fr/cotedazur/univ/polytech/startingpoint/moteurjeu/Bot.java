package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;

import java.util.ArrayList;

public class Bot {
    int nbOr;
    Role role;
    ArrayList<Quartier> quartierConstruit;
    ArrayList<Quartier> quartierMain;
    public Bot(){
        nbOr = 0;
        quartierConstruit = new ArrayList<Quartier>();
        quartierMain = new ArrayList<Quartier>();
    }
    public int getOr(){
        return nbOr;
    }
    public void setOr(int or){
        nbOr = or;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void ajoutQuartierConstruit(Quartier newQuartier){
        quartierConstruit.add(newQuartier);
    }
    public void ajoutQuartierMain(Quartier newQuartier){
        quartierMain.add(newQuartier);
    }
    public void setRole(Role[] roles){
        setRole(roles[0]);
    }
}
