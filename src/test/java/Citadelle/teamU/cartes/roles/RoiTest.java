package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RoiTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire();
        ArrayList<Bot> botliste = new ArrayList<>();
        botliste.add(bot);
        bot.setRole(new Roi(botliste));
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