package Citadelle.teamU.moteurJeu.bots;

import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.cartes.Quartier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BotAleatoire extends Bot{

    private static int numDuBotAleatoire = 1;
    private String name;

    public BotAleatoire(Pioche pioche){
        super(pioche);
        this.name = "BotAleatoire"+numDuBotAleatoire;
        numDuBotAleatoire++;
    }

    //rajouter ou override les methode qui définissent la manière de jouer d'un bot

    /**
     * effectue les actions de base de manière aleatoire
     * @return  liste de quartier piochés puis gardés
     */
    @Override
    public List<Quartier> faireActionDeBase(){
        quartiersViolets();         //actions spéciales violettes
        //une arrayList qui en 0 contient null si le bot prend 2 pieces d'or
        //en indice 0 et 1 les quartiers parmis lesquelles ils choisi
        //en indice 2 le quartier choisi parmis les deux
        List<Quartier> choixDeBase = new ArrayList<>();
        //cree un nombre random soit 0 soit 1, selon le nombre aleatoire choisi, fait une action de base
        int intAleatoire= randInt(2);
        if(intAleatoire == 0){
            // piocher deux quartiers, et en choisir un des deux aléatoirement
            // piocher deux quartiers, quartier1 et quartier 2
            choixDeBase = piocheDeBase();

            choixDeBase.addAll(choisirCarte(new ArrayList<>(choixDeBase)));

        } else {
            choixDeBase.add(null);
            changerOr(2);
        }
        affichageJoueur.afficheChoixDeBase(choixDeBase);
        return choixDeBase;
    }


    @Override
    public void choisirRole(List<Role> roles){
        if (orProchainTour >= 0) nbOr += orProchainTour;         //on recupere l'or du vol
        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
    }

    /**
     * permet de choisir quelle(s) cartes garder après avoir piocher des quartiers
     * @param quartierPioches liste de quartier piochés
     * @return le(s) quartiers gardé(s)
     */
    @Override
    public List<Quartier> choisirCarte(List<Quartier> quartierPioches) {
        if (!quartiersConstruits.contains(Quartier.BIBLIOTHEQUE)){
            int intAleatoire2 = randInt(2); // Choisi un int aléatoire 0 ou 1
            if (intAleatoire2 == 0) {
                ajoutQuartierMain(quartierPioches.get(0));
                pioche.remettreDansPioche(quartierPioches.get(1));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(0)));
            } else {
                ajoutQuartierMain(quartierPioches.get(1));
                pioche.remettreDansPioche(quartierPioches.get(0));
                return new ArrayList<>(Collections.singleton(quartierPioches.get(1)));
            }
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
     * Construit un quartier aléatoire parmis ceux qu'il peut construire
     */
    @Override
    public Quartier construire(){
        List<Quartier> quartiersPossible = new ArrayList<>();
        for(Quartier quartier : quartierMain){
            if(quartier.getCout()<=nbOr  &&  !quartiersConstruits.contains(quartier)){
                quartiersPossible.add(quartier);
            }
        }
        if(!quartiersPossible.isEmpty()){
            int intAleatoire = randInt(quartiersPossible.size());
            Quartier quartierConstruire = quartiersPossible.get(intAleatoire);
            ajoutQuartierConstruit(quartierConstruire);
            affichageJoueur.afficheConstruction(quartierConstruire);
            return quartierConstruire;
        }
        return null;
    }

    /**
     * echange toutes ses cartes aleatoire avec bot ou pioche
     * @param magicien Role magicien
     */
    @Override
    public void actionSpecialeMagicien(Magicien magicien){
        int aleat = randInt(magicien.getBotliste().size() + 1);        // tire un chiffre aleatoire pour 4 bots et la pioche
        while (aleat == magicien.getBotliste().indexOf(this)){          //on l'empêche d'échanger avec lui meme
            aleat = randInt(magicien.getBotliste().size() + 1);
        }
        if(aleat < magicien.getBotliste().size()){                      // aleatoire correspondant à un bot
            affichageJoueur.afficheActionSpecialeMagicienAvecBot(magicien.getBotliste().get(aleat));
            magicien.changeAvecBot(this, magicien.getBotliste().get(aleat));
            affichageJoueur.afficheNouvelleMainMagicien();
        } else {                                                        //aleatoire correspondant à la pioche
            affichageJoueur.afficheActionSpecialeMagicienAvecPioche(this.getQuartierMain());
            magicien.changeAvecPioche(this, this.getQuartierMain());
            affichageJoueur.afficheNouvelleMainMagicien();
        }
    }

    /**
     * tue un role au hasard
     * @param assassin
     */
    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        int rang = randInt(7)+ 1;
        affichageJoueur.afficheMeurtre(assassin.getRoles().get(rang));
        assassin.tuer(assassin.getRoles().get(rang));
    }

    /**
     * assassine aleatoirement
     * @param voleur Role voleur
     */
    @Override
    public void actionSpecialeVoleur(Voleur voleur){
        int rang = randInt(6) + 2;       // pour un nb aleatoire hors assassin et voleur
        affichageJoueur.afficheActionSpecialeVoleur(voleur.getRoles().get(rang));
        voleur.voler(this, voleur.getRoles().get(rang) );
    }


    /**
     * essaye de detruire un quartier aleatoire d'un adversaire aleatoire
     * @param condottiere Role condottiere
     */
    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {
        List<Bot> botList = new ArrayList<>(condottiere.getBotListe());
        botList.remove(this);
        int indiceRandomBot = randInt(botList.size());
        Bot botADetruire = (botList.get(indiceRandomBot));
        if(!botADetruire.getQuartiersConstruits().isEmpty()) {
            int indiceRandomQuartier = randInt(botADetruire.getQuartiersConstruits().size() );
            Quartier quartierAdetruire = botADetruire.getQuartiersConstruits().get(indiceRandomQuartier);

            if (quartierAdetruire!=null && this.getOr() >= quartierAdetruire.getCout() - 1 && !quartierAdetruire.equals(Quartier.DONJON)) {
                condottiere.destructionQuartier(this, botADetruire, quartierAdetruire);
            }
        }
    }
    public void setRolesVisible(List<Role> rolesVisible) {
        this.rolesVisible = rolesVisible;
    }
    @Override
    public String toString(){
        return "Bot aleatoire";
    }
}
