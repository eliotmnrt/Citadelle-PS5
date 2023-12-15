package Citadelle.teamU.cartes;

import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PretreTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire();
        bot.setRole(new Pretre());
    }
    @Test
    public void PrêtreSpecialTest(){
        bot.changerOr(2);
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.EGLISE);
        assertSame(bot.getQuartierMain().get(4), Quartier.EGLISE);
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.EGLISE);
        assertSame(bot.getQuartiersConstruits().get(0), Quartier.EGLISE);
        assertEquals(4, bot.getQuartierMain().size());

        //église est un quartier bleue, il doit avoir 1 or en plus
        assertEquals(2, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(3, bot.getOr());

        bot.faireActionSpecialRole();
        assertEquals(4, bot.getOr());
    }

    @Test
    public void PrêtrePasSpecialTest(){
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
        assertEquals(3, bot.getOr()); // taverne n'est pas bleue
    }

}