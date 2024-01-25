package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Roi;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotAleatoireTest {
    private BotAleatoire bot;
    Pioche pioche;
    ArrayList<Bot> botliste;
    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = spy(new BotAleatoire(pioche));
        botliste = new ArrayList<>();
        botliste.add(bot);
    }

    @Test
    void quartierTest(){
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
    void testManufacture(){
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
    void testLaboratoire(){
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

    @Test
    void testBibliotheque(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.BIBLIOTHEQUE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);       //lui coute 6ors, lui en reste 6

        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartiersConstruits().get(0));
        assertEquals(1, bot.getQuartierMain().size());

        doReturn(0).when(bot).randInt(2);       //on le force à choisir de piocher des quartiers au lieu de prendre 2ors

        bot.faireActionDeBase();    //il pioche ses 2 quartiers et les garde car il a construit la bibliotheque

        assertEquals(3, bot.getQuartierMain().size());      //il a mtn 1 + 2 quartiers dans sa main
        assertEquals(6, bot.getOr());
    }

    @Test
    void testObservatoire(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.OBSERVATOIRE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.OBSERVATOIRE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.OBSERVATOIRE);       //lui coute 5ors, lui en reste 7

        assertSame(Quartier.OBSERVATOIRE, bot.getQuartiersConstruits().get(0));
        assertEquals(1, bot.getQuartierMain().size());

        doReturn(0).when(bot).randInt(2);       //on le force à choisir de piocher des quartiers au lieu de prendre  2ors
        doReturn(Quartier.TAVERNE).when(pioche).piocherQuartier();      //pour tester le NOMBRE de quartiers piochés on le fait piocher uniquement des tavernes

        bot.faireActionDeBase();    //il pioche ses 2 quartiers et les garde car il a construit la bibliotheque

        ArrayList<Quartier> quartiersPioches = new ArrayList<>();
        quartiersPioches.add(Quartier.TAVERNE);
        quartiersPioches.add(Quartier.TAVERNE);
        quartiersPioches.add(Quartier.TAVERNE);


        verify(bot).choisirCarte(quartiersPioches);     //on verifie que l'on a bien les 3 tavernes qu'on a pioché

        assertEquals(2, bot.getQuartierMain().size());      //il a mtn 1 + 2 quartiers dans sa main
        assertEquals(7, bot.getOr());
    }

    @Test
    void testBibliothequeETObservatoire(){
        bot.changerOr(20);      //il a 22 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.BIBLIOTHEQUE);
        main.add(Quartier.OBSERVATOIRE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartierMain().get(0));
        assertSame(Quartier.OBSERVATOIRE, bot.getQuartierMain().get(1));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);       //lui coute 6ors, lui en reste 16
        bot.ajoutQuartierConstruit(Quartier.OBSERVATOIRE);       //lui coute 5ors, lui en reste 11

        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartiersConstruits().get(0));
        assertSame(Quartier.OBSERVATOIRE, bot.getQuartiersConstruits().get(1));
        assertEquals(1, bot.getQuartierMain().size());

        doReturn(0).when(bot).randInt(2);       //on le force à choisir de piocher des quartiers au lieu de 2ors

        bot.faireActionDeBase();    //il pioche ses 3 quartiers et les garde car il a construit la bibliotheque

        assertEquals(4, bot.getQuartierMain().size());      //il a mtn 1 + 3 quartiers dans sa main
        assertEquals(11, bot.getOr());
    }



}