package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;

public class BotConstruitChere extends Bot{
    private String name;

    public BotConstruitChere(){
        super();
        this.name="BotConstruitChere";
    }

    @Override
    public ArrayList<Quartier> faireActionDeBase() {
        // A REFACTORER
        ArrayList<Quartier> choixDeBase = new ArrayList<>();
        Quartier quartierConstruire = construireQuartierChere();
        if (quartierConstruire != null) {
            quartierConstruit.add(quartierConstruire);
            quartierMain.remove(quartierConstruire);
            nbOr -= quartierConstruire.getCout();
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
            choixDeBase.add(quartierConstruire);


        } else {
            choixDeBase.add(null);
            changerOr(2);
            Quartier quartierConstruire2 = construireQuartierChere();
            if (quartierConstruire2 != null) {
                quartierConstruit.add(quartierConstruire2);
                quartierMain.remove(quartierConstruire2);
                nbOr -= quartierConstruire2.getCout();
                choixDeBase.add(quartierConstruire2);
            }


        }
        return choixDeBase;
    }
    public Quartier construireQuartierChere(){

        int max=0;
        Quartier quartierChoisi=null;
        /*
        ArrayList<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr&&!quartierConstruit.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(quartiersPossible.size()==0){
            return null;
        }*/
        for(Quartier quartier :quartierMain){
            if(quartier.getCout()>max){
                max=quartier.getCout();
                quartierChoisi=quartier;
            }

        }
        // répétitions de code BotAleatoire, a refactorer plus tard
        if (quartierChoisi!=null) {
            if (quartierChoisi.getCout() <= nbOr && !quartierConstruit.contains(quartierChoisi)) {
                return quartierChoisi;
            }
        }
        return null;
    }
    @Override
    public String toString(){

        return name;
    }

}
