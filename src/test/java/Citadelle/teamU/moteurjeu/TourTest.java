package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.cartes.roles.Role;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {

    ArrayList<Bot> botliste;

    @BeforeEach
    void setUp(){
        Pioche pioche = new Pioche();
        Bot bot1 = new BotAleatoire(pioche);
        Bot bot2 = new BotAleatoire(pioche);
        Bot bot3 = new BotAleatoire(pioche);
        Bot bot4 = new BotAleatoire(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot1);
        botliste.add(bot2);
        botliste.add(bot3);
        botliste.add(bot4);
        Tour tour = new Tour(botliste);
        tour.prochainTour();
    }

    @Test
    void tourAvecRoiTest() {

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
}