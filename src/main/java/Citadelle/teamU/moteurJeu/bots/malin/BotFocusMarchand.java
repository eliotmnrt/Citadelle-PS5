package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.*;

public class BotFocusMarchand extends BotMalin {
    private static int numDuBotAleatoire = 1;
    private int nbQuartiersVertsConstruits = 0;

    public BotFocusMarchand(Pioche pioche) {
        //Bot qui monopolise le role de marchand
        //Il construit ses quartiers verts en priorité
        //prend des piece si : il a des quartiers verts non construits
        //pioche sinon
        super(pioche);
        this.name = "Bot qui focus marchand_" + numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    /**
     * fait action de base selon les quartiers verts
     * @return liste de quartiers piochés et gardés
     */
    @Override
    public List<Quartier> faireActionDeBase() {
        quartiersViolets();
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier vert non construit
        for (Quartier quartier : quartierMain) {
            if (quartier.getCouleur() == TypeQuartier.VERT && quartier.getCout() >= nbOr && !quartiersConstruits.contains(quartier)) {
                choixDeBase.add(null);
                changerOr(2);
                affichageJoueur.afficheChoixDeBase(choixDeBase);
                return choixDeBase;
            }
        }

        return suite(choixDeBase);
    }

    /**
     * choisi le role selon l'avancement de la partie pour notre bot
     * @param roles liste de Role
     */
    @Override
    public void choisirRole(List<Role> roles) {
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (nbQuartiersVertsConstruits < 2) {
            choisirRoleDebut(roles);
        } else {
            changementFocus = true;
            choisirRoleFin(roles);
        }
    }

    /**
     * focus archi et magicien pour recup des cartes vertes et les construire
     * @param roles liste de Role
     */
    public void choisirRoleDebut(List<Role> roles) {
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Architecte")){return;}
        if (trouverRole(roles, "Magicien")){return;}
        if (trouverRole(roles, "Marchand")){return;}

        int intAleatoire = randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * Choisi le role en fin de partie
     * @param roles liste de Role disponibles
     */
    public void choisirRoleFin(List<Role> roles) {
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Marchand")){
            return;
        }
        if (trouverRole(roles, "Roi")){return;}
        if (trouverRole(roles, "Architecte")){return;}
        if (trouverRole(roles, "Magicien")){return;}

        int intAleatoire = randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }


    /**
     * Choisi les cartes à piocher et à garder
     * @param quartierPioches liste de Quartiers piochés
     * @return la/les carte(s) choisie(s)
     */
    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartiersConstruits.contains(Quartier.BIBLIOTHEQUE)) {
            if (quartierPioches.get(2) == null) {        //on cherche la presence du quartier vert
                quartierPioches.remove(2);
                if (quartierPioches.get(0).getCouleur() == TypeQuartier.VERT && quartierPioches.get(1).getCouleur() == TypeQuartier.VERT) {
                    if(quartierPioches.get(0).getCout()<=quartierPioches.get(1).getCout()){
                        ajoutQuartierMain(quartierPioches.get(1));
                        pioche.remettreDansPioche(quartierPioches.remove(0));
                    }
                    else{
                        ajoutQuartierMain(quartierPioches.get(0));
                        pioche.remettreDansPioche(quartierPioches.remove(1));
                    }
                } else if (quartierPioches.get(0).getCouleur() == TypeQuartier.VERT) {
                    ajoutQuartierMain(quartierPioches.get(0));
                    pioche.remettreDansPioche(quartierPioches.remove(1));
                } else {
                    quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
                    ajoutQuartierMain(quartierPioches.get(0));
                    pioche.remettreDansPioche(quartierPioches.remove(1));
                }
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }

            Quartier quartierVert = null;
            List<Quartier> autresQuartiers = new ArrayList<>();
            for (Quartier quartierPioch : quartierPioches) {
                if (quartierPioch.getCouleur() == TypeQuartier.VERT) {
                    quartierVert = quartierPioch;
                } else {
                    autresQuartiers.add(quartierPioch);
                }
            }
            if (quartierVert != null) {
                ajoutQuartierMain(quartierVert);
                for (Quartier quart : autresQuartiers) {
                    pioche.remettreDansPioche(quart);
                }
                return new ArrayList<>(Collections.singleton(quartierVert));
            } else {
                return choisirCartePasImportante(quartierPioches);
            }
        } else {
            for (Quartier quartier : quartierPioches) {
                if (quartier != null) {
                    ajoutQuartierMain(quartier);
                }
            }
            return quartierPioches;
        }
    }

    @Override
    public Quartier construire(){
        List<Quartier> quartiersVerts = new ArrayList<>();
        List<Quartier> quartiersAutreConstructibles = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if(quartier.getCouleur() == TypeQuartier.VERT && !quartiersConstruits.contains(quartier)){
                quartiersVerts.add(quartier);
            } else if (quartier.getCout() <= nbOr && !quartiersConstruits.contains(quartier)){
                quartiersAutreConstructibles.add(quartier);
            }
        }
        if(!quartiersVerts.isEmpty()){       //construit en prio les quartiers verts le moins cher, s'il y en a
            quartiersVerts.sort(Comparator.comparingInt(Quartier::getCout));
            for (int i=quartiersVerts.size()-1; i>=0; i--){
                if (quartiersVerts.get(i).getCout() <= nbOr && !quartiersConstruits.contains(quartiersVerts.get(i))){                      //si on peut le construire tant mieux
                    affichageJoueur.afficheConstruction(quartiersVerts.get(i));
                    ajoutQuartierConstruit(quartiersVerts.get(i));
                    this.nbQuartiersVertsConstruits ++;
                    return quartiersVerts.get(i);
                }
            }
            return null;
        }
        if (!quartiersAutreConstructibles.isEmpty()){        //construit ensuite en prio le quartier le moins cher
            quartiersAutreConstructibles.sort(Comparator.comparingInt(Quartier::getCout));
            ajoutQuartierConstruit(quartiersAutreConstructibles.get(0));
            affichageJoueur.afficheConstruction(quartiersAutreConstructibles.get(0));
            return quartiersAutreConstructibles.get(0);
        }
        return null;
    }




    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        //n'echange pas sa main des autres joueurs mais toutes ses cartes non vertes avec la pioche, sauf s'il ne possede aucun quartier vert
        //si une carte verte est deja construite il l'échange aussi
        int comp = 0;
        for (Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.VERT && !quartiersConstruits.contains(quartier)){
                comp++;
            }
        }

        if(comp == 0){
            int nbQuartierMain = this.getQuartierMain().size();
            Bot botAvecQuiEchanger = null;
            for (Bot botAdverse: magicien.getBotliste()){  //on regarde qui a le plus de cartes dans sa main
                if(botAdverse.getQuartierMain().size() >= nbQuartierMain + 3){
                    botAvecQuiEchanger = botAdverse;
                }
            }
            if(botAvecQuiEchanger != null){ // si un bot a 3 cartes de plus que nous, on échange avec lui
                affichageJoueur.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
                magicien.changeAvecBot(this, botAvecQuiEchanger);
                affichageJoueur.afficheNouvelleMainMagicien();
                return;
            }
        }
        List<Quartier> quartiersAEchanger = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if (quartier.getCouleur() != TypeQuartier.VERT || quartiersConstruits.contains(quartier)){
                quartiersAEchanger.add(quartier);
            }
        }
        affichageJoueur.afficheActionSpecialeMagicienAvecPioche(quartiersAEchanger);
        magicien.changeAvecPioche(this, quartiersAEchanger);
        affichageJoueur.afficheNouvelleMainMagicien();
    }

    public void setRolesVisible(List<Role> rolesVisible) {
        this.rolesVisible = rolesVisible;
    }
}

