package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

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
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()<4 && quartiersTrie.get(0).getCout()<=nbOr && !quartierConstruit.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            ajoutQuartierConstruit(quartierConstruit);
            affichageJoueur.afficheConstruction(quartierConstruit);
            return quartierConstruit;
        }
        return null;
    }

    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartierConstruit.contains(Quartier.BIBLIOTHEQUE)){
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
