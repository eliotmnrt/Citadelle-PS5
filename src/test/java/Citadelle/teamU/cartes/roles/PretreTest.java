package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PretreTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire(pioche);
        ArrayList<Bot> botliste = new ArrayList<>();
        botliste.add(bot);
        bot.setRole(new Pretre(botliste));
    }
    @Test
    void PretreSpecialTest(){
        bot.changerOr(2);
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.EGLISE);
        assertSame(Quartier.EGLISE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.EGLISE);
        assertSame(Quartier.EGLISE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        //église est un quartier bleue, il doit avoir 1 or en plus
        assertEquals(2, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(3, bot.getOr());

        bot.faireActionSpecialRole();
        assertEquals(4, bot.getOr());
    }

    @Test
    void PretrePasSpecialTest(){
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

    @Test
    void pretreConstruitEcoleDeMagie(){
        bot.changerOr(10);      //il a 12 ors
        bot.ajoutQuartierMain(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        assertEquals(6, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(7, bot.getOr()); // ecole de magie compte pour jaune n'est pas jaune
    }

}