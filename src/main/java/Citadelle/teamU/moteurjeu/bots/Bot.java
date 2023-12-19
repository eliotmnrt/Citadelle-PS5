package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;

public class Bot {
    protected int nbOr;
    protected Role role;
    protected ArrayList<Quartier> quartierConstruit;
    protected ArrayList<Quartier> quartierMain;

    protected int score; // represente les points de victoire
    public Bot(){
        nbOr = 2;
        quartierConstruit = new ArrayList<>();
        quartierMain = new ArrayList<>();
        score=0;
        initQuartierMain();


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
    public void choisirRole(ArrayList<Role> roles){
        setRole(roles.get(0));
    }
    public void ajoutQuartierConstruit(Quartier newQuartier){
        // verifier si les quartiers à construire sont dans la main, que le bot a assez d'or et qu'il a pas déjà construit un quartier avec le même nom
        if(quartierMain.contains(newQuartier)&&nbOr >= newQuartier.getCout()&& !quartierConstruit.contains(newQuartier)) {
            quartierConstruit.add(newQuartier);
            quartierMain.remove(newQuartier);
            changerOr(-newQuartier.getCout());
            score+= newQuartier.getCout();
        }
    }
    public int getOrdre(){
        return role.getOrdre();
    }


    public void ajoutQuartierMain(Quartier newQuartier){
        quartierMain.add(newQuartier);
    }
    public void initQuartierMain(){ //une partie commence avec 4  quartier pour chaque joueur
        for(int i=0; i<4;i++){
            ajoutQuartierMain(Pioche.piocherQuartier());
        }
    }
    public ArrayList<Quartier> getQuartierMain(){ return quartierMain;}
    public ArrayList<Quartier> getQuartiersConstruits(){
        return this.quartierConstruit;
    }
    public void faireActionDeBase(){
        //effectue un choix entre prendre 2 pieces et piocher un quartuer
    }
    public int getScore(){
        return this.score;
    }

    /**
     * Fait les actions qui sont différentes en fonction de chaque roles
     */
    public void faireActionSpecialRole(){
        role.actionSpecial(this);
    }
    public void construire(){

    }
}
