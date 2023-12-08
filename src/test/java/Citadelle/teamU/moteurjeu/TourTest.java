package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.moteurjeu.bots.Bot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {
    @Test
    void tourAvecRoiTest() {
        Pioche pioche = new Pioche();
        Bot bot = new Bot();
        Tour tour = new Tour(bot);
        assertFalse(bot.getRole() == null); // on a bien attribué un role
        // pour l'instant y'a que roi après mettre :
        //bot.setRole(new Roi(bot));
        assertTrue(bot.getRole() instanceof Roi);
    }
}