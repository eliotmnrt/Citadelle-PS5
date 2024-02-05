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

public class BotFocusRoi extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;
    private List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner
    private int nbQuartiersJaunesConstruits = 0;

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

    @Override
    public String toString(){
        return name;
    }

    // utile pour les tests uniquement
    public void setRolesRestants(List<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }

    @Override
    public List<Quartier> faireActionDeBase(){
        quartiersViolets();
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier jaune non construit
        for(Quartier quartier : quartierMain){
            if (quartier.getCouleur() == TypeQuartier.JAUNE && quartier.getCout() >= nbOr && !quartierConstruit.contains(quartier)) {
                choixDeBase.add(null);
                changerOr(2);
                affichageJoueur.afficheChoixDeBase(choixDeBase);
                return choixDeBase;
            }
        }

        int aleat = randInt(3);
        if(aleat == 0){                     //1 chances sur 3 de prendre de l'or
            choixDeBase.add(null);
            changerOr(2);
            affichageJoueur.afficheChoixDeBase(choixDeBase);
            return choixDeBase;
        } else {                            // sinon on pioche
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            choixDeBase = piocheDeBase();

            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartierConstruit.contains(Quartier.BIBLIOTHEQUE)){
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

    @Override
    public void choisirRole(List<Role> roles){
        if (nbQuartiersJaunesConstruits < 2){
            choisirRoleDebut(roles);
        } else {
            choisirRoleFin(roles);
        }
    }

    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        if(rolesRestants.size()>1){
            int rang= randInt(rolesRestants.size());
            assassin.tuer(rolesRestants.get(rang));

        }
        else{
            int rang = randInt(7)+ 1  ;     // pour un nb aleatoire hors assassin et condottiere prsq on il y est pas dans ma branche
            assassin.tuer(assassin.getRoles().get(rang));
        }
    }

    public void choisirRoleDebut(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        for (int i=0; i<roles.size(); i++){     //on cherche l'archi en premier => plus de cartes
            if(roles.get(i) instanceof Architecte){
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i=0; i<roles.size(); i++){     //le magicien en 2eme => renouveler ses cartes non jaunes
            if(roles.get(i) instanceof Magicien){
                setRole(roles.remove(i));
                return;
            }
        }
        for (int i=0; i<roles.size(); i++){     //le roi sinon
            if(roles.get(i) instanceof Roi){
                setRole(roles.remove(i));
                return;
            }
        }
        int intAleatoire= randInt(roles.size());    //sinon aleatoire
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * utilisé si on a 2 quartiers jaunes ou plus construits, cad si prendre le roi est rentable
     * @param roles roles
     */
    public void choisirRoleFin(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
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
        List<Quartier> quartiersJaunes = new ArrayList<>();
        List<Quartier> quartiersAutreConstructibles = new ArrayList<>();
        for(Quartier quartier: quartierMain){
            if(quartier.getCouleur() == TypeQuartier.JAUNE && !quartierConstruit.contains(quartier)){
                quartiersJaunes.add(quartier);
            } else if (quartier.getCout() <= nbOr && !quartierConstruit.contains(quartier)){
                quartiersAutreConstructibles.add(quartier);
            }
        }
        if(!quartiersJaunes.isEmpty()){       //construit en prio les quartiers jaunes le moins cher, s'il y en a
            quartiersJaunes.sort(Comparator.comparingInt(Quartier::getCout));  //on cherche le quartier jaune le plus cher possible
            for (int i=quartiersJaunes.size()-1; i>=0; i--){
                if (quartiersJaunes.get(i).getCout() <= nbOr && !quartierConstruit.contains(quartiersJaunes.get(i))){                      //si on peut le construire tant mieux
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
            ajoutQuartierConstruit(quartiersAutreConstructibles.get(0));
            affichageJoueur.afficheConstruction(quartiersAutreConstructibles.get(0));
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
            if (quartier.getCouleur() == TypeQuartier.JAUNE && !quartierConstruit.contains(quartier)){
                comp++;
            }
        }

        if(comp == 0){
            int nbQuartierMain = this.getQuartierMain().size();
            Bot botAvecQuiEchanger = null;
            for (Bot botAdverse: magicien.getBotListe()){  //on regarde qui a le plus de cartes dans sa main
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
            if (quartier.getCouleur() != TypeQuartier.JAUNE || quartierConstruit.contains(quartier)){
                quartiersAEchanger.add(quartier);
            }
        }
        affichageJoueur.afficheActionSpecialeMagicienAvecPioche(quartiersAEchanger);
        magicien.changeAvecPioche(this, quartiersAEchanger);
        affichageJoueur.afficheNouvelleMainMagicien();
    }

    @Override           //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang = randInt(rolesRestants.size());
            affichageJoueur.afficheActionSpecialeVoleur(rolesRestants.get(rang));
            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang = randInt(5) +1;       // pour un nb aleatoire hors assassin et voleur
            //+1 pcq le premier c'est voleur et on veut pas le prendre
            affichageJoueur.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }

    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {
        // détruit que un quartier qui coute 1
        List<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        for(Bot bot:botList){
            for(Quartier quartier: bot.getQuartiersConstruits()){
                if(quartier.getCout()==1 && !quartier.equals(Quartier.DONJON)){
                    condottiere.destructionQuartier(this,bot, quartier);
                    return;
                }
            }
        }
    }
}
