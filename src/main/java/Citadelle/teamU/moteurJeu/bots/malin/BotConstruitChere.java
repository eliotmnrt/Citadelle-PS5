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
     * si un bot a plus de cartes que nous, on échange avec lui
     * sinon on échange avec la pioche
     * @param magicien Role magicien
     */
    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int nbQuartierMain = this.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: magicien.getBotliste()){  //on cherche le bot qui a le plus de cartes dans sa main
            if(botAdverse.getQuartierMain().size() > nbQuartierMain){
                botAvecQuiEchanger = botAdverse;
                nbQuartierMain = botAvecQuiEchanger.getQuartierMain().size();
            }
        }
        if(botAvecQuiEchanger != null){ // si un bot a plus de cartes que nous, on échange avec lui
            affichageJoueur.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
            magicien.changeAvecBot(this, botAvecQuiEchanger);
            affichageJoueur.afficheNouvelleMainMagicien();

        } else {    // sinon on échange toutes ses cartes avec la pioche
            affichageJoueur.afficheActionSpecialeMagicienAvecPioche(this.getQuartierMain());
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichageJoueur.afficheNouvelleMainMagicien();
        }
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
