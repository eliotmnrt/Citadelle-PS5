package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BotAleatoireTest {
    private BotAleatoire bot;
    ArrayList<Bot> botliste;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot);
    }

    @Test
    public void quartierTest(){
        bot.ajoutQuartierMain(Quartier.TAVERNE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.quartierMain.size()); //4 de base + 3 ajouts
        bot.setRole(new Roi(botliste));
        bot.construire();
        assertTrue(bot.quartierMain.size()==6||bot.quartierMain.size()==7);
        assertTrue(bot.quartierConstruit.isEmpty() ||bot.quartierConstruit.size()==1);
    }

    @Test
    public void testManufacture(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.MANUFACTURE);
        bot.setQuartierMain(main);    //main = manufacture

        assertSame(Quartier.MANUFACTURE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.MANUFACTURE);       //lui coute 5ors, lui en reste 7
        assertSame(Quartier.MANUFACTURE, bot.getQuartiersConstruits().get(0));
        assertEquals(0, bot.getQuartierMain().size());

        bot.quartiersViolets();
        assertEquals(3, bot.getQuartierMain().size());
        assertEquals(4, bot.getOr());
    }

    @Test
    public void testLaboratoire(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.LABORATOIRE);
        main.add(Quartier.MARCHE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.LABORATOIRE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.LABORATOIRE);       //lui coute 5ors, lui en reste 7
        bot.ajoutQuartierConstruit(Quartier.MARCHE);       //lui coute 2ors, lui en reste 5

        assertSame(Quartier.LABORATOIRE, bot.getQuartiersConstruits().get(0));
        assertEquals(1, bot.getQuartierMain().size());

        bot.quartiersViolets();     //on echange la carte marché, en doublon contre 1or

        assertEquals(0, bot.getQuartierMain().size());
        assertEquals(6, bot.getOr());
    }

}