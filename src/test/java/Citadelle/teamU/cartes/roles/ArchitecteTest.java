package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Architecte;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
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
        bot = new BotAleatoire();
        botliste = new ArrayList<>();
        botliste.add(bot);
        bot.setRole(new Architecte(botliste));
    }
    @Test
    public void archiSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour chateau)
        assertEquals(4, bot.getOr());

        assertEquals(bot.getQuartierMain().size(),4);
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        assertSame(bot.getQuartierMain().get(4), Quartier.TEMPLE);
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        // Le bot peut construire entre 1 et 3 quartiers
        //A re faire
    }
}