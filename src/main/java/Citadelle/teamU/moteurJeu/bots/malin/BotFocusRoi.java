package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotFocusRoi extends BotMalin {
    private static int numDuBotAleatoire = 1;
    private int nbQuartiersJaunesConstruits = 0;
    private boolean strat2 = false;

    public BotFocusRoi(Pioche pioche){
        //Bot qui monopolise le role de roi
        //Il construit ses quartiers jaunes en priorité
        //prend des piece si : il a des quartier jaunes non construits
        //pioche sinon
        //s'il ne peut pas avoir le roi, il cherche magicien puis architecte pour renouveler ses cartes jaunes
        super(pioche);
        this.name = "BotQuiFocusRoi"+numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    /**
     * fait action de base selon les quartiers jaunes
     * @return liste de quartiers piochés et gardés
     */
    @Override
    public List<Quartier> faireActionDeBase(){
        quartiersViolets();
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier jaune non construit
        for(Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.JAUNE && quartier.getCout() >= nbOr && !quartiersConstruits.contains(quartier)) {
                choixDeBase.add(null);
                changerOr(2);
                affichageJoueur.afficheChoixDeBase(choixDeBase);
                return choixDeBase;
            }
        }

        if(strat2){
            choixDeBase.add(null);
            changerOr(2);
            affichageJoueur.afficheChoixDeBase(choixDeBase);
            return choixDeBase;

        } else {                            // sinon on pioche
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    /**
     * choix de cartes entre celles piochées, objectif de garder les cartes jaunes constructibles
     * @param quartierPioches liste de Quartiers piochés
     * @return carte(s) gardée(s)
     */
    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartiersConstruits.contains(Quartier.BIBLIOTHEQUE)){
            if (quartierPioches.get(2) == null){        //on cherche le quartier jaune s'il y a
                quartierPioches.remove(2);
                if (quartierPioches.get(1).getCouleur() == TypeQuartier.JAUNE){
                    ajoutQuartierMain(quartierPioches.get(1));
                    pioche.remettreDansPioche(quartierPioches.remove(0));
                }
                else if(quartierPioches.get(0).getCouleur() == TypeQuartier.JAUNE){
                    ajoutQuartierMain(quartierPioches.get(0));
                    pioche.remettreDansPioche(quartierPioches.remove(1));
                } else {
                    quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
                    ajoutQuartierMain(quartierPioches.get(0));
                    pioche.remettreDansPioche(quartierPioches.remove(1));
                }
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }

            Quartier quartierJaune = null;
            List<Quartier> autresQuartiers = new ArrayList<>();
            for (Quartier quartierPioch : quartierPioches) {
                if (quartierPioch.getCouleur() == TypeQuartier.JAUNE) {
                    quartierJaune = quartierPioch;
                } else {
                    autresQuartiers.add(quartierPioch);
                }
            }
            if (quartierJaune != null){
                ajoutQuartierMain(quartierJaune);
                for (Quartier quart: autresQuartiers){
                    pioche.remettreDansPioche(quart);
                }
                return new ArrayList<>(Collections.singleton(quartierJaune));
            } else {
                quartierPioches = new ArrayList<>(quartierPioches);
                quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
                Collections.reverse((quartierPioches));
                pioche.remettreDansPioche(quartierPioches.remove(0));
                pioche.remettreDansPioche(quartierPioches.remove(0));
                ajoutQuartierMain(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }
        } else {
            for (Quartier quartier: quartierPioches){
                if (quartier != null){
                    ajoutQuartierMain(quartier);
                }
            }
            return quartierPioches;
        }
    }

    /**
     * choisi le role selon l'avancement de la partie pour notre bot
     * @param roles liste de Role
     */
    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (nbQuartiersJaunesConstruits < 2){
            choisirRoleDebut(roles);
        } else {
            strat2 = true;
            choisirRoleFin(roles);
        }
    }

    //pour les tests
    public void setNbQuartiersJaunesConstruits(int nb){
        nbQuartiersJaunesConstruits = nb;
    }


    /**
     * focus archi et magicien pour recup des cartes jaunes et les construire
     * @param roles liste de Role
     */
    public void choisirRoleDebut(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Architecte")){ return; }
        if (trouverRole(roles, "Magicien")){return;}
        if (trouverRole(roles, "Roi")){return;}
        int intAleatoire= randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * si 2 quartiers jaunes construits, ons e met a focus le roi
     * @param roles roles
     */
    public void choisirRoleFin(List<Role> roles){
        role = null;
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Roi")){return;}
        if (trouverRole(roles, "Architecte")){return;}
        if (trouverRole(roles, "Magicien")){return;}

        int intAleatoire= randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }


    /**
     * si on a 1 quartier jaune, on le construit (on economise de l'argent s'il faut), sinon on construit le quartier le moins cher
     * @return Quartier construit ou null
     */
    @Override
    public Quartier construire(){
        List<Quartier> quartiersJaunes = new ArrayList<>();
        List<Quartier> quartiersAutreConstructibles = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if(quartier.getCouleur() == TypeQuartier.JAUNE && !quartiersConstruits.contains(quartier)){
                quartiersJaunes.add(quartier);
            } else if (quartier.getCout() <= nbOr && !quartiersConstruits.contains(quartier)){
                quartiersAutreConstructibles.add(quartier);
            }
        }
        if(!quartiersJaunes.isEmpty()){       //construit en prio les quartiers jaunes le moins cher, s'il y en a
            quartiersJaunes.sort(Comparator.comparingInt(Quartier::getCout));  //on cherche le quartier jaune le plus cher possible
            for (int i=quartiersJaunes.size()-1; i>=0; i--){
                if (quartiersJaunes.get(i).getCout() <= nbOr && !quartiersConstruits.contains(quartiersJaunes.get(i))){                      //si on peut le construire tant mieux
                    affichageJoueur.afficheConstruction(quartiersJaunes.get(i));
                    ajoutQuartierConstruit(quartiersJaunes.get(i));
                    this.nbQuartiersJaunesConstruits ++;
                    return quartiersJaunes.get(i);
                }
            }
            return null;
        }
        if (!quartiersAutreConstructibles.isEmpty()){        //construit ensuite en prio le quartier le moins cher
            quartiersAutreConstructibles.sort(Comparator.comparingInt(Quartier::getCout));
            Collections.reverse(quartiersAutreConstructibles);
            ajoutQuartierConstruit(quartiersAutreConstructibles.get(0));
            affichageJoueur.afficheConstruction(quartiersAutreConstructibles.get(0));
            return quartiersAutreConstructibles.get(0);
        }
        return null;
    }


    /**
     * effectue action du magicien en visant plutôt la pioche (ca lui permet de garder ses quartiers jaunes s'il le souhaite)
     * @param magicien Role magicien
     */
    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        //n'echange pas sa main des autres joueurs mais toutes ses cartes non nobles(jaune) avec la pioche, sauf s'il ne possede aucun quartier jaune
        //si une carte jaune est deja construite il l'échange aussi
        int comp = 0;
        for (Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.JAUNE && !quartiersConstruits.contains(quartier)){
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
            if (quartier.getCouleur() != TypeQuartier.JAUNE || quartiersConstruits.contains(quartier)){
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
    @Override
    public String toString(){
        return "Bot qui focus le roi";
    }
}
