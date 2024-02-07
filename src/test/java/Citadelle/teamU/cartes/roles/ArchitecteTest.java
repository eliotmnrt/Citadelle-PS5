package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ArchitecteTest {
    private BotAleatoire bot;
    ArrayList<Bot> botliste;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot);
        bot.setRole(new Architecte(botliste));
    }
    @Test
    void archiSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(4, bot.getOr());

        assertEquals(4, bot.getQuartierMain().size());
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        assertSame(Quartier.TEMPLE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        // Le bot peut construire entre 1 et 3 quartiers
        //A re faire
    }
}