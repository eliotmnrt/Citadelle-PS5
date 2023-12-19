package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Random;

public class BotConstruitChere extends Bot{
    private String name;
    private final int COUT_MINIMAL=4;

    public BotConstruitChere(){
        super();
        this.name="BotConstruitChere";
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

            if (quartier1.getCout() > quartier2.getCout()) {
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);

            } else {
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);

            }

        }
        else{


            changerOr(2);
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
