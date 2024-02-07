package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.*;

public class BotConstruitVite extends BotMalin {
    private static int numDuBot = 1;

    public BotConstruitVite(Pioche pioche){
        //Bot qui construit le plus vite possible
        //Il construit des qu'il peut (le moins chere)
        //prend des piece si : il a des quartier qui coute moins de 3
        //pioche sinon
        //Si il ne peut pas construire : il pioche jusqu'à avoir des carte qui coute moins de 3
        //Si il a des cartes qui coute moins de 3 : il prend de l'or
        //Il prend l'architecte si possible
        super(pioche);
        this.name = "Bot_qui_construit_vite" + numDuBot;
        numDuBot++;
    }
    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Architecte")){return;} //on cherche l architecte pour construire la max
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int nbQuartierMain = this.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: magicien.getBotliste()){  //on regarde qui a le plus de cartes dans sa main
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
            affichageJoueur.afficheActionSpecialeMagicienAvecPioche(this.quartierMain);
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichageJoueur.afficheNouvelleMainMagicien();
        }
    }


    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere){
        // detruit que le quartier le moins chère du bot qui a le plus de quartier construits
        List<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        Bot botMax = botList.get(0);
        for(Bot bot:botList){
            if(botMax.getQuartiersConstruits().size() < bot.getQuartiersConstruits().size() && bot.getQuartiersConstruits().size()<8){
                //pas un bot qui a 8 quartiers
                botMax=bot;
            }
        }
        if (botMax.getQuartiersConstruits().isEmpty()) {
            return;
        }
        Quartier minPrixQuartier=botMax.getQuartiersConstruits().get(0);
        for(Quartier quartier: botMax.getQuartiersConstruits()){
            if(quartier.getCout() < minPrixQuartier.getCout() && !quartier.equals(Quartier.DONJON)){
                minPrixQuartier = quartier;
            }
        }
        if (minPrixQuartier.getCout() - 1 <= nbOr){
            condottiere.destructionQuartier(this,botMax,minPrixQuartier);
        }
    }

    public void setRolesVisible(List<Role> rolesVisible) {
        this.rolesVisible = rolesVisible;
    }
}
