package fr.cotedazur.univ.polytech.startingpoint.cartes;

import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoiTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        bot = new BotAleatoire();
        bot.setRole(new Roi());
    }
    @Test
    public void roiSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(bot.getOr(),4);

        bot.ajoutQuartierMain(Quartier.CHATEAU);
        assertTrue(bot.getQuartierMain().get(0) == Quartier.CHATEAU );
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.CHATEAU);
        assertTrue(bot.getQuartiersConstruits().get(0) == Quartier.CHATEAU );
        assertTrue(bot.getQuartierMain().isEmpty());

        //Chateau est un quartier jaune, il doit avoir 1 or en plus
        assertEquals(bot.getOr(),0);
        bot.faireActionSpecialRole();
        assertEquals(bot.getOr(),1);

        bot.faireActionSpecialRole();
        assertEquals(bot.getOr(),2);
    }

    @Test
    public void roiPasSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(bot.getOr(),4);

        bot.ajoutQuartierMain(Quartier.TAVERNE);
        assertTrue(bot.getQuartierMain().get(0) == Quartier.TAVERNE );
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertTrue(bot.getQuartiersConstruits().get(0) == Quartier.TAVERNE );
        assertTrue(bot.getQuartierMain().isEmpty());

        assertEquals(bot.getOr(),3);
        bot.faireActionSpecialRole();
        assertEquals(bot.getOr(),3); // taverne n'est pas jaune
    }

}