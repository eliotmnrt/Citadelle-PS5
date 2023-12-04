package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Roi;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.Bot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TourTest {
    @Test
    void tourAvecRoiTest() {
        Bot bot = new Bot();
        Tour tour = new Tour(bot);
        assertFalse(bot.getRole() == null); // on a bien attribuer un role
        // pour l'instant y'a que roi après mettre :
        //bot.setRole(new Roi(bot));
        assertTrue(bot.getRole() instanceof Roi);
    }
}