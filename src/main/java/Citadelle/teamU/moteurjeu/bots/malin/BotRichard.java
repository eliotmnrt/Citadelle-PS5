package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BotRichard extends BotMalin{
//je suis parti du principe que ce bot agit comme botConstruitVite sauf pour les règles demandées

    private static int numDuBot = 1;



    private boolean premierAChoisir = false;
    private boolean joueurAvance = false;
    private boolean assassinerMagicien = false;
    private boolean joueurProcheFinir=false;
    public BotRichard(Pioche pioche) {
        super(pioche);
        this.name = "Bot_Richard" + numDuBot;
        numDuBot++;
    }

    @Override
    public void choisirRole(List<Role> roles) {

        nbTour++;
        List<Bot> joueursProchesDeFinirList = getJoueursProcheFinir(); //avec 7 quartiers construit
        System.out.println("Joueurs proches de finir:" + joueursProchesDeFinirList);

        assassinerMagicien = false;

        isPremierAChoisir(roles);    //si il y a encore 5 roles a piocher c'est que l'on est premier

        if (orProchainTour >= 0) nbOr += orProchainTour;

        if (nbTour>1 && architecteAvance()){

            System.out.println("je suis la");
            if (premierAChoisir){
                if (trouverRole(roles, "Assassin")){ //trouverRole chercher le role et le prendre
                    System.out.println("Assassin");
                    return;
                }
                if (trouverRole(roles, "Architecte")){
                    System.out.println("Architecte");
                    return;
                }
            }
        } else if (nbTour > 1 && joueurAvance()) {
            if (trouverRole(roles, "Roi")) {
                return;
            }
            if (trouverRole(roles, "Assassin")) {
                return;
            }
            if (trouverRole(roles, "Condottiere")) {
                return;
            }
            if (trouverRole(roles, "Pretre")) {
                return;
            }
        }

        /*
         Si le joueur en passe de gagner est 1er ou 2ème joueur, il n’y a pas grand-chose à faire car il va choisir : l’Assassin s’il est disponible (seule perso intouchable) ou l’Evêque ou le Condottiere
         afin de construire son dernier quartier sans craindre le Condottiere.
         */
        else if (nbTour > 1 && joueurProcheFinir) {
            ordreChoix = getOrdreChoixRole(roles);
            /*
             Dans l e meilleur des cas, il est 2ème joueur et il manque l’Evêque ou Condottiere : le premier joueur doit
              prendre l’Assassin et tuer l’Evêque ou Condottiere.
            */
            if (joueursProchesDeFinirList.contains(this)) {
                if (this.ordreChoix == 2) {
                    if (!(trouverRole(roles, "Pretre") && trouverRole(roles, "Condottiere"))) {
                        trouverRole(roles, "Assassin");
                        return;
                        //pour tuer l'eveque ou le condottiere
                    }
                }
                if ((this.ordreChoix == 1 || this.ordreChoix == 2)) {
                    if (trouverRole(roles, "Assassin")) { //trouverRole chercher le role et le prendre
                        return;
                    }
                    if (trouverRole(roles, "Pretre")) { // j'ai privilégié le pretre au condott parce qu'il joue avant
                        return;
                    }
                    if (trouverRole(roles, "Condottiere")) {
                        return;
                    }
                }
            }
            /*else{ //il s'agit d'un autre joueur en passe de gagner
                for(Bot bot: joueursProchesDeFinirList){
                    if(bot.getOrdreChoixRole()==3){
                        if(this.getOrdreChoixRole(){

                        }
                    }
                }

            }
        }*/
        }
            //si on a bcp de cartes et que les autres non, on tue l'assassin?????
            if (nbTour > 1 && quartierMain.size() >= 4) {
                List<Bot> list = new ArrayList<>(role.getBotliste());
                list.remove(this);
                if (list.stream().allMatch(bot -> bot.getQuartierMain().size() <= 2) && (trouverRole(roles, "Assassin"))) {
                    assassinerMagicien = true;
                    return;
                }
            }


            //sinon aleatoire
            int intAleatoire = randInt(roles.size());
            Role rolechoisi = roles.remove(intAleatoire);
            setRole(rolechoisi);
            rolesRestants = new ArrayList<>(roles);

    }


    public boolean isPremierAChoisir(List<Role> roles){
        return premierAChoisir = roles.size() == 5;
    }

    public boolean getPremierAChoisir() {
        return premierAChoisir;
    }
    public boolean architecteAvance(){
        //joueur avance (meets conditions en dessous) et peut gagner en prenant l'architecte
        List<Bot> list = new ArrayList<>(role.getBotliste());
        list.remove(this);
        return list.stream().anyMatch(bot -> bot.getOr()>=4 && !bot.getQuartierMain().isEmpty() && bot.getQuartiersConstruits().size()==5);
    }
    public boolean joueurAvance(){
        //joueur qui a 6 quartiers construits
        List<Bot> list = new ArrayList<>(role.getBotliste());
        list.remove(this);
        joueurAvance = list.stream().anyMatch(bot -> bot.getQuartiersConstruits().size()==6);
        return joueurAvance;
    }
    public boolean joueurProcheFinir(){
        //joueur qui a 7 quartiers construit
        List<Bot> list = new ArrayList<>(role.getBotliste());
        joueurProcheFinir=list.stream().anyMatch(bot -> bot.getQuartiersConstruits().size()==7);
        return joueurProcheFinir;
    }
    public List<Bot> getJoueursProcheFinir(){
        List<Bot> bots= new ArrayList<>();
        if (joueurProcheFinir){
            List<Bot> list = new ArrayList<>(role.getBotliste());
            bots=list.stream().filter(bot -> bot.getQuartiersConstruits().size() == 7).collect(Collectors.toList());
        }
        return bots;
    }


    @Override
    public void actionSpecialeAssassin(Assassin assassin){
        //si un joueur menace de finir
        if (joueurAvance){
            Optional<Role> roleRoi = assassin.getRoles().stream().filter(Roi.class::isInstance).findFirst();
            roleRoi.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
            roleRoi.ifPresent(assassin::tuer);
            return;
        }

        //si un joueur menace de finir en 1 tour avec l'architecte
        if (premierAChoisir){
            Optional<Role> roleArchi = assassin.getRoles().stream().filter(Architecte.class::isInstance).findFirst();
            roleArchi.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
            roleArchi.ifPresent(assassin::tuer); //assassin.tuer(roleArchi)
            return;
        }

        //si on a bientot fini on tue le condottiere pour éviter la destruction de nos quartiers
        if (this.getQuartiersConstruits().size() >= 6){
            Optional<Role> roleCondott = assassin.getRoles().stream().filter(Condottiere.class::isInstance).findFirst();
            roleCondott.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
            roleCondott.ifPresent(assassin::tuer);
            return;
        }

        //si on a bcp de cartes et pas, les autres, on tue le magicien
        if (assassinerMagicien){
            Optional<Role> roleMagicien = assassin.getRoles().stream().filter(Magicien.class::isInstance).findFirst();
            roleMagicien.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
            roleMagicien.ifPresent(assassin::tuer);
        }

        //si un bot est très riche on tue le voleur
        Optional<Bot> botRiche = assassin.getBotliste().stream().filter(bot -> bot.getOr() > 7).findAny();
        if (botRiche.isPresent()){
            affichageJoueur.afficheMeurtre(botRiche.get().getRole());
            assassin.tuer(botRiche.get().getRole());
            return;
        }


        //sinon on tue n'importe qui sauf voleur et condott
        int rang;
        int comp = 0;
        do{
            comp++;
            rang = randInt(rolesRestants.size());
            if(comp >= 20) break;       //pour sortir boucle si uniquement voleur ou archi dans rolesrestants
        }while(rolesRestants.get(rang) instanceof Voleur || rolesRestants.get(rang) instanceof Condottiere);

        affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
        assassin.tuer(rolesRestants.get(rang));

    }

    @Override
    public void actionSpecialeMagicien(Magicien magicien) {
        //on échange ses cartes avec le joueur avancé ssi peu de cartes et aucune pas chere
        if (joueurAvance && quartierMain.size()<=3 && quartierMain.stream().allMatch(quartier -> quartier.getCout() >=3)){
            List<Bot> list = new ArrayList<>(magicien.getBotliste());
            list.remove(this);
            Optional<Bot> optionalBot = list.stream().max(Comparator.comparingInt(Bot::getNbQuartiersConstruits));
            optionalBot.ifPresentOrElse(affichageJoueur::afficheActionSpecialeMagicienAvecBot, () -> {throw new IllegalArgumentException();});
            optionalBot.ifPresent(bot -> magicien.changeAvecBot(this, bot));
            optionalBot.ifPresent(bot -> affichageJoueur.afficheNouvelleMainMagicien());
        }
    }

    @Override
    public void actionSpecialeCondottiere(Condottiere condottiere) {
        if (joueurAvance) {
            List<Bot> list = new ArrayList<>(condottiere.getBotliste());
            list.remove(this);
            Optional<Bot> optionalBot = list.stream().max(Comparator.comparingInt(Bot::getNbQuartiersConstruits));
            if (optionalBot.isPresent()) {
                Optional<Quartier> quartierMin = optionalBot.get().getQuartiersConstruits().stream().min(Comparator.comparingInt(Quartier::getCout));
                if (quartierMin.isPresent() && quartierMin.get().getCout()-1 <= nbOr){
                    condottiere.destructionQuartier(this, optionalBot.get(), quartierMin.get());
                }
                return;
            } else {
                throw new IllegalArgumentException();
            }
        }
        super.actionSpecialeCondottiere(condottiere);
    }
}
