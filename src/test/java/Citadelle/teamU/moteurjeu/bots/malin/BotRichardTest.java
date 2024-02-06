package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Tour;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BotRichardTest {
    BotRichard bot;
    Pioche pioche;

    List<Bot> botliste;
    Bot bot1;
    Bot bot2;
    Bot bot3;
    Tour tour;

    @BeforeEach
    public void setBot() {
        pioche = spy(new Pioche());
        bot = spy(new BotRichard(pioche));
        botliste = new ArrayList<>();
        tour = new Tour(botliste);
        bot1 = new BotAleatoire(pioche);
        bot2 = new BotConstruitChere(pioche);
        bot3 = new BotFocusRoi(pioche);
        botliste.add(bot);
        botliste.add(bot1);
        botliste.add(bot2);
        botliste.add(bot3);


    }

    @Test
        //tester si le bot Richard choisit soit l'assassin ou l'architecte si il est le premier à choisir
    void testPremierAChoisir() {


        bot.setOrdreChoixRole(4);
        bot1.setOrdreChoixRole(2);
        bot2.setOrdreChoixRole(3);
        bot3.setOrdreChoixRole(1);
        tour.prochainTour();

        //assertFalse(bot.getPremierAChoisir());

        bot1.setOrdreChoixRole(4);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot.setOrdreChoixRole(1);
        tour.prochainTour();

        //assertTrue(bot.getPremierAChoisir());


        }
        @Test
        void roleProbableTestCouleursApres(){
            bot.setOrdreChoixRole(1);
            bot1.setOrdreChoixRole(2);
            bot.setRole(new Marchand(botliste));
            List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Roi(botliste),new Pretre(botliste),new Condottiere(botliste)));
            bot1.setQuartierConstruit(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE)));
            //chateau, palais et manoir sont jaune et marche rouge
            //Donc il doit viser le Roi car il est dans les roles restant et il a 3 quartier Jaune
            bot1.setRole(new Roi(botliste));
            Role aViser = bot.roleProbable(bot1,rolesRestant);
            System.out.println(aViser);
            assertTrue(aViser instanceof Roi);

            bot1.setQuartierConstruit(new ArrayList<>(Arrays.asList(Quartier.CHATEAU)));
            aViser = bot.roleProbable(bot1,rolesRestant);
            assertFalse(aViser instanceof Roi);
        }
    @Test
    void roleProbableTestCouleursAvant(){
        bot.setRole(new Marchand(botliste));
        bot.setOrdreChoixRole(3);
        bot1.setOrdreChoixRole(1);
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        bot1.setQuartierConstruit(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE)));
        //chateau, palais et manoir sont jaune et marche, forteresse rouge
        //Donc il doit viser le Roi car il n'est pas dans les roles restant et il a 3 quartiers Jaune
        bot1.setRole(new Roi(botliste));
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertTrue(aViser instanceof Roi);

        bot1.setQuartierConstruit(new ArrayList<>(Arrays.asList(Quartier.CHATEAU)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Roi);

        //Bcp de quartier Rouge mais Condottiere après lui alors que il est avant lui dans l'ordre
        bot1.setQuartierConstruit(new ArrayList<>(Arrays.asList(Quartier.CHATEAU,Quartier.TERRAIN_DE_BATAILLE,Quartier.MARCHE,Quartier.FORTERESSE)));
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Roi(botliste),new Condottiere(botliste)));
        bot1.setRole(new Magicien(botliste));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Condottiere);
    }
    @Test
    void roleProbableVoleurApres(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Voleur(botliste,new ArrayList<>()));
        bot1.changerOr(-1); //On a 1 or
        assertEquals(1,bot1.getOr());
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Voleur(botliste,new ArrayList<>())));
        bot.setOrdreChoixRole(1);
        bot1.setOrdreChoixRole(2);

        //Le voleur est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertTrue(aViser instanceof Voleur);

        //Le voleur n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Voleur);
    }

    @Test
    void roleProbableVoleurAvant(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Voleur(botliste,new ArrayList<>()));
        bot1.changerOr(-1); //On a 1 or
        assertEquals(1,bot1.getOr());
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Voleur(botliste,new ArrayList<>())));
        bot.setOrdreChoixRole(2);
        bot1.setOrdreChoixRole(1);

        //Le voleur est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Voleur);

        //Le voleur n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertTrue(aViser instanceof Voleur);
    }
    @Test
    void roleProbableMagicienApres(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Magicien(botliste));
        bot1.changerOr(40);
        bot1.setQuartierMain(Arrays.asList(Quartier.CHATEAU));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Magicien(botliste)));
        bot.setOrdreChoixRole(1);
        bot1.setOrdreChoixRole(2);

        //Le Magicien est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertTrue(aViser instanceof Magicien);

        //Le Magicien n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Magicien);
    }

    @Test
    void roleProbableMagicienAvant(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Magicien(botliste));
        bot1.changerOr(40);
        bot1.setQuartierMain(Arrays.asList(Quartier.CHATEAU));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Magicien(botliste)));
        bot.setOrdreChoixRole(2);
        bot1.setOrdreChoixRole(1);

        //Le Magicien est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Magicien);

        //Le Magicien n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        System.out.println(aViser);
        assertTrue(aViser instanceof Magicien);
    }
    @Test
    void roleProbableArchiApres(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Architecte(botliste));
        bot1.changerOr(40);
        bot1.setQuartierMain(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Architecte(botliste)));
        bot.setOrdreChoixRole(1);
        bot1.setOrdreChoixRole(2);

        //L'archi est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertTrue(aViser instanceof Architecte);

        //L'archi n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Architecte);
    }

    @Test
    void roleProbableArchiAvant(){
        bot.setRole(new Roi(botliste));
        bot1.setRole(new Architecte(botliste));
        bot1.changerOr(40);
        bot1.setQuartierMain(Arrays.asList(Quartier.CHATEAU,Quartier.PALAIS,Quartier.MANOIR,Quartier.MARCHE,Quartier.FORTERESSE));
        List<Role> rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste),new Architecte(botliste)));
        bot.setOrdreChoixRole(2);
        bot1.setOrdreChoixRole(1);

        //L'archi est dans les roles restants
        Role aViser = bot.roleProbable(bot1,rolesRestant);
        assertFalse(aViser instanceof Architecte);

        //L'archi n'est pas dans les roles restants
        rolesRestant = new ArrayList<>(Arrays.asList(new Pretre(botliste),new Condottiere(botliste)));
        aViser = bot.roleProbable(bot1,rolesRestant);
        System.out.println(aViser);
        assertTrue(aViser instanceof Architecte);
    }

    }
