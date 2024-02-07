package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.*;


public abstract class BotMalin extends Bot {

    protected List<Role> rolesRestants;  // garde en memoire les roles suivants pour les voler/assassiner

    protected BotMalin(Pioche pioche){
        super(pioche);
        rolesRestants = new ArrayList<>();
    }


    // utile pour les tests uniquement
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

    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
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



    /**
     * Construit un quartier
     */
    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
    @Override
    public Quartier construire(){
        List<Quartier> quartiersTrie = new ArrayList<>(quartierMain);
        quartiersTrie.sort(Comparator.comparingInt(Quartier::getCout));
        Collections.reverse(quartiersTrie);
        if(!quartiersTrie.isEmpty() && quartiersTrie.get(0).getCout()>=4 && quartiersTrie.get(0).getCout()<=nbOr && !quartierConstruit.contains(quartiersTrie.get(0))){
            Quartier quartierConstruit = quartiersTrie.get(0);
            affichageJoueur.afficheConstruction(quartierConstruit);
            ajoutQuartierConstruit(quartierConstruit);
            return quartierConstruit;
        }
        return null;
    }

    //méthode de originaire de botConstruitChere car utile pour botRichard aussi -> évite doublons
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




    //méthode de originaire de botConstruitVite car utile pour botRichard aussi -> évite doublons
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

    //méthode de originaire de botConstruitVite car utile pour BOTCONSTRUITCHERE aussi -> évite doublons
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


    //méthode de originaire de BOTCONSTRUITCHERE car utile pour FOCUSROI aussi -> évite doublons
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


}

