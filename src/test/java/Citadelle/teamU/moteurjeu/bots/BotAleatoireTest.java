package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Roi;
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
        bot.setRole(new Roi());
        bot.construireQuartierAleatoire();
        assertEquals(6, bot.quartierMain.size());
        assertNotSame(Quartier.CATHEDRALE, bot.quartierConstruit.get(0));
        assertEquals(1, bot.quartierConstruit.size());
    }

}