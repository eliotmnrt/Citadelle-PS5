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

import java.util.ArrayList;
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

        assertFalse(bot.getPremierAChoisir());

        bot1.setOrdreChoixRole(4);
        bot2.setOrdreChoixRole(2);
        bot3.setOrdreChoixRole(3);
        bot.setOrdreChoixRole(1);
        tour.prochainTour();

        //assertTrue(bot.getPremierAChoisir());


        }



    }
