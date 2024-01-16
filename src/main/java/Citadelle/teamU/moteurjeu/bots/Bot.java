package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Random;

public class Bot {
    protected int nbOr;

    protected Role role;
    protected Pioche pioche;
    protected ArrayList<Quartier> quartierConstruit;
    protected ArrayList<Quartier> quartierMain;
    protected int orProchainTour; //or vole par le voleur que l'on recupere au prochain tour

    public void setScore(int score) {
        this.score = score;
    }

    protected int score; // represente les points de victoire
    public Bot(Pioche pioche){
        this.pioche = pioche;
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
    public void voleDOrParVoleur(){nbOr = 0;}

    public Pioche getPioche() {return pioche;}

    public void setOrProchainTour(int orProchainTour) {this.orProchainTour = orProchainTour;}

    public Role getRole() {
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public void choisirRole(ArrayList<Role> roles){
        setRole(roles.get(0));
    }
    public int getOrdre(){
        return role.getOrdre();
    }
    public int randInt(int nb){return new Random().nextInt(nb);}

    public int getOrProchainTour(){return orProchainTour;}  //utile pour les tests uniquemement

    public void ajoutQuartierConstruit(Quartier newQuartier){
        // verifier si les quartiers à construire sont dans la main, que le bot a assez d'or et qu'il a pas déjà construit un quartier avec le même nom
        if(quartierMain.contains(newQuartier)&&nbOr >= newQuartier.getCout()&& !quartierConstruit.contains(newQuartier)) {
            quartierConstruit.add(newQuartier);
            quartierMain.remove(newQuartier);
            changerOr(-newQuartier.getCout());
            score+= newQuartier.getCout();
        }
    }


    public void ajoutQuartierMain(Quartier newQuartier){
        quartierMain.add(newQuartier);
    }
    public void initQuartierMain(){ //une partie commence avec 4  quartier pour chaque joueur
        for(int i=0; i<4;i++){
            ajoutQuartierMain(pioche.piocherQuartier());
        }
    }
    public ArrayList<Quartier> getQuartierMain(){ return quartierMain;}
    public ArrayList<Quartier> getQuartiersConstruits(){
        return this.quartierConstruit;
    }
    public void setQuartiersConstruits(ArrayList<Quartier> quartierConstruit){
        this.quartierConstruit= quartierConstruit;
    }
    public ArrayList<Quartier> faireActionDeBase(){
        // return le quartier choisi si le bot a choisi de piocher un quartier
        // si le bot a choisi de prendre des pieces ça return null
        ArrayList<Quartier> choixDeBase = new ArrayList<>();
        return choixDeBase;
    }
    public int getScore(){
        return this.score;
    }




    /**
     * Fait les actions qui sont différentes en fonction de chaque roles
     */
    public void faireActionSpecialRole(){
        role.actionSpeciale(this);
    }
    public Quartier construire(){
        return null;
    }
    public void actionSpecialeMagicien(Magicien magicien){}
    public void actionSpecialeVoleur(Voleur voleur){}
    public void actionSpecialeCondottiere(Condottiere condottiere){}
}
