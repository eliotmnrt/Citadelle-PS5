package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Pretre;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.*;

/**
 * La stratégie est de construit vite des quartier pas chère (moins de 3) pour essayer de finir en premier
 */
public class BotConstruitVite extends BotMalin {
    private static int numDuBot = 1;

    /**
     * @param pioche
     */
    public BotConstruitVite(Pioche pioche){
        super(pioche);
        this.name = "Bot qui construit vite_" + numDuBot;
        numDuBot++;
    }

    /**
     * Il vise de choisir l'Architecte pour construire le plus de quartiers possible rapidement
     * @param roles
     */
    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (trouverRole(roles, "Architecte")){return;}
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * Si il a des cartes qui coute moins de 3 : il prend de l'or
     * Sinon il pioche et choisi la carte la moins chère
     * @return
     */
    @Override
    public List<Quartier> faireActionDeBase(){
        quartiersViolets();         //actions spéciales violettes
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        //en indice 3 le quartier construit si un quartier a été construit
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier qu'il a pas deja construit qui coute moins de 3
        boolean aQuartierPasChere = false;
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<4 && !quartiersConstruits.contains(quartier)){
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
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    /**
     * Construit le moins chère (qui doit couter moins de 3)
     * @return
     */
    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()<4 && quartiersTrie.get(0).getCout()<=nbOr && !quartiersConstruits.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            ajoutQuartierConstruit(quartierConstruit);
            affichageJoueur.afficheConstruction(quartierConstruit);
            return quartierConstruit;
        }
        return null;
    }

    /**
     * @param quartierPioches liste de Quartiers piochés
     * On choisit celui qui coute le moins chère
     * @return
     */
    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartiersConstruits.contains(Quartier.BIBLIOTHEQUE)){
            if (quartierPioches.get(2) == null){
                quartierPioches.remove(2);
                quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
                Collections.reverse(quartierPioches);
                pioche.remettreDansPioche(quartierPioches.remove(0));
                ajoutQuartierMain(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }
            quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
            Collections.reverse(quartierPioches);
            pioche.remettreDansPioche(quartierPioches.remove(0));
            pioche.remettreDansPioche(quartierPioches.remove(0));
            ajoutQuartierMain(quartierPioches.get(0));
            return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
        } else {
            for (Quartier quartier: quartierPioches){
                if (quartier != null){
                    ajoutQuartierMain(quartier);
                }
            }
            return quartierPioches;
        }
    }


    /**
     * detruit que le quartier le moins chère du bot qui a le plus de quartier construits
     * @param condottiere Role condottiere
     */
    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere){
        List<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        Bot botMax = botList.get(0);
        for(Bot bot:botList){
            if(botMax.getQuartiersConstruits().size() < bot.getQuartiersConstruits().size() && bot.getQuartiersConstruits().size()<8 && !(bot.getRole() instanceof  Pretre)){
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
        if (minPrixQuartier.getCout() - 1 <= nbOr && botMax.getQuartiersConstruits().size()<8 && !(botMax.getRole() instanceof Pretre)){
            condottiere.destructionQuartier(this,botMax,minPrixQuartier);
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
     * @return Bot construit vite_son numéro
     */
    @Override
    public String toString(){
        return name;
    }
}
