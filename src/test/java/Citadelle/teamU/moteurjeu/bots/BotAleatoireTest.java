package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotAleatoireTest {
    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire();
    }

    @Test
    public void quartierTest(){
        bot.ajoutQuartierMain(Quartier.TAVERNE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.quartierMain.size()); //4 de base + 3 ajouts
        bot.construireQuartierAleatoire();
        assertEquals(6, bot.quartierMain.size());
        assertTrue(bot.quartierConstruit.get(0) != Quartier.CATHEDRALE);
        assertEquals(1, bot.quartierConstruit.size());
    }

}