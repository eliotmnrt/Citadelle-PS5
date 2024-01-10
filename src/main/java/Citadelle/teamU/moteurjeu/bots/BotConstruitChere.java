package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Random;

public class BotConstruitChere extends Bot{
    private String name;
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;
    private ArrayList<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotConstruitChere(){
        super();
        this.name="BotConstruitChere"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }

    // utile pour les tests uniquement
    public void setRolesRestants(ArrayList<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }


    @Override
    public ArrayList<Quartier> faireActionDeBase() {
        // A REFACTORER
        ArrayList<Quartier> choixDeBase = new ArrayList<>();

        boolean piocher=true;
        for(Quartier quartier: quartierMain){
            if (quartier.getCout()>=4){
                piocher=false;
            }
        }
        if (piocher){
            Quartier quartier1 = Pioche.piocherQuartier();
            Quartier quartier2 = Pioche.piocherQuartier();
            choixDeBase.add(quartier1);
            choixDeBase.add(quartier2);
            if (quartier1.getCout() > quartier2.getCout()) {
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);
                choixDeBase.add(quartier1);
            } else {
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                choixDeBase.add(quartier2);
            }
        }
        else{
            choixDeBase.add(null);
            changerOr(2);
        }
        return choixDeBase;
    }
    public Quartier construire(){

        int max=0;
        Quartier quartierChoisi=null;
        for(Quartier quartier :quartierMain){
            if(quartier.getCout()>max){
                max=quartier.getCout();
                quartierChoisi=quartier;
            }

        }
        // répétitions de code BotAleatoire, a refactorer plus tard
        if (quartierChoisi!=null) {
            if (quartierChoisi.getCout() <= nbOr && !quartierConstruit.contains(quartierChoisi) && quartierChoisi.getCout()>=COUT_MINIMAL) {
                quartierConstruit.add(quartierChoisi);
                quartierMain.remove(quartierChoisi);
                nbOr -= quartierChoisi.getCout();
                return quartierChoisi;
            }
        }
        return null;
    }
    @Override
    public String toString(){
        return name;
    }

    @Override
    public void choisirRole(ArrayList<Role> roles){
        nbOr += orProchainTour;         //on recupere l'or du vol
        orProchainTour = 0;

        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
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
            magicien.changeAvecBot(this, botAvecQuiEchanger);

        } else {    // sinon on échange toutes ses cartes avec la pioche
            magicien.changeAvecPioche(this, this.getQuartierMain());
        }
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
            int rang =randInt(5) +1;       // pour un nb aleatoire hors assassin et voleur
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }
}
