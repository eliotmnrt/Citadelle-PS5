package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotConstruitViteTest {
    private BotConstruitVite bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotConstruitVite();
    }

    @Test
    public void quartierMoinsChereTest(){
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.quartierMain.size()); //4 de base + 3 ajouts
        bot.setRole(new Roi());
        bot.construireQuartierMoinsChere();
        assertEquals(1, bot.quartierConstruit.get(0).getCout());
        assertEquals(1, bot.quartierConstruit.size());
        assertEquals(6, bot.quartierMain.size());
    }

    @Test
    public void piocheTest(){
        bot.ajoutQuartierMain(Quartier.CIMETIERE);
        bot.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // les deux coute chere
        assertEquals(6, bot.quartierMain.size()); //4 de base + 2 ajouts
        bot.setRole(new Roi());
        bot.faireActionDeBase();
        assertTrue((bot.quartierMain.size()==6&&bot.quartierConstruit.size()==0)||(bot.quartierMain.size()==5&&bot.quartierConstruit.size()==1));
        assertTrue(bot.quartierConstruit.get(0).getCout()<4);
    }

}