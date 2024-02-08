package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.Pioche;

import java.util.ArrayList;
import java.util.List;

/**
 * La stratégie est de construit des quartier chère pour avoir un score haut
 */
public class BotConstruitChere extends BotMalin {
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;

    /**
     * @param pioche
     */
    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.name="Bot qui construit chere_"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }


    /**
     * Il essaye de prendre le role Marchand pour avoir le plus d'or possible
     * @param roles
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
     * @param rolesVisible
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
