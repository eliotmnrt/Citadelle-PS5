package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Role;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class BotConstruitVite extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;

    public BotConstruitVite(){
        //Bot qui construit le plus vite possible
        //Il construit des qu'il peut (le moins chere)
        //prend des piece si : il a des quartier qui coute moins de 3
        //pioche sinon
        //Si il ne peut pas construire : il pioche jusqu'à avoir des carte qui coute moins de 3
        //Si il a des cartes qui coute moins de 3 : il prend de l'or
        //Il prend l'architecte si possible
        super();
        this.name = "Bot qui construit vite "+numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    @Override
    public ArrayList<Quartier> faireActionDeBase(){
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        //en indice 3 le quartier construit si un quartier a été construit
        ArrayList<Quartier> choixDeBase=new ArrayList<>();
        //cherche si il a au moins 1 quartier qu'il a pas deja construit qui coute moins de 3
        boolean aQuartierPasChere = false;
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=3&&!quartierConstruit.contains(quartier)){
                aQuartierPasChere = true;
            }
        }
        if(aQuartierPasChere){
            changerOr(2);
        }
        else{
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            Quartier quartier1= Pioche.piocherQuartier();
            Quartier quartier2=Pioche.piocherQuartier();
            choixDeBase.add(quartier1);
            choixDeBase.add(quartier2);
            if (quartier1.getCout()<quartier2.getCout()){
                ajoutQuartierMain(quartier1);
                Pioche.remettreDansPioche(quartier2);
                choixDeBase.add(quartier1);
            }
            else{
                ajoutQuartierMain(quartier2);
                Pioche.remettreDansPioche(quartier1);
                choixDeBase.add(quartier2);
            }
        }

        // construire un quartier parmis ceux qu'il peux construire
        ArrayList<Quartier> quartierConstruire= construireQuartierMoinsChere();
        if (quartierConstruire!=null){
            choixDeBase.addAll(quartierConstruire);
        }

        return choixDeBase;
    }

    @Override
    public void choisirRole(Role[] roles){
        Random aleatoire= new Random();
        int intAleatoire= aleatoire.nextInt(1); //Pour l'instant y'a qu'un role
        setRole(roles[intAleatoire]);
    }

    /**
     * Construi un quartier aléatoire parmis ceux qu'il peut construire
     */
    public ArrayList<Quartier> construireQuartierMoinsChere(){
        ArrayList<Quartier> quartiersTrie = quartierMain;
        Collections.sort(quartiersTrie, Comparator.comparingInt(Quartier::getCout));
        ArrayList<Quartier> quartierConstruit = new ArrayList<>();
        for(int i=0; i<role.getNbQuartierConstructible() ; i++){
            quartierConstruit.add(quartiersTrie.get(i));
            ajoutQuartierConstruit(quartiersTrie.get(i));
        }
        return quartiersTrie;
    }

    @Override
    public String toString(){
        return name;
    }
}
