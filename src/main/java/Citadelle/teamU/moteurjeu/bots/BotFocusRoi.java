package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BotFocusRoi extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;
    private ArrayList<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotFocusRoi(Pioche pioche){
        //Bot qui monopolise le role de roi
        //Il construit ses quartiers jaunes en priorité
        //prend des piece si : il a des quartier jaunes non construits
        //pioche sinon
        //s'il ne peut pas avoir le roi, il cherche magicien puis architecte pour renouveler ses cartes jaunes
        super(pioche);
        this.name = "Bot_qui_Focus_Roi"+numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    @Override
    public String toString(){
        return name;
    }

    // utile pour les tests uniquement
    public void setRolesRestants(ArrayList<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }

    @Override
    public ArrayList<Quartier> faireActionDeBase(){
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        ArrayList<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier jaune non construit
        for(Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.JAUNE && quartier.getCout() >= nbOr && !quartierConstruit.contains(quartier)) {
                choixDeBase.add(null);
                changerOr(2);
                return choixDeBase;
            }
        }

        int aleat = randInt(3);
        if(aleat == 0){                     //1 chances sur 3 de prendre de l'or
            choixDeBase.add(null);
            changerOr(2);
            return choixDeBase;
        } else {                            // sinon on pioche
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1 = pioche.piocherQuartier();
            Quartier quartier2 = pioche.piocherQuartier();
            choixDeBase.add(quartier1);
            choixDeBase.add(quartier2);
            if (quartier1.getCouleur() == TypeQuartier.JAUNE){
                ajoutQuartierMain(quartier1);
                choixDeBase.add(quartier1);
                pioche.remettreDansPioche(quartier2);
            }
            else if(quartier2.getCouleur() == TypeQuartier.JAUNE){
                ajoutQuartierMain(quartier2);
                choixDeBase.add(quartier2);
                pioche.remettreDansPioche(quartier1);
            } else {
                int rand = randInt(2);
                ajoutQuartierMain(choixDeBase.get(rand));
                choixDeBase.add(choixDeBase.get(rand));
                pioche.remettreDansPioche(choixDeBase.get(1 - rand));
            }
        }



        return choixDeBase;
    }

    @Override
    public void choisirRole(ArrayList<Role> roles){
        nbOr += orProchainTour;         //on recupere l'or du vol
        orProchainTour = 0;
        System.out.println(this.name + roles);
        for (int i=0; i<roles.size(); i++){     //on cherche le roi en premier
            if(roles.get(i) instanceof Roi){
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i=0; i<roles.size(); i++){     //l'archi en 2eme
            if(roles.get(i) instanceof Architecte){
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i=0; i<roles.size(); i++){     //le magicien en 3eme
            if(roles.get(i) instanceof Magicien){
                setRole(roles.remove(i));
                return;
            }
        }

        int intAleatoire= randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    @Override
    public Quartier construire(){
        ArrayList<Quartier> quartiersJaunes = new ArrayList<>();
        ArrayList<Quartier> quartiersAutreConstructibles = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if(quartier.getCouleur() == TypeQuartier.JAUNE){
                quartiersJaunes.add(quartier);
            } else if (quartier.getCout() <= nbOr && !quartierConstruit.contains(quartier)){
                quartiersAutreConstructibles.add(quartier);
            }
        }
        if(!quartiersJaunes.isEmpty()){       //construit en prio le jaune le moins cher
            Collections.sort(quartiersJaunes, Comparator.comparingInt(Quartier::getCout));  //on cherche le quartier jaune le moins cher
            if (quartiersJaunes.get(quartiersJaunes.size()-1).getCout() <= nbOr && !quartierConstruit.contains(quartiersJaunes.get(quartiersJaunes.size()-1))){                      //si on peut le construire tant mieux
                ajoutQuartierConstruit(quartiersJaunes.get(quartiersJaunes.size()-1));
            } else {                                                            //sinon on garde son or pour le construire plus tard
                return null;
            }
            return quartiersJaunes.get(0);
        }
        else if (!quartiersAutreConstructibles.isEmpty()){        //construit ensuite en prio le quartier le plus cher
            Collections.sort(quartiersAutreConstructibles, Comparator.comparingInt(Quartier::getCout));
            Collections.reverse(quartiersAutreConstructibles);
            ajoutQuartierConstruit(quartiersAutreConstructibles.get(0));
            return quartiersAutreConstructibles.get(0);
        }
        return null;
    }

    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        //n'echange pas sa main des autres joueurs mais toutes ses cartes non nobles(jaune) avec la pioche, sauf s'il ne possede aucun quartier jaune
        //si une carte jaune est deja construite il l'échange aussi

        int comp = 0;
        for (Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.JAUNE){
                comp++;
            }
        }

        if(comp == 0){
            int nbQuartierMain = this.getQuartierMain().size();
            Bot botAvecQuiEchanger = null;
            for (Bot botAdverse: magicien.getBotListe()){  //on regarde qui a le plus de cartes dans sa main
                if(botAdverse.getQuartierMain().size() > nbQuartierMain){
                    botAvecQuiEchanger = botAdverse;
                    nbQuartierMain = botAvecQuiEchanger.getQuartierMain().size();
                }
            }
            if(botAvecQuiEchanger != null){ // si un bot a 3 cartes de plus que nous, on échange avec lui
                magicien.changeAvecBot(this, botAvecQuiEchanger);
                return;
            }
        }


        ArrayList<Quartier> quartiersAEchanger = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if (quartier.getCouleur() != TypeQuartier.JAUNE || quartierConstruit.contains(quartier)){
                quartiersAEchanger.add(quartier);
            }
        }
        magicien.changeAvecPioche(this, quartiersAEchanger);
    }

    @Override           //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang = randInt(rolesRestants.size());

            /*while (rang == rolesRestants.indexOf(Assassin))
             */

            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang = randInt(5) +1;       // pour un nb aleatoire hors assassin et voleur
            //+1 pcq le premier c'est voleur et on veut pas le prendre
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }
}
