package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MarchandTest {

    private BotAleatoire bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotAleatoire(pioche);
        ArrayList<Bot> botliste = new ArrayList<>();
        botliste.add(bot);
        bot.setRole(new Marchand(botliste));
    }
    @Test
    void MarchandSpecialTest(){
        bot.changerOr(2);
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        /*taverne est un quartier vert, il doit avoir 1 or en plus
        gagne une piece supplémentaire car il est marchand*/
        assertEquals(3, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(5, bot.getOr());

        bot.faireActionSpecialRole();
        assertEquals(7, bot.getOr());
    }

    @Test
    void MarchandPasSpecialTest(){
        bot.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(4, bot.getOr());

        bot.ajoutQuartierMain(Quartier.TEMPLE);
        assertSame(Quartier.TEMPLE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.TEMPLE);
        assertSame(Quartier.TEMPLE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        assertEquals(3, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(4, bot.getOr()); // temple n'est pas vert,marchand gagne pièce supp
    }
    @Test
    void marchandConstruitEcoleDeMagie(){
        bot.changerOr(10);      //il a 12 ors
        bot.ajoutQuartierMain(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, bot.getQuartierMain().get(4));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, bot.getQuartiersConstruits().get(0));
        assertEquals(4, bot.getQuartierMain().size());

        assertEquals(6, bot.getOr());
        bot.faireActionSpecialRole();
        assertEquals(8, bot.getOr()); // ecole de magie compte pour vert
    }

}