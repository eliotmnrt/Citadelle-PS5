package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.AffichageJoueur;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotConstruitChere extends BotMalin {
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;

    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.name="BotConstruitChere"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }



    @Override
    public List<Quartier> faireActionDeBase() {
        // A REFACTORER
        quartiersViolets();         //actions spéciales violettes

        List<Quartier> choixDeBase = new ArrayList<>();

        boolean piocher=true;
        for(Quartier quartier: quartierMain){
            if (quartier.getCout() >= 4) {
                piocher = false;
                break;
            }
        }
        if (piocher){
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        else{
            choixDeBase.add(null);
            changerOr(2);
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        Collections.reverse(quartiersTrie);
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()>=COUT_MINIMAL && quartiersTrie.get(0).getCout()<=nbOr && !quartierConstruit.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            affichageJoueur.afficheConstruction(quartierConstruit);
            ajoutQuartierConstruit(quartierConstruit);
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
                pioche.remettreDansPioche(quartierPioches.remove(0));
                ajoutQuartierMain(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }
            quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
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
            affichageJoueur.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
            magicien.changeAvecBot(this, botAvecQuiEchanger);
            affichageJoueur.afficheNouvelleMainMagicien();

        } else {    // sinon on échange toutes ses cartes avec la pioche
            affichageJoueur.afficheActionSpecialeMagicienAvecPioche(this.getQuartierMain());
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichageJoueur.afficheNouvelleMainMagicien();
        }
    }



    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere){
        // détruit que un quartier qui coute 1
        List<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        for(Bot bot:botList){
            for(Quartier quartier: bot.getQuartiersConstruits()){
                if(quartier.getCout()==1 && !quartier.equals(Quartier.DONJON) && bot.getQuartiersConstruits().size()<8){
                    condottiere.destructionQuartier(this,bot, quartier);
                    return;
                }
            }
        }
    }

}
