package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.TypeQuartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import java.util.*;

import static Citadelle.teamU.cartes.TypeQuartier.*;
import static java.util.Arrays.stream;

public class BotRichard extends BotMalin{
    //je suis parti du principe que ce bot agit comme botConstruitChere sauf pour les règles demandées
    private static int numDuBot = 1;
    private boolean joueurProcheFinir=false;
    int bcpCartes=4;
    int bcpArgent=8;
    private boolean joueurAvance = false;
    public BotRichard(Pioche pioche) {
        super(pioche);
        this.name = "Bot Richard_" + numDuBot;
        numDuBot++;
    }

    /**
     * decide de quel role prendre
     * @param roles liste de roles disponibles
     */
    @Override
    public void choisirRole(List<Role> roles){
        nbTour++;
        int ordreChoix=getOrdreChoixRole();

        if (orProchainTour >= 0) nbOr += orProchainTour;
        if (nbTour>1) {

            if (architecteAvance()) {

                if (ordreChoix == 1) {
                    if (trouverRole(roles, "Assassin")) { //trouverRole chercher le role et le prendre
                        return;
                    }
                    if (trouverRole(roles, "Architecte")) {
                        return;
                    }
                }
            } else if (joueurAvance()) {
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

                joueurProcheFinir=joueurProcheFinir();

                Bot botProcheDeFinir = this.getJoueurProcheFinir();//avec 7 quartiers construit

                if (joueurProcheFinir) {
            /*
             Dans le meilleur des cas, il est 2ème joueur et il manque l’Evêque ou Condottiere : le premier joueur doit
              prendre l’Assassin et tuer l’Evêque ou Condottiere.
            */
                    if (botProcheDeFinir.equals(this)){
                        if ((ordreChoix == 1 || ordreChoix == 2)) {
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
                    else { //il s'agit d'un autre joueur en passe de gagner
                        Bot bot = botProcheDeFinir;
                    /*
                     Si le joueur en passe de gagner est 3ème joueur, les deux premiers joueurs doivent jouer la combo !
                      Si le joueur en passe de gagner est 4ème joueur (ou plus), les premiers joueurs doivent jouer la combo dans le même esprit que les cas précédents. Mais avec plus de chance de succès.
                        L’idée est d’essayer de tuer le joueur en passe de gagner et(ou) de lui détruire un quartier.
                    */
                        if ((bot.getOrdreChoixRole() == 2) && trouverRole(roles, "Assassin")) {
                            return;
                        }
                        if (bot.getOrdreChoixRole() == 3 || bot.getOrdreChoixRole() == 4) {
                            if (hasInstanceOf(roles, new Assassin(role.getBotliste(), roles)) && hasInstanceOf(roles, new Condottiere(role.getBotliste())) && hasInstanceOf(roles, new Pretre(role.getBotliste()))) {
                        /* 1er cas.
 Il y a : Assassin, Evêque et Condottiere.
 Le premier à choisir ne doit pas prendre l’Assassin, il prend le Condottiere…
 Le deuxième prend l’Assassin et tue l’Evêque.*/
                                if (ordreChoix == 1) {
                                    trouverRole(roles, "Condottiere");
                                    return;
                                }
                            }
                            if (hasInstanceOf(roles, new Assassin(role.getBotliste(), roles)) && hasInstanceOf(roles, new Pretre(role.getBotliste()))) {
                                if (ordreChoix == 2) {
                                    trouverRole(roles, "Assassin");
                                    return;
                                }
                            }
                            /*2ème cas.
                        Il manque : l’Evêque
                        Le premier prend l’Assassin et tue qui il veut sauf le Condottiere.
                                Le deuxième prend le Condottiere…*/
                            if (hasInstanceOf(roles, new Assassin(role.getBotliste(), roles)) && hasInstanceOf(roles, new Condottiere(role.getBotliste()))) {
                                if (ordreChoix == 1) {
                                    trouverRole(roles, "Assassin"); //et ne tue pas le condottiere
                                    return;
                                }
                            }
                            if (ordreChoix == 2 && trouverRole(roles, "Condottiere")) {
                                return;
                            }
                            if (hasInstanceOf(roles, new Assassin(role.getBotliste(), roles)) && hasInstanceOf(roles, new Pretre(role.getBotliste()))) {
                        /*  3ème cas.
 Il manque : Le Condottiere
 Le premier prend l’Assassin et tue le Magicien, si le 2ème joueur a beaucoup de cartes et le joueur en position de gagner en a aucune, sinon il tue qui il veut.
 Le deuxième prend le Magicien (s'il n'a pas trop de cartes) et prends celles du joueur en position de gagner. */
                                if (ordreChoix == 1) {
                                    trouverRole(roles, "Assassin"); //tue le magicien
                                    return;
                                }
                            }
                            if (ordreChoix == 2) {

                                if (this.getQuartierMain().size() < bcpCartes) {
                                    trouverRole(roles, "Magicien"); //Le deuxième prend le Magicien (s'il n'a pas trop de cartes) et prends celles du joueur en position de gagner.
                                    return;
                                }
                            }
                            if (hasInstanceOf(roles, new Condottiere(role.getBotliste())) && hasInstanceOf(roles, new Pretre(role.getBotliste()))) {
                        /*   4ème cas.
                         Il n’y pas l’Assassin
                         Le premier prend le Condottiere.
                         Le deuxième prend l’Evêque. */
                                if (ordreChoix == 1 ) {

                                    trouverRole(roles, "Condottiere"); //detruit un quartier du joueur en passe en gagner
                                    return;
                                }
                            }
                            if (ordreChoix == 2 && trouverRole(roles, "Pretre")) {
                                return;
                            }
                        }
                    }
                }

        }

        int intAleatoire = randInt(roles.size());
        setRole(roles.remove(intAleatoire));
        rolesRestants = new ArrayList<>(roles);
    }



    /**
     * check si un joueur menace de finir en un tour avec architecte
     * @return true, false sinon
     */
    public boolean architecteAvance(){
        List<Bot> list = new ArrayList<>(role.getBotliste());
        list.remove(this);
        return list.stream().anyMatch(bot -> bot.getOr()>=4 && !bot.getQuartierMain().isEmpty() && bot.getQuartiersConstruits().size()==5);
    }

    /**
     * check si un joueur à 6 quartiers
     * @return true, false sinon
     */
    public boolean joueurAvance(){
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
    public Bot getJoueurProcheFinir(){
        if (joueurProcheFinir){
            List<Bot> list = new ArrayList<>(role.getBotliste());
            for(Bot bot:list){
                if (bot.getQuartiersConstruits().size()==7) return bot;
            }
        }
        return null;
    }
    //pour les tests uniquement
    public void setJoueurAvance(boolean joueurAvance) {this.joueurAvance = joueurAvance;}

    /**
     * assassine en fonction de l'état de la partie
     * @param assassin Role Assassin
     */
    @Override
    public void actionSpecialeAssassin(Assassin assassin) {
        //si un joueur menace de finir
        if (joueurAvance) {

            List<Bot> list = new ArrayList<>(assassin.getBotliste());
            list.remove(this);
            Optional<Bot> optionalBot = list.stream().max(Comparator.comparingInt(Bot::getNbQuartiersConstruits));
            Role roleBotVise = null;
            if (optionalBot.isPresent()) {
                roleBotVise = roleProbable(optionalBot.get());
            } else {
                throw new IllegalArgumentException();
            }
            affichageJoueur.afficheMeurtre(roleBotVise);
            assassin.tuer(roleBotVise);
            return;
        }

        //si un joueur menace de finir en 1 tour avec l'architecte

        if (joueurProcheFinir) {
            Bot botProcheFinir = getJoueurProcheFinir();
                /*
                 cas ou ou le joueur en passe de finir est 2ème joueur et il manque l’Evêque ou Condottiere :
                 le premier joueur doit prendre l’Assassin et tuer l’Evêque ou Condottiere.
                */

            if (!botProcheFinir.equals(this) && botProcheFinir.getOrdreChoixRole() == 2 && getOrdreChoixRole() == 1) {

                Optional<Role> rolePretre = rolesRestants.stream().filter(Pretre.class::isInstance).findFirst();
                Optional<Role> roleCondott = rolesRestants.stream().filter(Condottiere.class::isInstance).findFirst();

                if (rolePretre.isPresent() ^ roleCondott.isPresent()) {

                    rolePretre.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
                    rolePretre.ifPresent(assassin::tuer);
                    roleCondott.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
                    roleCondott.ifPresent(assassin::tuer);
                    return;
                }
            }


            if (!botProcheFinir.equals(this) && botProcheFinir.getOrdreChoixRole() == 3) {

                    /*
                     Si le joueur en passe de gagner est 3ème joueur, les deux premiers joueurs doivent jouer la combo
                     1er cas:
                     Il y a : Assassin, Evêque et Condottiere
                     Le premier à choisir ne doit pas prendre l’Assassin, il prend le Condottiere…
                     Le deuxième prend l’Assassin et tue l’Evêque.
                    */

                Optional<Role> rolePretre = rolesRestants.stream().filter(Pretre.class::isInstance).findFirst();
                Optional<Role> roleCondott = rolesRestants.stream().filter(Condottiere.class::isInstance).findFirst();


                if (rolePretre.isPresent() && this.getOrdreChoixRole() == 2) {

                    rolePretre.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
                    rolePretre.ifPresent(assassin::tuer);
                    return;
                }
                    /*
                     2ème cas.
                    Il manque : l’Evêque
                    Le premier prend l’Assassin et tue qui il veut sauf le Condottiere.
                            */
                if (roleCondott.isPresent() && !rolePretre.isPresent() && this.getOrdreChoixRole() == 1) {
                    int rang;
                    do {
                        rang = randInt(rolesRestants.size());
                    } while (rolesRestants.get(rang) instanceof Condottiere);
                    affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
                    assassin.tuer(rolesRestants.get(rang));
                    //roleCondott.ifPresent(assassin::tuer);
                    return;
                }
                Optional<Bot> bot2 = assassin.getBotliste().stream().filter(bot -> bot.getOrdreChoixRole() == 2).findFirst();

                if (rolePretre.isPresent() && this.getOrdreChoixRole() == 1) {

                         /*
                    3ème cas.
 Il manque : Le Condottiere
 Le premier prend l’Assassin et tue le Magicien, si le 2ème joueur a beaucoup de
 cartes et le joueur en position de gagner en a aucune, sinon il tue qui il veut.
                            */
                    if (bot2.isPresent()) {

                        if (botProcheFinir.getQuartierMain().size() == 0 && bot2.get().getQuartierMain().size() >= bcpCartes) {
                            Optional<Role> roleMagicien = rolesRestants.stream().filter(Magicien.class::isInstance).findFirst();
                            roleMagicien.ifPresent(value -> affichageJoueur.afficheMeurtre(value));
                            roleMagicien.ifPresent(assassin::tuer);
                            return;
                        }
                    }
                }
            }
        }
            if (getOrdreChoixRole() == 1){

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
            if (rolesRestants.size() > 1){
                do{
                    comp++;
                    rang = randInt(rolesRestants.size());
                    if(comp >= 20) break;       //pour sortir boucle si uniquement voleur ou archi dans rolesrestants
                }while(rolesRestants.get(rang) instanceof Voleur || rolesRestants.get(rang) instanceof Condottiere);

                affichageJoueur.afficheMeurtre(rolesRestants.get(rang));
                assassin.tuer(rolesRestants.get(rang));
            } else {

                rang = randInt(7) + 1;
                affichageJoueur.afficheMeurtre(assassin.getRoles().get(rang));
                assassin.tuer(assassin.getRoles().get(rang));
            }



        }

        /**
         * assassine en fonction de l'état de la partie
         * @param magicien Role magicien
         */
        @Override
        public void actionSpecialeMagicien(Magicien magicien) {
            //on échange ses cartes avec le joueur avancé ssi peu de cartes et aucune pas chere
            if (joueurAvance && quartierMain.size()<bcpCartes && quartierMain.stream().allMatch(quartier -> quartier.getCout() >=3)){
                List<Bot> list = new ArrayList<>(magicien.getBotliste());
                list.remove(this);
                Optional<Bot> optionalBot = list.stream().max(Comparator.comparingInt(Bot::getNbQuartiersConstruits));
                optionalBot.ifPresentOrElse(affichageJoueur::afficheActionSpecialeMagicienAvecBot, () -> {throw new IllegalArgumentException();});
                optionalBot.ifPresent(bot -> magicien.changeAvecBot(this, bot));
                optionalBot.ifPresent(bot -> affichageJoueur.afficheNouvelleMainMagicien());
                return;
            }
            joueurProcheFinir=joueurProcheFinir();
            if (joueurProcheFinir) {
                Bot botProcheFinir = getJoueurProcheFinir();

                if (botProcheFinir.getOrdreChoixRole() == 3 && this.getOrdreChoixRole() == 2 && this.getQuartierMain().size() < bcpCartes ) {
                    magicien.changeAvecBot(this, botProcheFinir);
                    affichageJoueur.afficheNouvelleMainMagicien();
                    return;
                }
            }
            super.actionSpecialeMagicien(magicien);
        }

        /**
         * assassine en fonction de l'état de la partie
         * @param condottiere Role condottiere
         */
        @Override
        public void actionSpecialeCondottiere(Condottiere condottiere) {
            if (joueurAvance) {
                List<Bot> list = new ArrayList<>(condottiere.getBotliste());
                list.remove(this);
                Optional<Bot> optionalBot = list.stream().max(Comparator.comparingInt(Bot::getNbQuartiersConstruits));
                if (optionalBot.isPresent()) {
                    Optional<Quartier> quartierMin = optionalBot.get().getQuartiersConstruits().stream().min(Comparator.comparingInt(Quartier::getCout));
                    if (quartierMin.isPresent() && quartierMin.get().getCout()-1 <= nbOr && optionalBot.get().getQuartiersConstruits().size() < 8 &&!(optionalBot.get().getRole() instanceof Pretre)){
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
        //prise en compte des deux cartes visibles
        // return un role mais pas le "vrai" role c'est pas la bonne reference
        public Role roleProbable(Bot botCible){
            boolean apres = botCible.getOrdreChoixRole() > getOrdreChoixRole();
            //Si il a construit 7 quartiers il surement prend l'assassin si y'a pas il va prendre pretre
            if(botCible.getQuartiersConstruits().size()==7){
                if(apres){
                    if(hasInstanceOf(rolesRestants,new Assassin(role.getBotliste(),new ArrayList<>()))) return new Assassin(role.getBotliste(),new ArrayList<>());
                    if(hasInstanceOf(rolesRestants,new Pretre(role.getBotliste()))) return new Pretre(role.getBotliste());
                }else{
                    if(!hasInstanceOf(rolesRestants,new Assassin(role.getBotliste(),new ArrayList<>()))&&!hasInstanceOf(rolesVisible,new Assassin(role.getBotliste(),new ArrayList<>()))) return new Assassin(role.getBotliste(),new ArrayList<>());
                    if(!hasInstanceOf(rolesRestants,new Pretre(role.getBotliste()))&&!hasInstanceOf(rolesVisible,new Pretre(role.getBotliste()))) return new Pretre(role.getBotliste());
                }
            }
            if(joueurAvance){
                if(apres){
                    if(hasInstanceOf(rolesRestants,new Roi(role.getBotliste()))) return new Roi(role.getBotliste());
                }else{
                    if(!hasInstanceOf(rolesRestants,new Roi(role.getBotliste()))&&!hasInstanceOf(rolesVisible,new Roi(role.getBotliste()))) return new Roi(role.getBotliste());
                }
            }


            HashMap<TypeQuartier,Integer> couleurs = new HashMap<>();
            couleurs.put(VERT,0);
            couleurs.put(ROUGE,0);
            couleurs.put(JAUNE,0);
            couleurs.put(BLEUE,0);        //ordre marchand, condottiere, roi, pretre
            HashMap<TypeQuartier,Role> roles = new HashMap<>();
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
            for(TypeQuartier i : couleurs.keySet()){
                // Le role n'est pas dans les roles visible
                // Si le botCible est après nous on verifie que le role est dans ceux qu'on a eu dans nos mains
                // Inversement faut qu'il soit pas dans les roles qu'on a vu si il est avant nous
                // Parmis les roles qui valide ces conditions on garde celui qui a la couleur de quartier que le botCible a le plus
                if(!hasInstanceOf(rolesVisible,roles.get(i))&& (couleurs.get(i)>max&&couleurs.get(i)>1)&&(
                        rolesRestants.size()<2
                                ||(apres&&hasInstanceOf(rolesRestants,roles.get(i)))
                                ||(!apres&&!hasInstanceOf(rolesRestants,roles.get(i))&&!roles.get(i).toString().equals(role.toString())))){
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
            if(botCible.getOr()<3&&!hasInstanceOf(rolesVisible,new Voleur(role.getBotliste(),new ArrayList<>()))){
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
            if(botCible.getQuartierMain().size()<3&&!hasInstanceOf(rolesVisible,new Magicien(role.getBotliste()))){
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
            //Architecte : il a bcp de carte en main (a partir de 5)
            if(botCible.getQuartierMain().size()>4&&!hasInstanceOf(rolesVisible,new Architecte(role.getBotliste()))){
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
            int choisi;
            if (apres){
                choisi = randInt(rolesRestants.size());
                return rolesRestants.get(choisi);
            }
            ArrayList<Role> listRoleReste = new ArrayList<Role>(Arrays.asList(new Assassin(role.getBotliste(),new ArrayList<>()),new Voleur(role.getBotliste(),new ArrayList<>()),new Magicien(role.getBotliste()),new Roi(role.getBotliste()),new Pretre(role.getBotliste()),new Marchand(role.getBotliste()),new Architecte(role.getBotliste()),new Condottiere(role.getBotliste())));
            ArrayList<Role> toRemove = new ArrayList<>();
            for(Role role : listRoleReste){
                if(role.toString().equals(this.getRole().toString())) toRemove.add(role);
                if(role.toString().equals(rolesVisible.get(0).toString())) toRemove.add(role);
                if(role.toString().equals(rolesVisible.get(1).toString())) toRemove.add(role);
                for(Role roleApres : rolesRestants){
                    if(roleApres.toString().equals(role.toString())) toRemove.add(role);
                }
            }
            for(Role role : toRemove){
                listRoleReste.remove(role);
            }
            listRoleReste.removeAll(toRemove);
            choisi = randInt(listRoleReste.size());
            return listRoleReste.get(choisi);
        }

        public boolean hasInstanceOf(List<Role> list, Role role){
            for (Role r : list){
                if(r.toString().equals(role.toString())) return true;
            }
            return false;
        }
        public void setRolesVisible(List<Role> rolesVisible) {
            this.rolesVisible = rolesVisible;
        }
        @Override
        public String toString(){
            return name;
        }
    }
