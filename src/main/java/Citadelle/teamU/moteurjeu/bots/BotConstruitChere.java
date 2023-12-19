package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Affichage;
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
    public void faireActionDeBase() {


        boolean piocher=true;
        for(Quartier quartier: quartierMain){
            if (quartier.getCout()>=4){
                piocher=false;
            }
        }
        if (piocher){
            Quartier quartier1 = Pioche.piocherQuartier();
            Quartier quartier2 = Pioche.piocherQuartier();
            Affichage.afficheQuartiersPioches(this,quartier1,quartier2);
            if (quartier1.getCout() > quartier2.getCout()) {
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);
                Affichage.afficheQuartierChoisi(this,quartier1);

            } else {
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                Affichage.afficheQuartierChoisi(this,quartier2);

            }

        }
        else{
            changerOr(2);
            Affichage.afficheChoixOr(this);
        }

    }
    public void construire(){

        int max=0;
        Quartier quartierChoisi=null;
        for(Quartier quartier :quartierMain){
            if(quartier.getCout()>max){
                max=quartier.getCout();
                quartierChoisi=quartier;
            }

        }
        if (quartierChoisi!=null) {
            if (quartierChoisi.getCout() <= nbOr && !quartierConstruit.contains(quartierChoisi) && quartierChoisi.getCout()>=COUT_MINIMAL) {
                quartierConstruit.add(quartierChoisi);
                quartierMain.remove(quartierChoisi);
                nbOr -= quartierChoisi.getCout();
                score += quartierChoisi.getCout();

                Affichage.afficheQuartierConstruit(this,quartierChoisi);
            }
        }

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
}
