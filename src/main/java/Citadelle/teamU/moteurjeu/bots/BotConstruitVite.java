package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Affichage;
import Citadelle.teamU.moteurjeu.Pioche;

import java.util.*;

public class BotConstruitVite extends Bot {
    private static int numDuBotAleatoire = 1;
    private String name;
    private List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    public BotConstruitVite(Pioche pioche){
        //Bot qui construit le plus vite possible
        //Il construit des qu'il peut (le moins chere)
        //prend des piece si : il a des quartier qui coute moins de 3
        //pioche sinon
        //Si il ne peut pas construire : il pioche jusqu'à avoir des carte qui coute moins de 3
        //Si il a des cartes qui coute moins de 3 : il prend de l'or
        //Il prend l'architecte si possible
        super(pioche);
        this.affichage = new Affichage(this);
        this.name = "Bot_qui_construit_vite" + numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    // utile pour les tests uniquement
    public void setRolesRestants(List<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }


    @Override
    public List<Quartier> faireActionDeBase(){
        quartiersViolets();         //actions spéciales violettes
        //une arrayList qui contient rien si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        //en indice 3 le quartier construit si un quartier a été construit
        List<Quartier> choixDeBase = new ArrayList<>();
        //cherche si il a au moins 1 quartier qu'il a pas deja construit qui coute moins de 3
        boolean aQuartierPasChere = false;
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<4 && !quartierConstruit.contains(quartier)){
                aQuartierPasChere = true;
                break;
            }
        }
        if(aQuartierPasChere){
            choixDeBase.add(null);
            changerOr(2);
        }
        else{
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichage.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }


    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;        //on recupere l'or du vol
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * Construit un quartier
     */

    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()<4 && quartiersTrie.get(0).getCout()<=nbOr && !quartierConstruit.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            ajoutQuartierConstruit(quartierConstruit);
            affichage.afficheConstruction(quartierConstruit);
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
                Collections.reverse(quartierPioches);
                pioche.remettreDansPioche(quartierPioches.remove(0));
                ajoutQuartierMain(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            }
            quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
            Collections.reverse(quartierPioches);
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
            affichage.afficheActionSpecialeMagicienAvecBot(botAvecQuiEchanger);
            magicien.changeAvecBot(this, botAvecQuiEchanger);
            affichage.afficheNouvelleMainMagicien();
        } else {    // sinon on échange toutes ses cartes avec la pioche
            affichage.afficheActionSpecialeMagicienAvecPioche(this.quartierMain);
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichage.afficheNouvelleMainMagicien();
        }
    }

    //A MODIFER QUAND AJOUT CLASSE ASSASSIN, on peut pas tuer l'assassin
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        if (rolesRestants.size() > 1){
            //s'il reste plus d'un role restant c'est qu'il y a au moins un joueur apres nous
            // c.a.d au moins 1 chance sur 2 de voler qq
            int rang = randInt(rolesRestants.size());

            affichage.afficheActionSpecialeVoleur(rolesRestants.get(rang));
            voleur.voler(this, rolesRestants.get(rang));
        }
        else {
            //sinon on fait aleatoire et on croise les doigts
            int rang = randInt(6) +1;       // pour un nb aleatoire hors assassin et voleur
            //+1 pcq le premier c'est voleur et on veut pas le prendre
            affichage.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
            voleur.voler(this, voleur.getRoles().get(rang) );
        }
    }


    @Override
    public String toString(){
        return name;
    }

    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere){
        // detruit que le quartier le moins chère du bot qui a le plus de quartier construits
        ArrayList<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        Bot botMax = botList.get(0);
        for(Bot bot:botList){
            if(botMax.getQuartiersConstruits().size() < bot.getQuartiersConstruits().size()){
                botMax=bot;
            }
        }
        if (botMax.getQuartiersConstruits().isEmpty()){
            return;
        }
        Quartier minPrixQuartier = botMax.getQuartiersConstruits().get(0);
        for(Quartier quartier: botMax.getQuartiersConstruits()){
            if(quartier.getCout() < minPrixQuartier.getCout() && !quartier.equals(Quartier.DONJON)){
                minPrixQuartier = quartier;
            }
        }
        condottiere.destructionQuartier(this,botMax,minPrixQuartier);
    }

}
