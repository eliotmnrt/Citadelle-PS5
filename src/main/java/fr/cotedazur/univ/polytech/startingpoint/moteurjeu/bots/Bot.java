package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Role;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Pioche;

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

    /**
     * @param or a ajouter (positif) ou à soustraire (négatif)
     * Ajoute ou soustrait x nombre d'or
     */
    public void changerOr(int or){
        nbOr = nbOr+or;
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
        // verifier si les quartiers à construire sont dans la main, que le bot a assez d'or et qu'il a pas déjà construit un quartier avec le même nom
        if(quartierMain.contains(newQuartier)&&nbOr >= newQuartier.getCout()&& !quartierConstruit.contains(newQuartier)) {
            quartierConstruit.add(newQuartier);
            quartierMain.remove(newQuartier);
            changerOr(-newQuartier.getCout());
        }
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
