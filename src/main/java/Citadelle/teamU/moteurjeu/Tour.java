package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private List<Bot> botListe;
    private static int nbTour = 0;
    List<Role> roles = new ArrayList<>();
    private SecureRandom random;

    List<Role> rolesTemp = new ArrayList<>();
    public Tour(List<Bot> botListe){
        random = new SecureRandom();
        roles.add(new Voleur(botListe, roles));
        roles.add(new Magicien(botListe));
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        roles.add(new Condottiere(botListe));
        this.botListe = botListe;
    }


    public void prochainTour(){
        rolesTemp = new ArrayList<>(roles);
        rolesTemp.remove(random.nextInt(rolesTemp.size()));
        rolesTemp.remove(random.nextInt(rolesTemp.size()));
        Bot premierFinir = null;
        nbTour++;
        distributionRoles();
        System.out.println("\n\n\nTour "+ nbTour);
        System.out.println(botListe);
        botListe.sort(Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe){
            bot.getAffichage().afficheBot();
            bot.faireActionSpecialRole();
            bot.faireActionDeBase();
            bot.construire();

            if(bot.getQuartiersConstruits().size()>=8 && premierFinir==null) premierFinir=bot; //Premier bot qui a 8 quartier
        }
        if (premierFinir!=null){
            bonus(premierFinir);
        }

    }

    public void bonus(Bot premierFinir) {
        Affichage affichageFin = new Affichage(botListe);
        premierFinir.setScore(premierFinir.getScore()+4); // on gagne 4 si on est le premier a finir
        premierFinir.getAffichage().afficheBonusPremier();
        for(Bot bot : botListe){
            List<Quartier> quartiers = bot.getQuartiersConstruits();
            if(quartiers.size()>=8 && bot != premierFinir){
                bot.setScore(bot.getScore()+2); //Si il n'est pas le premier a finir mais qu'il fini dans le tour (il a 8 quartiers ou plus)
                premierFinir.getAffichage().afficheBonusQuartier();
            }
            if(contiensCouleur(quartiers,TypeQuartier.VERT) && contiensCouleur(quartiers,TypeQuartier.VIOLET) && contiensCouleur(quartiers,TypeQuartier.BLEUE) && contiensCouleur(quartiers,TypeQuartier.JAUNE)&&contiensCouleur(quartiers,TypeQuartier.ROUGE)){
                bot.setScore(bot.getScore()+3); //Si le bot a un quartier de chaque couleur il gagne 3 points
                bot.getAffichage().afficheBonusCouleur();
            }
        }
        affichageFin.afficheLeVainqueur();
    }

    private boolean contiensCouleur(List<Quartier> quartiers, TypeQuartier typeQuartier) {
        for(Quartier quartier : quartiers){
            if(quartier.getTypeQuartier() == typeQuartier) return true;
        }
        return false;
    }
    public List<Bot> distributionRoles(){
        List<Bot> listeDistribution = botListe;
        //On met celui avec la couronne devant, et après on met ceux dans le bonne ordre
        listeDistribution.sort(Comparator.comparingInt(Bot::getOrdreChoixRole)); //Ordonne en fonction de leur ordre dans la partie
        for(int i = 0 ; i<listeDistribution.size() ; i++){
            if(listeDistribution.get(i).isCouronne()){
                //celui qui a la couronne choisi son role en premier puis celui après lui.. etc
                for(int j= 0; j < i ; j++){
                    listeDistribution.add(listeDistribution.get(j));
                    listeDistribution.remove(j);
                }
                break;
            }
        }
        System.out.println("Ordre dans lequel les bots choissent leurs role : "+listeDistribution);
        for (Bot bot: listeDistribution){
            bot.choisirRole(rolesTemp);
        }
        return listeDistribution;
    }


}
