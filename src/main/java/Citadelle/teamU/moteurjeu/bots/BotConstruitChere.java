package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;

public class BotConstruitChere extends Bot{
    private String name;
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;
    private ArrayList<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.affichage = new Affichage(this);
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
                piocher = false;
            }
        }
        if (piocher){
            choixDeBase = choisirEntreDeuxQuartiersViaCout(1);
        }
        else{
            choixDeBase.add(null);
            changerOr(2);
        }
        affichage.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    @Override
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
        if (quartierChoisi!=null && (quartierChoisi.getCout() <= nbOr && !quartierConstruit.contains(quartierChoisi) && quartierChoisi.getCout()>=COUT_MINIMAL)) {
                ajoutQuartierConstruit(quartierChoisi);
                affichage.afficheConstruction(quartierChoisi);
                return quartierChoisi;
        }
        return null;
    }
    @Override
    public String toString(){
        return name;
    }


    @Override
    public void choisirRole(ArrayList<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;         //on recupere l'or du vol
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
            affichage.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
            magicien.changeAvecBot(this, botAvecQuiEchanger);
            affichage.afficheNouvelleMainMagicien();

        } else {    // sinon on échange toutes ses cartes avec la pioche
            affichage.afficheActionSpecialeMagicienAvecPioche(this.getQuartierMain());
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichage.afficheNouvelleMainMagicien();
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
            int rang =randInt(5) +1;       // pour un nb aleatoire hors assassin et voleur
            affichage.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }
}
