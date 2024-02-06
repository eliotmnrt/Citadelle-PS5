package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;

import Citadelle.teamU.moteurjeu.bots.Bot;

import java.security.SecureRandom;
import java.util.*;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private List<Bot> botListe;
    private int nbTour = 0;
    private AffichageJeu affichageJeu;
    private List<Role> roles = new ArrayList<>();
    private SecureRandom random;
    private Bot mort;

    List<Role> rolesTemp = new ArrayList<>();

    public Tour(List<Bot> botListe){
        random = new SecureRandom();
        this.affichageJeu = new AffichageJeu(this);
        roles.add(new Assassin(botListe,roles));
        roles.add(new Voleur(botListe, roles));
        roles.add(new Magicien(botListe));
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        roles.add(new Condottiere(botListe));
        this.botListe = botListe;
    }
    public List<Role> getRolesTemp(){
        return rolesTemp;
    }
    public void setRolesTemp(ArrayList<Role> roles){
        this.rolesTemp=roles;
    }

    public void prochainTour(){
        nbTour++;
        rolesTemp = new ArrayList<>(roles);
        rolesTemp.remove(random.nextInt(rolesTemp.size()));
        affichageJeu.afficheCartesVisible(rolesTemp.remove(random.nextInt(rolesTemp.size())),rolesTemp.remove(random.nextInt(rolesTemp.size())));
        Bot premierFinir = null;
        distributionRoles();
        affichageJeu.affichageNbTour();
        botListe.sort(Comparator.comparingInt(Bot::getOrdre));
        for (Bot bot: botListe) {
            if (!bot.estMort()) {
                bot.getAffichage().afficheBot();
                bot.faireActionSpecialRole();
                bot.faireActionDeBase();
                bot.construire();

                if (bot.getQuartiersConstruits().size() >= 8 && premierFinir == null)
                    premierFinir = bot; //Premier bot qui a 8 quartier
            } else {
                mort = bot;
            }
            bot.setMort(false);
        }
        if (mort != null){
            mort.getAffichage().afficheMort(mort);
            mort = null;
        }
        if (premierFinir!=null){
            bonus(premierFinir);
        }
    }
    public Bot getLeVainqueur(){
        //affiche le vainqueur de la partie, celui qui a un score maximal
        int max=0;
        List<Bot> botVainqueur = new ArrayList<>();
        botVainqueur.add(this.getBotListe().get(0)); //choisit arbitrairement au début, on modifie dans la boucle quand on compare le score
        for(Bot bot1: this.getBotListe()){
            if (bot1.getScore()>max){
                max= bot1.getScore();
                botVainqueur.clear();
                botVainqueur.add(bot1);
            }else if (bot1.getScore()==max){
                botVainqueur.add(bot1);
            }
        }
        return botVainqueur.size()==1 ? botVainqueur.get(0) : null;
    }

    public void bonus(Bot premierFinir) {
        premierFinir.setScore(premierFinir.getScore()+4); // on gagne 4 si on est le premier a finir
        premierFinir.getAffichage().afficheBonusPremier();
        for(Bot bot : botListe){
            List<Quartier> quartiers = bot.getQuartiersConstruits();
            if(quartiers.contains(Quartier.UNIVERSITE)){
                bot.setScore(bot.getScore()+2);
                bot.getAffichage().afficheBonusQuartierViolet(Quartier.UNIVERSITE);
            }
            if(quartiers.contains(Quartier.DRACOPORT)){
                bot.setScore(bot.getScore()+2);
                bot.getAffichage().afficheBonusQuartierViolet(Quartier.DRACOPORT);
            }
            if(quartiers.size()>=8&&bot!=premierFinir){
                bot.setScore(bot.getScore()+2); //Si il n'est pas le premier a finir mais qu'il fini dans le tour (il a 8 quartiers ou plus)
                bot.getAffichage().afficheBonusQuartier();
            }
            if(nbCouleur(bot.getQuartiersConstruits())==5){
                bot.setScore(bot.getScore()+3); //Si le bot a un quartier de chaque couleur il gagne 3 points
                bot.getAffichage().afficheBonusCouleur();
            }
            else if(bot.getQuartiersConstruits().contains(Quartier.COUR_DES_MIRACLES)){
                List<Quartier> arrayList = bot.getQuartiersConstruits();
                arrayList.remove(Quartier.COUR_DES_MIRACLES);
                if(nbCouleur(bot.getQuartiersConstruits())==4){
                    bot.setScore(bot.getScore()+3); //Si le bot a un quartier de chaque couleur il gagne 3 points
                    bot.getAffichage().afficheBonusCouleurAvecQV();
                }
            }
        }
        affichageJeu.afficheLeVainqueur(getLeVainqueur());
    }
    private int nbCouleur(List<Quartier> quartiers) {
        ArrayList<TypeQuartier> arrayList = new ArrayList<>();
        for(Quartier quartier : quartiers){
            if(!arrayList.contains(quartier.getTypeQuartier())){
                arrayList.add(quartier.getTypeQuartier());
            }
        }
        return arrayList.size();
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
        affichageJeu.affichageOrdre(listeDistribution);
        for (Bot bot: listeDistribution){
            bot.choisirRole(rolesTemp);
        }
        return listeDistribution;
    }


    public List<Bot> getBotListe() {
        return botListe;
    }

    public int getNbTour() {
        return nbTour;
    }
}
