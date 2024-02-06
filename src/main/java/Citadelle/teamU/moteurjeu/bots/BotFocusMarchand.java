package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.AffichageJoueur;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotFocusMarchand extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;
    private List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner
    private int nbQuartiersVertsConstruits = 0;

    public BotFocusMarchand(Pioche pioche) {
        //Bot qui monopolise le role de marchand
        //Il construit ses quartiers verts en priorité
        //prend des piece si : il a des quartiers verts non construits
        //pioche sinon
        //s'il ne peut pas avoir le marchand, il cherche magic puis architecte pour renouveler ses cartes verts
        super(pioche);
        this.affichageJoueur = new AffichageJoueur(this);
        this.name = "BotQuiFocusMarchand" + numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setRolesRestants(List<Role> rolesRestants) {
        this.rolesRestants = rolesRestants;
    }

    @Override
    public List<Quartier> faireActionDeBase() {
        quartiersViolets();
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier jaune non construit
        for (Quartier quartier : quartierMain) {
            if (quartier.getCouleur() == TypeQuartier.VERT && quartier.getCout() >= nbOr && !quartierConstruit.contains(quartier)) {
                choixDeBase.add(null);
                changerOr(2);
                affichageJoueur.afficheChoixDeBase(choixDeBase);
                return choixDeBase;
            }
        }

        int aleat = randInt(3);
        if (aleat == 0) {
            choixDeBase.add(null);
            changerOr(2);
            affichageJoueur.afficheChoixDeBase(choixDeBase);
            return choixDeBase;
        } else {
            choixDeBase = piocheDeBase();

            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    @Override
    public void choisirRole(List<Role> roles) {
        if (nbQuartiersVertsConstruits < 3) {
            choisirRoleDebut(roles);
        } else {
            choisirRoleFin(roles);
        }
    }

    public void choisirRoleDebut(List<Role> roles) {
        if (orProchainTour >= 0) nbOr += orProchainTour;
        for (int i = 0; i < roles.size(); i++) {     //on cherche l'archi en premier pour avoir plus de cartes
            if (roles.get(i) instanceof Architecte) {
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i = 0; i < roles.size(); i++) {     //le magicien en 2eme pour renouveler ses cartes non vertes
            if (roles.get(i) instanceof Magicien) {
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i = 0; i < roles.size(); i++) {     //3eme meilleur choix choisir marchand
            if (roles.get(i) instanceof Marchand) {
                setRole(roles.remove(i));
                return;
            }
        }
        int intAleatoire = randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    public void choisirRoleFin(List<Role> roles) {
        if (orProchainTour >= 0) nbOr += orProchainTour;
        for (int i = 0; i < roles.size(); i++) {     //meilleur choix est le marchand
            if (roles.get(i) instanceof Marchand) {
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i = 0; i < roles.size(); i++) {     //le Roi en 2eme
            if (roles.get(i) instanceof Roi) {
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i = 0; i < roles.size(); i++) {     //l Architecte en 3eme
            if (roles.get(i) instanceof Architecte) {
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i = 0; i < roles.size(); i++) {     //le magicien en 4eme
            if (roles.get(i) instanceof Magicien) {
                setRole(roles.remove(i));
                return;
            }
        }
        int intAleatoire = randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }


    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartierConstruit.contains(Quartier.BIBLIOTHEQUE)) {
            if (quartierPioches.get(2) == null) {        //on cherche la presence du quartier vert
                quartierPioches.remove(2);
                if (quartierPioches.get(1).getCouleur() == TypeQuartier.VERT) {
                    ajoutQuartierMain(quartierPioches.get(1));
                    pioche.remettreDansPioche(quartierPioches.remove(0));
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
                quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
                Collections.reverse((quartierPioches));
                pioche.remettreDansPioche(quartierPioches.remove(0));
                pioche.remettreDansPioche(quartierPioches.remove(0));
                ajoutQuartierMain(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
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
            if(quartier.getCouleur() == TypeQuartier.VERT && !quartierConstruit.contains(quartier)){
                quartiersVerts.add(quartier);
            } else if (quartier.getCout() <= nbOr && !quartierConstruit.contains(quartier)){
                quartiersAutreConstructibles.add(quartier);
            }
        }
        if(!quartiersVerts.isEmpty()){       //construit en prio les quartiers verts le moins cher, s'il y en a
            quartiersVerts.sort(Comparator.comparingInt(Quartier::getCout));  //on cherche le quartier vert le plus cher possible
            for (int i=quartiersVerts.size()-1; i>=0; i--){
                if (quartiersVerts.get(i).getCout() <= nbOr && !quartierConstruit.contains(quartiersVerts.get(i))){                      //si on peut le construire tant mieux
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
    public void actionSpecialeVoleur(Voleur voleur){}
    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {}
    @Override
    public void actionSpecialeMagicien(Magicien magicien){}
    @Override

    public void actionSpecialeAssassin(Assassin assassin){}

}

