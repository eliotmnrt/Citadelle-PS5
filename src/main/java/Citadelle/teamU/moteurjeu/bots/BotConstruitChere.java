package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.AffichageJoueur;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BotConstruitChere extends Bot{
    private String name;
    private final int COUT_MINIMAL=4;
    private static int numDuBotConstruitChere=1;
    private List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotConstruitChere(Pioche pioche){
        super(pioche);
        this.name="BotConstruitChere"+numDuBotConstruitChere;
        numDuBotConstruitChere++;
    }

    // utile pour les tests uniquement
    public void setRolesRestants(List<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
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
    public String toString(){
        return name;
    }


    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;         //on recupere l'or du vol
        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
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


    //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang;
            do{
                rang = randInt(rolesRestants.size());
            }while(rolesRestants.get(rang) instanceof Assassin ); //ne pas prendre l'assassin car on ne peut pas le voler


            affichageJoueur.afficheActionSpecialeVoleur(rolesRestants.get(rang));
            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang =randInt(6) + 2;       // pour un nb aleatoire hors assassin et voleur
            affichageJoueur.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
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
    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        if(rolesRestants.size()>1){
            int rang= randInt(rolesRestants.size());
            affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
            assassin.tuer(rolesRestants.get(rang));

        }
        else{
            int rang = randInt(7)+ 1  ;     // pour un nb aleatoire hors assassin et condottiere prsq on il y est pas dans ma branche
            affichageJoueur.afficheMeurtre(assassin.getRoles().get(rang));
            assassin.tuer(assassin.getRoles().get(rang));
        }
    }
}
