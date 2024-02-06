package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotFocusMarchandTest {

    private BotFocusMarchand bot;
    Pioche pioche;
    List<Bot> botliste;

    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = spy(new BotFocusMarchand(pioche));
        botliste = new ArrayList<>();
        botliste.add(bot);
    }

    @Test
    void testChoixDeCartesJaunes(){
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.EGLISE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.TAVERNE, bot.getQuartierMain().get(0));    //il garde la taverne
    }

    @Test
    void testChoixDeCartes(){
        doReturn(Quartier.TERRAIN_DE_BATAILLE, Quartier.EGLISE, Quartier.PALAIS).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.EGLISE, bot.getQuartierMain().get(0));    //il garde le temple car moins cher
    }

    @Test
    void testChoixDeCartesEtLaboratoire(){
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.EGLISE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.TAVERNE, bot.getQuartierMain().get(0));    //il garde la taverne et pas le palais ni l eglise
    }

    @Test
    void testDecisionDeBase(){
        List<Quartier> quartiersMain = new ArrayList<>();
        quartiersMain.add(Quartier.PRISON);
        quartiersMain.add(Quartier.MANOIR);         //il a pas assez d'argent pour le construire donc il est censé piocher
        quartiersMain.add(Quartier.TEMPLE);
        quartiersMain.add(Quartier.COMPTOIR);
        bot.setQuartierMain(quartiersMain);

        bot.construire();       //censé ne rien construire car pas assez d'argent pour construire comptoir
        assertEquals(0, bot.getQuartiersConstruits().size());

        bot.faireActionDeBase();
        verify(bot).changerOr(2);   //verifie qu'on pioche
        assertEquals(2+2, bot.getOr());

        bot.construire();        //il a assez d'argent donc il doit construire le comptoir
        verify(bot).ajoutQuartierConstruit(Quartier.COMPTOIR);
        assertEquals(Quartier.COMPTOIR, bot.getQuartiersConstruits().get(0));
        assertEquals(4-3, bot.getOr()) ;

    }


}
