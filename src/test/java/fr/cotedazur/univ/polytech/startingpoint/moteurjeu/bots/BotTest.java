package fr.cotedazur.univ.polytech.startingpoint.moteurjeu.bots;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
import fr.cotedazur.univ.polytech.startingpoint.cartes.Roi;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Pioche;
import fr.cotedazur.univ.polytech.startingpoint.moteurjeu.Tour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    public Bot bot;
    @BeforeEach
    void setBot(){
        bot = new Bot();
    }
    @Test
    void botOrTest() {
        assertEquals(bot.getOr(),2); //le bot commence avec 2 d'or
        bot.setOr(bot.getOr()+2);
        assertEquals(bot.getOr(),4);
    }
    @Test
    void botQuartierTest() {
        // au départ on a aucun quartier
        assertTrue(bot.getQuartierMain().isEmpty());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        //ajout de 1 quartier dans notre main
        Pioche pioche = new Pioche();
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        assertFalse(bot.getQuartierMain().isEmpty());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        //ajout quartier construit verifier qu'il est dans la main et l'enlever de la main
    }

}