package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.Pioche;

import java.util.ArrayList;
import java.util.List;

/**
 * La stratégie est de construit des quartier chère pour avoir un score haut
 */
public class BotConstruitChere extends BotMalin {
    private static int numDuBotConstruitChere=1;

    /** constructeur
     * @param pioche Pioche
     */
    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.name="Bot qui construit chere_"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }


    /**
     * Il essaye de prendre le role Marchand pour avoir le plus d'or possible
     * @param roles List<Role>
     */
    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Marchand")){return;} //on cherche le marchand pour generer des sous
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }


    /**
     * Roles visible au début du tour
     * @param rolesVisible list<Role>
     */
    public void setRolesVisible(List<Role> rolesVisible) {
        this.rolesVisible = rolesVisible;
    }

    /**
     * @return Bot qui construit chere_son numéro
     */
    @Override
    public String toString(){
        return name;
    }
}
