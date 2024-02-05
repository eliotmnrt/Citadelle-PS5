package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.List;

public abstract class BotMalin extends Bot {

    protected List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    protected BotMalin(Pioche pioche){
        super(pioche);
        rolesRestants = new ArrayList<>();
    }


    // utile pour les tests uniquement
    public void setRolesRestants(List<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }


    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;        //on recupere l'or du vol
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }


    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        if(rolesRestants.size()>1){
            int rang = randInt(rolesRestants.size());
            affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
            assassin.tuer(rolesRestants.get(rang));

        }
        else{
            int rang = randInt(7) + 1  ;     // pour un nb aleatoire hors assassin et condottiere prsq on il y est pas dans ma branche
            affichageJoueur.afficheMeurtre(assassin.getRoles().get(rang));
            assassin.tuer(assassin.getRoles().get(rang));
        }
    }

    //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang;
            do{
                rang = randInt(rolesRestants.size());
            }while(rolesRestants.get(rang) instanceof Assassin ); //ne pas prendre l'assassin car on ne peut pas le voler


            affichageJoueur.afficheActionSpecialeVoleur(rolesRestants.get(rang));
            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang =randInt(6) + 2;       // pour un nb aleatoire hors assassin et voleur
            affichageJoueur.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }

}

