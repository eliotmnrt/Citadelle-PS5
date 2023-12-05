package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotAleatoireTest {
    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        bot = new BotAleatoire();
    }

    @Test
    public void quartierTest(){
        bot.ajoutQuartierMain(Quartier.TAVERNE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.TEMPLE); // il ne peut pas l'acheter
        assertEquals(bot.quartierMain.size(),3);
        bot.construireQuartierAleatoire();
        assertEquals(bot.quartierMain.size(),2);
        assertTrue(bot.quartierConstruit.get(0) == Quartier.TAVERNE || bot.quartierConstruit.get(0) == Quartier.PRISON );
        assertEquals(bot.quartierConstruit.size(),1);
    }

}