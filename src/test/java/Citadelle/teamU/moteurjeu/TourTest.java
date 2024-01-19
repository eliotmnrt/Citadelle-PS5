package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.BotConstruitVite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

class TourTest {

    ArrayList<Bot> botliste;
    Pioche pioche;
    Tour tour;
    Bot bot1;
    Bot bot2;
    Bot bot3;
    Bot bot4;

    @BeforeEach
    void setUp(){
        pioche = new Pioche();
        bot1 = Mockito.spy(new BotAleatoire(pioche));
        bot2 = Mockito.spy(new BotAleatoire(pioche));
        bot3 = Mockito.spy(new BotAleatoire(pioche));
        bot4 = Mockito.spy(new BotAleatoire(pioche));
        botliste = new ArrayList<>();
        botliste.add(bot1);
        botliste.add(bot2);
        botliste.add(bot3);
        botliste.add(bot4);
        tour = new Tour(botliste);
    }

    @Test
    void tourAvecRoiTest() {
        tour.prochainTour();
        assertNotNull(botliste.get(0).getRole()); // on a bien attribué un role
        assertNotNull(botliste.get(1).getRole());
        assertNotNull(botliste.get(2).getRole());
        assertNotNull(botliste.get(3).getRole());

        // pour l'instant y'a que roi après mettre :
        //bot.setRole(new Roi(bot));
        assertEquals(1, botliste.get(0).getRole().getClass().getInterfaces().length);
        assertEquals(1, botliste.get(1).getRole().getClass().getInterfaces().length);
        assertEquals(1, botliste.get(2).getRole().getClass().getInterfaces().length);
        assertEquals(1, botliste.get(3).getRole().getClass().getInterfaces().length);
    }
    @Test
    void couronneTest(){
        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        bot1.setOrdreChoixRole(1);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot4.setOrdreChoixRole(4);
        bot2.setCouronne(true);
        //Le bot 2 commence a choisir les roles
        //Donc l'ordre de choix sera bot2->bot3->bot4->bot1
        doNothing().when(bot1).choisirRole(any());
        doNothing().when(bot2).choisirRole(any());
        doNothing().when(bot3).choisirRole(any());
        doNothing().when(bot4).choisirRole(any());

        ArrayList<Bot> res = tour.distributionRoles();
        assertEquals(res.get(0),bot2);
        assertEquals(res.get(1),bot3);
        assertEquals(res.get(2),bot4);
        assertEquals(res.get(3),bot1);
    }
    @Test
    void sansCouronneTest(){
        //On donne l'ordre dans lequel ils jouent 1->2->3->4->1...
        bot1.setOrdreChoixRole(1);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot4.setOrdreChoixRole(4);

        //Personne n'a de couronne le bot1 commence
        //Donc l'ordre de choix sera bot1->bot2->bot3->bot4->...
        doNothing().when(bot1).choisirRole(any());
        doNothing().when(bot2).choisirRole(any());
        doNothing().when(bot3).choisirRole(any());
        doNothing().when(bot4).choisirRole(any());

        ArrayList<Bot> res = tour.distributionRoles();
        assertEquals(res.get(0),bot1);
        assertEquals(res.get(1),bot2);
        assertEquals(res.get(2),bot3);
        assertEquals(res.get(3),bot4);
    }
    @Test
    void bonusTest(){
        //A FAIRE
    }
}