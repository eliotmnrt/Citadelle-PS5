package Citadelle.teamU.moteurJeu.bots;

//modification pour test githubActions
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurJeu.AffichageJoueur;
import Citadelle.teamU.moteurJeu.Pioche;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public abstract class Bot {
    protected int nbOr;
    protected String name;
    protected boolean mort;
    protected Role role;
    protected Pioche pioche;
    protected boolean couronne;
    protected List<Quartier> quartiersConstruits;
    protected List<Quartier> quartierMain;
    protected int orProchainTour = -1; //or vole par le voleur que l'on recupere au prochain tour
    protected SecureRandom random;
    protected AffichageJoueur affichageJoueur;
    protected int score; // represente les points de victoire
    protected int orVole = -1;      //sert pour afficher l'or que l'on a volé / s'est fait volé
    protected int ordreChoixRole;
    protected int nbTour = 0;

    protected List<Role> rolesVisible;

    protected Bot(Pioche pioche){
        this.pioche = pioche;
        this.affichageJoueur = new AffichageJoueur(this);
        nbOr = 2;
        quartiersConstruits = new ArrayList<>();
        quartierMain = new ArrayList<>();
        score = 0;
        random = new SecureRandom();
        mort = false;
        initQuartierMain();
        this.affichageJoueur = new AffichageJoueur(this);
    }
    public boolean estMort() { //renvoit s'il est mort
        return mort;
    }
    public void setMort(boolean mort) {
        this.mort = mort;
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

    public int getOrProchainTour(){return orProchainTour;}

    @Override
    public String toString(){
        return name;
    }

    /**
     * permet de verifier l'ajout d'un quartier que l'on veut construire dans quartiersConstruits
     * @param newQuartier Quartier à construire
     */
    public void ajoutQuartierConstruit(Quartier newQuartier){
        // verifier si les quartiers à construire sont dans la main, que le bot a assez d'or et qu'il a pas déjà construit un quartier avec le même nom
        if(quartierMain.contains(newQuartier) && nbOr >= newQuartier.getCout() && !quartiersConstruits.contains(newQuartier)) {
            quartiersConstruits.add(newQuartier);
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

    /**
     * init les 4 quartiers de la main du bot au debut de la partie
     */
    public void initQuartierMain(){ //une partie commence avec 4  quartier pour chaque joueur
        for(int i=0; i<4;i++){
            ajoutQuartierMain(pioche.piocherQuartier());
        }
    }
    public List<Quartier> getQuartierMain(){ return quartierMain;}
    public List<Quartier> getQuartiersConstruits(){
        return this.quartiersConstruits;
    }

    public int getNbQuartiersConstruits(){ return quartiersConstruits.size(); }
    public int getScore(){
        return this.score;
    }


    public void setScore(int score) { this.score = score; }

    /**
     * Fait les actions qui sont différentes en fonction de chaque role
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

    public void setQuartiersConstruits(List<Quartier> quartiersConstruits) {
        this.quartiersConstruits = quartiersConstruits;
    }

    public void setQuartierMain(List<Quartier> quartierMain) {
        this.quartierMain = quartierMain;
    }

    /**
     * utilise certaines conditions pour déclencher des effets de cartes violettes
     */
    public void quartiersViolets(){
        if (quartiersConstruits.contains(Quartier.MANUFACTURE)){
            quartierManufacture();
        }
        if(quartiersConstruits.contains(Quartier.LABORATOIRE)){
            quartierLaboratoire();
        }
    }

    /**
     * si on a au moins 3ors et 1 carte ou moins, on paye pour des cartes avec la carte manufacture
     */
    public void quartierManufacture(){
        if (nbOr >= 3 && quartierMain.size() <= 1){
            changerOr(-3);
            List<Quartier> nvxQuartiers = new ArrayList<>();
            for (int i=0; i<3; i++){
                Quartier quartier = pioche.piocherQuartier();
                nvxQuartiers.add(quartier);
                ajoutQuartierMain(quartier);
            }
            affichageJoueur.afficheQuartierManufacture(nvxQuartiers);
        }
    }

    /**
     * si on a un doublon dans les cartes de la main, on l'échange contre 1piece
     */
    public void quartierLaboratoire(){
        for (Quartier quartier: quartierMain){
            if (quartiersConstruits.contains(quartier)){
                int rang = quartierMain.indexOf(quartier);
                pioche.remettreDansPioche(quartierMain.remove(rang));
                changerOr(1);
                affichageJoueur.afficheQuartierLaboratoire(quartier);
                return;
            }
        }
    }

    /**
     * on recupère le quartier détruit si 1piece et quartier pas déjà construit
     * @param quartierDetruit Quartier detruit par le condottiere
     */
    public void quartierCimetiere(Quartier quartierDetruit){
        if (nbOr >= 1 && !quartiersConstruits.contains(quartierDetruit)){
            changerOr(-1);
            quartiersConstruits.add(quartierDetruit);         //on utilise pas ajoutQuartierConstruit car on recup directement le quartier
            score+= quartierDetruit.getCout();
            affichageJoueur.afficheQuartierCimetiere(quartierDetruit);
        }
    }

    /**
     * pioche le bon nombre de cartes en fonction de l'observatoire
     * @return  la liste de quartier piochés, avec un null en 3e pos si pas d'observatoire
     */
    public List<Quartier> piocheDeBase(){
        List<Quartier> quartiersPioches = new ArrayList<>();
        if (!quartiersConstruits.contains(Quartier.OBSERVATOIRE)){
            quartiersPioches.add(pioche.piocherQuartier());
            quartiersPioches.add(pioche.piocherQuartier());
            quartiersPioches.add(null);
        } else {
            quartiersPioches.add(pioche.piocherQuartier());
            quartiersPioches.add(pioche.piocherQuartier());
            quartiersPioches.add(pioche.piocherQuartier());
        }
        return quartiersPioches;
    }
    // à implementer dans chaque bot
    public abstract Quartier construire();
    public abstract List<Quartier> faireActionDeBase();
    public abstract void actionSpecialeMagicien(Magicien magicien);
    public abstract void actionSpecialeVoleur(Voleur voleur);
    public abstract void actionSpecialeCondottiere(Condottiere condottiere);
    public abstract void choisirRole(List<Role> roles);
    public abstract List<Quartier> choisirCarte(List<Quartier> quartierPioches);
    public abstract void actionSpecialeAssassin(Assassin assassin);
    public abstract void setRolesVisible(List<Role> rolesVisible);
    //

}
