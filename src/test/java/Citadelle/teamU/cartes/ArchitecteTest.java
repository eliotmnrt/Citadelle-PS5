package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArchitecteTest {
    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire();
        bot.setRole(new Architecte());
    }
    @Test
    public void archiSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(4, bot.getOr());

        assertEquals(bot.getQuartierMain().size(),4);

        bot.ajoutQuartierMain(Quartier.TEMPLE);
        assertSame(bot.getQuartierMain().get(4), Quartier.TEMPLE);
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        // Le bot peut construire entre 1 et 3 quartiers
        ArrayList<Quartier> quartiersConstruit = bot.construireQuartierAleatoire();
        assertTrue(quartiersConstruit.size()==1||quartiersConstruit.size()==2||quartiersConstruit.size()==3);
    }
}