package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.*;

public class BotConstruitVite extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;
    private ArrayList<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotConstruitVite(Pioche pioche){
        //Bot qui construit le plus vite possible
        //Il construit des qu'il peut (le moins chere)
        //prend des piece si : il a des quartier qui coute moins de 3
        //pioche sinon
        //Si il ne peut pas construire : il pioche jusqu'à avoir des carte qui coute moins de 3
        //Si il a des cartes qui coute moins de 3 : il prend de l'or
        //Il prend l'architecte si possible
        super(pioche);
        this.affichage = new Affichage(this);
        this.name = "Bot_qui_construit_vite" + numDuBotAleatoire;
        numDuBotAleatoire++;
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
        //en indice 3 le quartier construit si un quartier a été construit
        ArrayList<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier qu'il a pas deja construit qui coute moins de 3
        boolean aQuartierPasChere = false;
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<4 && !quartierConstruit.contains(quartier)){
                aQuartierPasChere = true;
                break;
            }
        }
        if(aQuartierPasChere){
            choixDeBase.add(null);
            changerOr(2);
        }
        else{
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            choixDeBase = choisirEntreDeuxQuartiersViaCout(-1);
        }
        affichage.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }


    @Override
    public void choisirRole(ArrayList<Role> roles){
        nbOr += orProchainTour;         //on recupere l'or du vol
        orProchainTour = 0;
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * Construit un quartier
     */

    @Override
    public Quartier construire(){
        ArrayList<Quartier> quartiersTrie = quartierMain;
        Collections.sort(quartiersTrie, Comparator.comparingInt(Quartier::getCout));
        if(!quartiersTrie.isEmpty()&&quartiersTrie.get(0).getCout()<4&&quartiersTrie.get(0).getCout()<=nbOr){
            Quartier quartierConstruit = quartiersTrie.get(0);
            ajoutQuartierConstruit(quartierConstruit);
            affichage.afficheConstruction(quartierConstruit);
            return quartierConstruit;
        }
        return null;
    }


    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int nbQuartierMain = this.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: magicien.getBotListe()){  //on regarde qui a le plus de cartes dans sa main
            if(botAdverse.getQuartierMain().size() > nbQuartierMain){
                botAvecQuiEchanger = botAdverse;
                nbQuartierMain = botAvecQuiEchanger.getQuartierMain().size();
            }
        }
        if(botAvecQuiEchanger != null){ // si un bot a plus de cartes que nous, on échange avec lui
            affichage.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
            magicien.changeAvecBot(this, botAvecQuiEchanger);
        } else {    // sinon on échange toutes ses cartes avec la pioche
            affichage.afficheActionSpecialeMagicienAvecPioche(this.quartierMain);
            magicien.changeAvecPioche(this, this.getQuartierMain());
        }
    }

    //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang = randInt(rolesRestants.size());

            /*while (rang == rolesRestants.indexOf(Assassin))
            */
            affichage.afficheActionSpecialeVoleur(rolesRestants.get(rang));
            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang = randInt(5) +1;       // pour un nb aleatoire hors assassin et voleur
            //+1 pcq le premier c'est voleur et on veut pas le prendre
            affichage.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }


    @Override
    public String toString(){
        return name;
    }


}
