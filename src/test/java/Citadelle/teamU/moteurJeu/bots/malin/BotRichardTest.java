package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.*;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Tour;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.Test;

import Citadelle.teamU.moteurJeu.Pioche;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BotRichardTest {
    BotRichard bot;
    BotRichard bot2;
    Pioche pioche;

    List<Bot> botliste;
    Bot botAleatoire, botConstruitChere, botFocusRoi;
    Tour tour;
    List<Role> roles;
    Assassin assassin;
    Roi roi;

    @BeforeEach
    public void setBot() {
        pioche = spy(new Pioche());
        bot = spy(new BotRichard(pioche));
        botliste = new ArrayList<>();
        tour = new Tour(botliste);
        botAleatoire = new BotAleatoire(pioche);
        botConstruitChere = new BotConstruitChere(pioche);
        botFocusRoi = new BotFocusRoi(pioche);
        botliste.add(bot);
        botliste.add(botAleatoire);
        botliste.add(botConstruitChere);
        botliste.add(botFocusRoi);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Assassin(botliste,new ArrayList<>()),new Pretre(botliste))));

        roles = new ArrayList<>();
        assassin = spy(new Assassin(botliste, roles));
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roi = new Roi(botliste);
        roles.add(roi);
        roles.add(new Pretre(botliste));
        roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(new Condottiere(botliste));
    }

        @Test
        void roleProbableTestCouleursApres(){
            doReturn(1).when(bot).randInt(anyInt());
            bot.setOrdreChoixRole(1);
            botAleatoire.setOrdreChoixRole(2);
            bot.setRole(new Marchand(botliste));
            List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Roi(botliste),new Pretre(botliste),new Condottiere(botliste)));
            botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE)));
            //chateau, palais et manoir sont jaune et marche rouge
            //Donc il doit viser le Roi car il est dans les roles restant et il a 3 quartier Jaune
            botAleatoire.setRole(new Roi(botliste));
            bot.setRolesRestants(rolesRestant);
            Role aViser = bot.roleProbable(botAleatoire);
            assertTrue(aViser instanceof Roi);

            //Il est dans les roles d'après
            botAleatoire.setQuartiersConstruits(new ArrayList<>(List.of(Quartier.CHATEAU)));
            bot.setRolesRestants(rolesRestant);
            aViser = bot.roleProbable(botAleatoire);
            assertTrue(aViser instanceof Pretre);
        }
    @Test
    void roleProbableTestCouleursAvant(){
        doReturn(1).when(bot).randInt(anyInt());
        bot.setRole(new Marchand(botliste));
        botAleatoire.changerOr(20);
        bot.setOrdreChoixRole(3);
        botAleatoire.setOrdreChoixRole(1);
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Architecte(botliste),new Condottiere(botliste)));
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE)));
        //chateau, palais et manoir sont jaune et marche, forteresse rouge
        //Donc il doit viser le Roi car il n'est pas dans les roles restant et il a 3 quartiers Jaune
        botAleatoire.setRole(new Roi(botliste));
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Roi);

        botAleatoire.setQuartiersConstruits(new ArrayList<>(List.of(Quartier.CHATEAU)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Magicien);

        //Bcp de quartier Rouge mais Condottiere après lui alors que il est avant lui dans l'ordre
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.TERRAIN_DE_BATAILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        rolesRestant = new ArrayList<>(Arrays.asList(new Voleur(botliste,new ArrayList<>()),new Roi(botliste),new Condottiere(botliste)));
        botAleatoire.setRole(new Magicien(botliste));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Architecte);
    }
    @Test
    void roleProbableVoleurApres(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Voleur(botliste,new ArrayList<>()));
        botAleatoire.changerOr(-1); //On a 1 or
        assertEquals(1,botAleatoire.getOr());
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Voleur(botliste,new ArrayList<>())));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);

        //Le voleur est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Voleur);

        //Le voleur n'est pas dans les roles restants et est dans les roles visibles
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Pretre(botliste),new Voleur(botliste,new ArrayList<>()))));
        aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Voleur);
    }

    @Test
    void roleProbableVoleurAvant(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Voleur(botliste,new ArrayList<>()));
        botAleatoire.changerOr(-1); //On a 1 or
        assertEquals(1,botAleatoire.getOr());
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Voleur(botliste,new ArrayList<>())));
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);

        //Le voleur est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Voleur);

        //Le voleur n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Voleur);
    }
    @Test
    void roleProbableMagicienApres(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Magicien(botliste));
        botAleatoire.changerOr(40);
        botAleatoire.setQuartierMain(List.of(Quartier.CHATEAU));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Magicien(botliste)));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);

        //Le Magicien est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Magicien);

        //Le Magicien n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Magicien);
    }

    @Test
    void roleProbableMagicienAvant(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Magicien(botliste));
        botAleatoire.changerOr(40);
        botAleatoire.setQuartierMain(List.of(Quartier.CHATEAU));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Magicien(botliste)));
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);

        //Le Magicien est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Magicien);

        //Le Magicien n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Magicien);
    }
    @Test
    void roleProbableArchiApres(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Architecte(botliste));
        botAleatoire.changerOr(40);
        botAleatoire.setQuartierMain(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Architecte(botliste)));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);

        //L'archi est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Architecte);

        //L'archi n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Architecte);
    }

    @Test
    void roleProbableArchiAvant(){
        bot.setRole(new Roi(botliste));
        botAleatoire.setRole(new Architecte(botliste));
        botAleatoire.changerOr(40);
        botAleatoire.setQuartierMain(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Architecte(botliste)));
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);

        //L'archi est dans les roles restants
        bot.setRolesRestants(rolesRestant);
        Role aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Architecte);

        //L'archi n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Architecte);
    }
    @Test
    void roleProbableAssassinApres(){
        bot.setOrdreChoixRole(1);
        bot.setRole(new Magicien(botliste));
        botAleatoire.setOrdreChoixRole(2);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Roi(botliste),new Pretre(botliste))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Assassin(botliste,new ArrayList<>()),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE,Quartier.PRISON)));
        //Il a 7 quartiers donc il va choisir assasin vu qu'il est aprés et dans les roles restants
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Assassin);

        //Il en plus que 6 il ne doit pas prendre l'assassin
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Assassin);
    }
    @Test
    void roleProbableAssassinAvant(){
        doReturn(1).when(bot).randInt(anyInt());
        bot.setOrdreChoixRole(2);
        bot.setRole(new Magicien(botliste));
        botAleatoire.setOrdreChoixRole(1);
        botAleatoire.changerOr(20);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Roi(botliste),new Pretre(botliste))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Marchand(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE,Quartier.PRISON)));
        //Il a 7 quartiers donc il va choisir assasin vu qu'il est avant lui et il n'est pas dans les roles restants ni dans les roles visibles
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Assassin);

        //Il en plus que 6 il ne doit pas prendre l'assassin
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Voleur);
    }
    @Test
    void roleProbablePretreApres(){
        bot.setOrdreChoixRole(1);
        bot.setRole(new Magicien(botliste));
        botAleatoire.setOrdreChoixRole(2);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Roi(botliste),new Assassin(botliste,new ArrayList<>()))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE,Quartier.PRISON)));
        //Il a 7 quartiers donc il va choisir assasin vu qu'il est aprés et dans les roles restants
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Pretre);

        //Il en plus que 6 il ne doit pas prendre le pretre
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        aViser = bot.roleProbable(botAleatoire);
        assertFalse(aViser instanceof Pretre);
    }
    @Test
    void roleProbablePretreAvant(){
        bot.setOrdreChoixRole(2);
        bot.setRole(new Magicien(botliste));
        botAleatoire.setOrdreChoixRole(1);
        botAleatoire.changerOr(20);
        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Roi(botliste),new Assassin(botliste,new ArrayList<>()))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Marchand(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE,Quartier.PRISON)));
        //Il a 7 quartiers donc il va choisir assasin vu qu'il est avant lui et il n'est pas dans les roles restants ni dans les roles visibles
        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Pretre);

        //Il en plus que 6 il ne doit pas prendre le pretre
        botAleatoire.setQuartiersConstruits(new ArrayList<>(Arrays.asList(Quartier.TOUR_DE_GUET,Quartier.DONJON,Quartier.TAVERNE,Quartier.HOTEL_DE_VILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        doReturn(0).when(bot).randInt(anyInt());
        aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Voleur);
    }

    @Test
    void roleProbableJoueurAvanceApres(){
        bot.setOrdreChoixRole(1);
        bot.setRole(new Magicien(botliste));
        botAleatoire.setOrdreChoixRole(2);
        bot.setJoueurAvance(true); //on fait comme si bot1 etait un 'joueur avancé'

        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Pretre(botliste),new Assassin(botliste,new ArrayList<>()))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Roi(botliste),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);

        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Roi);
    }
    @Test
    void roleProbableJoueurAvanceAvant(){
        bot.setOrdreChoixRole(2);
        bot.setRole(assassin);
        botAleatoire.setOrdreChoixRole(1);
        bot.setJoueurAvance(true); //on fait comme si bot1 etait un 'joueur avancé'

        bot.setRolesVisible(new ArrayList<>(Arrays.asList(new Pretre(botliste),new Assassin(botliste,new ArrayList<>()))));
        ArrayList<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Assassin(botliste, new ArrayList<>()),new Condottiere(botliste)));
        bot.setRolesRestants(rolesRestant);

        Role aViser = bot.roleProbable(botAleatoire);
        assertTrue(aViser instanceof Roi);

        bot.faireActionSpecialRole();

        verify(bot).faireActionSpecialRole();
        verify(assassin).tuer(any(Roi.class));
    }

    @Test
    void testAssassinRandom(){
        bot.setOrdreChoixRole(2);
        bot.setRole(assassin);

        doReturn(0).when(bot).randInt(7);
        bot.faireActionSpecialRole();


        doReturn(1).when(bot).randInt(8);
        bot.setRolesRestants(roles);
        bot.faireActionSpecialRole();

        verify(assassin, times(2)).tuer(any(Voleur.class));

    }
    @Test
    void ChoixRoleBotPresqueFiniThisEstPremier(){
        //cas ou c'est this le joueur qui a presque fini et que que il a l'ordre 1
        //cas ou il y'a l'assassin, choisir l'assassin
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(1);
        System.out.println("ordre choix role"+bot.getOrdreChoixRole());
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        roles.add(condottiere);

        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());

        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==assassin);




        roles.remove(0);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        roles.add(condottiere);
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==pretre);

        //cas ou il n'y a ni assassin ni pretre

        roles.remove(0);
        //roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(pretre);
        roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==condottiere);


    }
    @Test
    void ChoixRoleBotPresqueFiniThisEstDeuxieme(){
        //cas particulier
        //cas ou c'est this le joueur qui a presque fini et que que il a l'ordre 1
        //cas ou il y'a l'assassin, choisir l'assassin
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        //roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==assassin);

        roles.remove(0);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(condottiere);
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==pretre);

        //cas ou il n'y a ni assassin ni pretre

        roles.remove(0);
        //roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(pretre);
        roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        bot.setQuartiersConstruits(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==condottiere);


    }
    @Test
    void ChoixRoleBotPresqueFiniBotEstDeuxieme(){
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        botFocusRoi.setQuartiersConstruits(quart);
        botFocusRoi.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        bot.setOrdreChoixRole(1);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        //roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==botFocusRoi);
        assertTrue(bot.getRole()==assassin);

    }
    @Test
    void ChoixRoleBotPresqueFiniBotEstTroisiemeEtThisEstPremierOuDeuxieme(){

        List<Bot> botliste2 = new ArrayList<>();
        Tour tour2= new Tour(botliste2);
        bot2=new BotRichard(pioche);
        botliste2.add(bot);
        botliste2.add(bot2);
        botliste2.add(botFocusRoi);
        botliste2.add(botConstruitChere);
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste2));
        bot2.setRole(new Marchand(botliste2));
        botFocusRoi.setQuartiersConstruits(quart);
        botFocusRoi.setOrdreChoixRole(3);
        bot2.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(4);
        bot.setOrdreChoixRole(1);

        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste2, roles);
        Pretre pretre=new Pretre(botliste2);
        Condottiere condottiere=new Condottiere(botliste2);
        roles.add(assassin);
        roles.add(new Voleur(botliste2, roles));
        //roles.add(new Magicien(botliste2));
        //roles.add(new Roi(botliste2));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste2));
        roles.add(condottiere);
        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour2.setRolesTemp(roles);
        bot.setNbTour(10);
        bot2.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour2.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        System.out.println("role choisi par bot richard2 "+bot2.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==botFocusRoi);
        assertTrue(bot.getRole()==condottiere);
        assertTrue(bot2.getRole()==assassin);


    }



}
