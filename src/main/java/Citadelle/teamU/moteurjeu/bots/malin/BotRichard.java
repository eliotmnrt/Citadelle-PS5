package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;

import java.util.*;

import static Citadelle.teamU.cartes.TypeQuartier.*;
import static java.util.Arrays.stream;

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

    //Roles restant y'a pas lui même donc dernier si size = 1
    //Voleur avec new Array dans list Role
    //pas prise en compte des deux cartes visibles
    public Role roleProbable(Bot botCible,List<Role> rolesRestants){
        boolean apres = botCible.getOrdreChoixRole() > getOrdreChoixRole();
        HashMap<TypeQuartier,Integer> couleurs = new HashMap<TypeQuartier,Integer>();
        couleurs.put(VERT,0);
        couleurs.put(ROUGE,0);
        couleurs.put(JAUNE,0);
        couleurs.put(BLEUE,0);        //ordre marchant, condottiere, roi, pretre
        HashMap<TypeQuartier,Role> roles = new HashMap<TypeQuartier,Role>();
        roles.put(VERT,new Marchand(role.getBotliste()));
        roles.put(ROUGE,new Condottiere(role.getBotliste()));
        roles.put(JAUNE,new Roi(role.getBotliste()));
        roles.put(BLEUE,new Pretre(role.getBotliste()));
        for(Quartier quartier : botCible.getQuartiersConstruits()){
            for(TypeQuartier type : couleurs.keySet()){
                if(type==quartier.getTypeQuartier()){
                    couleurs.put(type,couleurs.get(type)+1);
                }
            }
        }
        // On sais combien de quartier de chaque couleur il a
        int max = 0;
        Role meilleure=null;
        System.out.println("roles restants : "+rolesRestants);
        for(TypeQuartier i : couleurs.keySet()){
            // On cherche le role liée a la couleur qu'il a le plus ET avec un role dans la liste des roles restants
            // Si il en a 1 c'est pas significatif et on regarde les roles restant que si c'est pas le dernier et que le cible est après nous
            // Ou si il est pas après nous et donc le role n'est pas parmis les autres roles
            if((couleurs.get(i)>max&&couleurs.get(i)>1)&&(
                  rolesRestants.size()<2
                    ||(apres&&hasInstanceOf(rolesRestants,roles.get(i)))
                     ||(!apres&&!hasInstanceOf(rolesRestants,roles.get(i))&&roles.get(i).toString() != role.toString()))){
                max=couleurs.get(i);
                meilleure = roles.get(i);
            }
        }
        if(meilleure != null){
            return meilleure;
        }
        // On est dans la situation où:
        //Il est peut etre dernier
        //Le bot a pas plus de 1 quartier de couleur d'un des roles restants
        if(botCible.getOr()<3){
            // Si il y a un voleur et il a pas bcp d'argent et qu'il a choisi son role après nous
            boolean pasVoleur = true;
            for(Role restant : rolesRestants){
                if(restant instanceof Voleur){
                    pasVoleur = false;
                    if(apres) return restant;
                }
            }
            if(pasVoleur&&!apres){
                //si il est avant nous et qu'on a pas de voleur dans les roles possible
                return new Voleur(role.getBotliste(),new ArrayList<>());
            }
        }
        //Magicien : si qq a peu de carte (<3) dans sa main il a bcp de chance de le prendre
        if(botCible.getQuartierMain().size()<3){
            boolean pasMagicien = true;
            for(Role restant : rolesRestants){
                if(restant instanceof Magicien){
                    pasMagicien = false;
                    if(apres) return restant;
                }
            }
            if(pasMagicien&&!apres){
                //si il est avant nous et qu'on a pas de magicien dans les roles possible
                return new Magicien(role.getBotliste());
            }
        }
        //Architeche : il a bcp de carte en main (a partir de 5)
        if(botCible.getQuartierMain().size()>4){
            boolean pasArchi = true;
            for(Role restant : rolesRestants){
                if(restant instanceof Architecte){
                    pasArchi = false;
                    if(apres) return restant;
                }
            }
            if(pasArchi&&!apres){
                //si il est avant nous et qu'on a pas de magicien dans les roles possible
                return new Architecte(role.getBotliste());
            }
        }
        //Assasin pas de raison particuliere de le chosir

        // Si on a aucune info on fait au hasard parmis les cartes qui reste (si on est pas dernier)
        Random random = new Random();
        int choisi;
        if (rolesRestants.size()<2){
            choisi = random.nextInt(0,rolesRestants.size());
            return rolesRestants.get(choisi);
        }
        ArrayList<Role> listRoleTot = new ArrayList<Role>(Arrays.asList(new Assassin(role.getBotliste(),new ArrayList<>()),new Voleur(role.getBotliste(),new ArrayList<>()),new Magicien(role.getBotliste()),new Roi(role.getBotliste()),new Pretre(role.getBotliste()),new Marchand(role.getBotliste()),new Architecte(role.getBotliste()),new Condottiere(role.getBotliste())));
        choisi = random.nextInt(0,listRoleTot.size());
        while(listRoleTot.get(choisi).toString()==role.toString()){
            choisi = random.nextInt(0,listRoleTot.size());
        }
        return listRoleTot.get(choisi);
    }

    public boolean hasInstanceOf(List<Role> list, Role role){
        for (Role r : list){
            if(r.toString().equals(role.toString())) return true;
        }
        return false;
    }
}
