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
        bot.setRole(new Roi(bot));
    }
    @Test
    public void roiTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(bot.getOr(),4);

        bot.ajoutQuartierMain(Quartier.CHATEAU);
        assertTrue(bot.getQuartierMain().get(0) == Quartier.CHATEAU );
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.CHATEAU);
        assertTrue(bot.getQuartiersConstruits().get(0) == Quartier.CHATEAU );
        assertTrue(bot.getQuartierMain().isEmpty());

        //Chateau est un quartier jaune, il doit avoir 1 or en plus
        bot.faireActionSpecialRole();
        assertEquals(bot.getOr(),1);

        bot.faireActionSpecialRole();
        assertEquals(bot.getOr(),2);
    }

}