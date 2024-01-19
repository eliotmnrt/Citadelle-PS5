package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.AffichageJoueur;
import Citadelle.teamU.moteurjeu.Pioche;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

public abstract class Bot {
    protected int nbOr;

    protected Role role;
    protected Pioche pioche;
    protected boolean couronne;
    protected ArrayList<Quartier> quartierConstruit;
    protected ArrayList<Quartier> quartierMain;
    protected int orProchainTour = -1; //or vole par le voleur que l'on recupere au prochain tour
    protected SecureRandom random;
    protected AffichageJoueur affichageJoueur;
    protected int score; // represente les points de victoire
    protected int orVole = -1;      //sert pour afficher l'or que l'on a volé / s'est fait volé
    protected int ordreChoixRole;

    public Bot(Pioche pioche){
        this.pioche = pioche;
        nbOr = 2;
        quartierConstruit = new ArrayList<>();
        quartierMain = new ArrayList<>();
        score = 0;
        random = new SecureRandom();
        initQuartierMain();
    }

    public int getOr(){
        return nbOr;
    }

    public int getOrVole() { return orVole; }

    public void setOrVole(int orVole) { this.orVole = orVole; }

    /**
     * @param or a ajouter (positif) ou à soustraire (négatif)
     * Ajoute ou soustrait x nombre d'or
     */
    public void changerOr(int or){
        nbOr = nbOr+or;
    }
    public void voleDOrParVoleur(){
        orVole = nbOr;
        nbOr = 0;
    }
    public AffichageJoueur getAffichage(){return affichageJoueur;}

    public Pioche getPioche() {return pioche;}

    public void setOrProchainTour(int orProchainTour) {this.orProchainTour = orProchainTour;}

    public Role getRole() {
        return role;
    }
    public void setRole(Role role){
        this.role = role;
    }
    public int getOrdre(){
        return role.getOrdre();
    }
    public int randInt(int nb){return random.nextInt(nb);}

    public int getOrProchainTour(){return orProchainTour;}  //utile pour les tests uniquemement

    public void ajoutQuartierConstruit(Quartier newQuartier){
        // verifier si les quartiers à construire sont dans la main, que le bot a assez d'or et qu'il a pas déjà construit un quartier avec le même nom
        if(quartierMain.contains(newQuartier) && nbOr >= newQuartier.getCout() && !quartierConstruit.contains(newQuartier)) {
            quartierConstruit.add(newQuartier);
            quartierMain.remove(newQuartier);
            changerOr(-newQuartier.getCout());
            score+= newQuartier.getCout();
        } else {
            throw new IllegalArgumentException();
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

    public ArrayList<Quartier> choisirEntreDeuxQuartiersViaCout(int nb){   //pour eviter de dupliquer du code
        // si nb est positif on garde la carte la plus chère sinon la moins chère
        Quartier quartier1 = pioche.piocherQuartier();
        Quartier quartier2 = pioche.piocherQuartier();
        ArrayList<Quartier> choixDeBase = new ArrayList<>();
        choixDeBase.add(quartier1);
        choixDeBase.add(quartier2);
        if ((nb > 0 && quartier1.getCout() < quartier2.getCout()) || (nb < 0 && quartier1.getCout() > quartier2.getCout())){
            Collections.reverse(choixDeBase);
        }
        ajoutQuartierMain(choixDeBase.get(0));
        pioche.remettreDansPioche(choixDeBase.get(1));
        choixDeBase.add(choixDeBase.get(0));
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
    public boolean isCouronne() {
        return couronne;
    }
    public void setCouronne(boolean couronne) {
        this.couronne = couronne;
    }
    public void setOrdreChoixRole(int ordreChoixRole) {
        this.ordreChoixRole = ordreChoixRole;
    }

    public int getOrdreChoixRole() {
        return ordreChoixRole;
    }

    // à implementer dans chaque bot
    public abstract Quartier construire();
    public abstract ArrayList<Quartier> faireActionDeBase();
    public abstract void actionSpecialeMagicien(Magicien magicien);
    public abstract void actionSpecialeVoleur(Voleur voleur);
    public abstract void choisirRole(ArrayList<Role> roles);


     //
}
