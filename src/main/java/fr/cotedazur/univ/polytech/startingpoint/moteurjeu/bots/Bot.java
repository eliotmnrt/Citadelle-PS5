package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;

import java.util.ArrayList;

public class Bot {
    protected int nbOr;
    protected Role role;
    protected ArrayList<Quartier> quartierConstruit;
    protected ArrayList<Quartier> quartierMain;
    public Bot(){
        nbOr = 2;
        quartierConstruit = new ArrayList<>();
        quartierMain = new ArrayList<>();
    }
    public int getOr(){
        return nbOr;
    }
    public void setOr(int or){
        nbOr = or;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void choisirRole(Role[] roles){
        setRole(roles[0]);
    }
    public void ajoutQuartierConstruit(Quartier newQuartier){
        quartierConstruit.add(newQuartier);
    }
    public void ajoutQuartierMain(Quartier newQuartier){
        quartierMain.add(newQuartier);
    }
    public ArrayList<Quartier> getQuartierMain(){ return quartierMain;}
    public ArrayList<Quartier> getQuartiersConstruits(){
        return this.quartierConstruit;
    }
    public void faireActionDeBase(){

    }

    /**
     * Fait les actions qui sont différentes en fonction de chaque roles
     */
    public void faireActionSpecialRole(){
        role.actionSpecial(this);
    }
}
