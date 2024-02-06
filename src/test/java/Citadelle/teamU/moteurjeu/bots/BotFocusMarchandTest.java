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


}
