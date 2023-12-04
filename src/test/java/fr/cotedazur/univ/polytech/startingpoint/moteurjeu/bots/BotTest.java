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
        bot.changerOr(2);
        assertEquals(bot.getOr(),4);
        bot.changerOr(-1);
        assertEquals(bot.getOr(),3);
    }
    @Test
    void botQuartierTest() {
        // au départ on a aucun quartier
        assertTrue(bot.getQuartierMain().isEmpty());
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        //ajout de 1 quartier dans notre main
        Pioche pioche = new Pioche();
        bot.ajoutQuartierMain(Quartier.TAVERNE);
        assertFalse(bot.getQuartierMain().isEmpty());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        assertEquals(bot.getQuartierMain().get(0),Quartier.TAVERNE);

        //ajout quartier construit verifier qu'il est dans la main et l'enlever de la main
        bot.ajoutQuartierConstruit(bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartierMain().isEmpty());
        assertFalse(bot.getQuartiersConstruits().isEmpty());
        assertEquals(bot.getOr(),1); // le batiment coute 1 à construire
        assertEquals(bot.getQuartiersConstruits().get(0),Quartier.TAVERNE);
    }

}