package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoiTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire();
        bot.setRole(new Roi());
    }
    @Test
    public void roiSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.CHATEAU);
        assertSame(bot.getQuartierMain().get(4), Quartier.CHATEAU);
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.CHATEAU);
        assertSame(bot.getQuartiersConstruits().get(0), Quartier.CHATEAU);
        assertEquals(4, bot.getQuartierMain().size());

        //Chateau est un quartier jaune, il doit avoir 1 or en plus
        assertEquals(0, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(1, bot.getOr());

        bot.faireActionSpecialRole();
        assertEquals(2, bot.getOr());
    }

    @Test
    public void roiPasSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        assertEquals(3, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(3, bot.getOr()); // taverne n'est pas jaune
    }

}