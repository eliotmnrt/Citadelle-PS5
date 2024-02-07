package Citadelle.teamU.moteurJeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;

import Citadelle.teamU.moteurJeu.bots.Bot;

import java.security.SecureRandom;
import java.util.*;

public class Tour {
    //génerer aléatoirement une liste de nombre de BOT +1
    private List<Bot> botListe;
    private int nbTour = 0;
    private AffichageJeu affichageJeu;
    private List<Role> roles = new ArrayList<>();
    private List<Role> rolesVisible;
    private SecureRandom random;
    private Bot mort;

    private List<Role> rolesTemp = new ArrayList<>();

    public Tour(List<Bot> botListe){
        random = new SecureRandom();
        affichageJeu = new AffichageJeu(this);
        this.botListe = botListe;
        rolesVisible = new ArrayList<>();

        roles.add(new Assassin(botListe,roles));
        roles.add(new Voleur(botListe, roles));
        roles.add(new Magicien(botListe));
        roles.add(new Roi(botListe));
        roles.add(new Pretre(botListe));
        roles.add(new Marchand(botListe));
        roles.add(new Architecte(botListe));
        roles.add(new Condottiere(botListe));
    }
    public List<Role> getRolesTemp(){
        return rolesTemp;
    }
    public void setRolesTemp(List<Role> roles){
        this.rolesTemp=roles;
    }

    /**
     * gère un tour de la partie
     */
    public void prochainTour(){
        nbTour++;
        rolesTemp = new ArrayList<>(roles);
        rolesTemp.remove(random.nextInt(rolesTemp.size()));
        rolesVisible.add(rolesTemp.remove(random.nextInt(rolesTemp.size())));
        rolesVisible.add(rolesTemp.remove(random.nextInt(rolesTemp.size())));
        affichageJeu.afficheCartesVisible(rolesVisible);
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

    /**
     * retrouve le bot qui gagne avec le score le plus elevé
     * @return  Bot qui a gagné ou null si égalité
     */
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

    /**
     * calcul des bonus de fin de partie
     * @param premierFinir le bot qui a déclenché la fin de partie, qui a son propre bonus
     */
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

    /**
     * cherche le nb de couleurs dans une liste de quartier
     * @param quartiers liste de quartiers
     * @return  nb de couleurs dfférentes
     */
    private int nbCouleur(List<Quartier> quartiers) {
        HashSet<TypeQuartier> arrayList = new HashSet<>();
        for(Quartier quartier : quartiers){
            arrayList.add(quartier.getTypeQuartier());
        }
        return arrayList.size();
    }

    /**
     * procède à la distribution des roles en fonction de la couronne
     * @return la liste ordonnée dans l'ordre selon la couronne
     */
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
