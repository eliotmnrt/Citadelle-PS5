package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BotRichard extends BotMalin{
//je suis parti du principe que ce bot agit comme botConstruitVite sauf pour les règles demandées

    private static int numDuBot = 1;



    private boolean premierAChoisir = false;
    private boolean joueurAvance = false;
    public BotRichard(Pioche pioche) {
        super(pioche);
        this.name = "Bot_Richard" + numDuBot;
        numDuBot++;
    }

    @Override
    public void choisirRole(List<Role> roles){
        nbTour++;
        isPremierAChoisir(roles);    //si il y a encore 5 roles a piocher c'est que l'on est premier

        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (nbTour>1 && architecteAvance()){

            if (premierAChoisir){
                if (trouverRole(roles, "Assassin")){ //trouverRole chercher le role et le prendre
                    return;
                }
                if (trouverRole(roles, "Architecte")){
                    return;
                }
            }
        } else if (nbTour>1 && joueurAvance()){
            if (trouverRole(roles, "Roi")){
                return;
            }
            if (trouverRole(roles, "Assassin")){
                return;
            }
            if (trouverRole(roles, "Condottiere")){
                return;
            }
            if (trouverRole(roles, "Pretre")){
                return;
            }
        }
        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }

    public boolean isPremierAChoisir(List<Role> roles){
        return premierAChoisir = roles.size() == 5;
    }

    public boolean getPremierAChoisir() {
        return premierAChoisir;
    }
    private boolean architecteAvance(){
        List<Bot> list = new ArrayList<>(role.getBotliste());
        list.remove(this);
        return list.stream().anyMatch(bot -> bot.getOr()>=4 && !bot.getQuartierMain().isEmpty() && bot.getQuartiersConstruits().size()==5);
    }
    private boolean joueurAvance(){
        List<Bot> list = new ArrayList<>(role.getBotliste());
        list.remove(this);
        joueurAvance = list.stream().anyMatch(bot -> bot.getQuartiersConstruits().size()==6);
        return joueurAvance;
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
