package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.*;


public abstract class BotMalin extends Bot {
//bot qui regroupe les methodes communes aux bots intelligents
    protected List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner
    protected boolean changementFocus = false;     // pour la stratégie 2 de botfocusroi et botfocusmarchand

    protected BotMalin(Pioche pioche){
        super(pioche);
        rolesRestants = new ArrayList<>();
    }


    public void setRolesRestants(List<Role> rolesRestants){
        this.rolesRestants = rolesRestants;
    }


    @Override
    public void choisirRole(List<Role> roles){
        role = null;
        if (orProchainTour >= 0) nbOr += orProchainTour;        //on recupere l'or du vol
        int intAleatoire= randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    /**
     * methode pour chercher un role précis et se l'approprier
     * @param roles liste de roles dispos
     * @param roleRecherche String dur role recherché
     * @return true si trouvé, false sinon
     */
    public boolean trouverRole(List<Role> roles, String roleRecherche){
        Optional<Role> roleOptional = roles.stream().filter(role1 -> role1.toString().equals(roleRecherche)).findFirst();
        if (roleOptional.isPresent()){
            int rang = roles.indexOf(roleOptional.get());
            setRole(roles.remove(rang));
            rolesRestants = new ArrayList<>(roles);
            return true;
        }
        return false;
    }

    /**
     * fait action de base selon les regles de botconstruitChere
     * @return liste de cartes piochées et gardées
     */
    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
    @Override
    public List<Quartier> faireActionDeBase() {
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
        } else{
            choixDeBase.add(null);
            changerOr(2);
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }



    /**
     * Construit un quartier selon les règles de botConstruitChere
     */
    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        Collections.reverse(quartiersTrie);
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()>=4 && quartiersTrie.get(0).getCout()<=nbOr && !quartiersConstruits.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            affichageJoueur.afficheConstruction(quartierConstruit);
            ajoutQuartierConstruit(quartierConstruit);
            return quartierConstruit;
        }
        return null;
    }


    /**
     * methode de choix de cartes de botConstruitChere
     * @param quartierPioches liste de Quartiers piochés
     * @return le(s) quartier(s) gardés
     */
    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartiersConstruits.contains(Quartier.BIBLIOTHEQUE)){
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


    /**
     * assassine de manière intelligente mais pas en fonction de l'état de la partie
     * @param assassin Role assassin
     */
    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        if(rolesRestants.size()>1){
            int rang = randInt(rolesRestants.size());
            affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
            assassin.tuer(rolesRestants.get(rang));

        }
        else{
            int rang = randInt(7) + 1  ;     // pour un nb aleatoire hors assassin et condottiere prsq on il y est pas dans ma branche
            affichageJoueur.afficheMeurtre(assassin.getRoles().get(rang));
            assassin.tuer(assassin.getRoles().get(rang));
        }
    }

    /**
     * vole de manière intelligente mais pas en fonction de l'état de la partie
      * @param voleur Role voleur
     */
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

    /**
     * action de magicien de manière intelligente mais pas en fonction de l'état de la partie
     * @param magicien Role magicien
     */
    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int nbQuartierMain = this.getQuartierMain().size();
        Bot botAvecQuiEchanger = null;
        for (Bot botAdverse: magicien.getBotliste()){  //on regarde qui a le plus de cartes dans sa main
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


    /**
     * detruit de manière intelligente mais pas en fonction de l'état de la partie, detruit pour 0 ors
     * @param condottiere Role condottiere
     */
    //méthode de originaire de BOTCONSTRUITCHERE car utile pour FOCUSROI aussi -> évite doublons
    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere){
        // détruit que un quartier qui coute 1
        List<Bot> botList = new ArrayList<>(condottiere.getBotliste());
        botList.remove(this);
        for(Bot bot:botList){
            for(Quartier quartier: bot.getQuartiersConstruits()){
                if(quartier.getCout()==1 && !quartier.equals(Quartier.DONJON) && bot.getQuartiersConstruits().size()<8 && !(bot.getRole() instanceof Pretre)){
                    condottiere.destructionQuartier(this,bot, quartier);
                    return;
                }
            }
        }
    }

    public List<Quartier> suite(List<Quartier> choixDeBase){

        if(changementFocus){
            choixDeBase.add(null);
            changerOr(2);
            affichageJoueur.afficheChoixDeBase(choixDeBase);
            return choixDeBase;

        } else {                            // sinon on pioche
            choixDeBase = piocheDeBase();
            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }

    public List<Quartier> choisirCartePasImportante(List<Quartier> quartierPioches){
        quartierPioches.sort(Comparator.comparingInt(Quartier::getCout));
        Collections.reverse((quartierPioches));
        pioche.remettreDansPioche(quartierPioches.remove(0));
        pioche.remettreDansPioche(quartierPioches.remove(0));
        ajoutQuartierMain(quartierPioches.get(0));
        return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
    }


}

