package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {
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
    void archiTest(){
        bot.setRole(new Architecte(botliste));
        bot.changerOr(20); // 22 d'or au total pour pouvoir construire bcp potentiellement
        assertEquals(22, bot.getOr());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        // le bot a 3 quartiers de base
        // Le bot peut construire entre 1 et 3 quartiers
        }
    @Test
    void RoiTest(){
        bot.setRole(new Roi(botliste));
        bot.changerOr(20); // 22 d'or au total pour pouvoir construire n'importe quelle quartier
        assertEquals(22, bot.getOr());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        // le bot a 3 quartiers de base
        // Le bot peut construire 1 seul quartier vu que c'est un roi
        //A re faire
    }
}