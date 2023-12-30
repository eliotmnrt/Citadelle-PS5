package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Random;

public class BotConstruitChere extends Bot{
    private String name;
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;
    public BotConstruitChere(){
        super();
        this.name="BotConstruitChere"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
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
        Quartier quartierConstruire=construire();
        if(quartierConstruire!=null){
            choixDeBase.add(quartierConstruire);
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
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(roles.size());
        setRole(roles.remove(intAleatoire));
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
}
