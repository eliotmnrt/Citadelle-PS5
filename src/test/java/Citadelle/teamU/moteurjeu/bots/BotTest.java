package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Architecte;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotTest {
    public Bot bot;
    @BeforeEach
    void setBot(){
        Pioche pioche = new Pioche();
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
        assertEquals(4, bot.getQuartierMain().size());
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        //ajout de 1 quartier dans notre main
        bot.ajoutQuartierMain(Quartier.TAVERNE);
        assertEquals(5, bot.getQuartierMain().size());
        assertTrue(bot.getQuartiersConstruits().isEmpty());
        assertEquals(Quartier.TAVERNE, bot.getQuartierMain().get(4));

        //ajout quartier construit verifier qu'il est dans la main et l'enlever de la main
        bot.ajoutQuartierConstruit(bot.getQuartierMain().get(4));
        assertEquals(4, bot.getQuartierMain().size());
        assertFalse(bot.getQuartiersConstruits().isEmpty());
        assertEquals(1, bot.getOr()); // le batiment coute 1 à construire
        assertEquals(Quartier.TAVERNE, bot.getQuartiersConstruits().get(0));
    }
    @Test
    void botScoreTest(){
        bot.changerOr(100); //pour pouvoir tout acheter
        bot.setRole(new Architecte()); //pour en construire 2
        // On en ajoute nous meme au cas ou le 1er et le 2e soit le meme
        assertEquals(bot.getQuartierMain().size(), 4);
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        assertEquals(bot.getQuartierMain().size(), 6);
        bot.ajoutQuartierConstruit(bot.getQuartierMain().get(5));
        bot.ajoutQuartierConstruit(bot.getQuartierMain().get(4));
        assertEquals(bot.getQuartiersConstruits().get(0).getCout()+bot.getQuartiersConstruits().get(1).getCout(), bot.getScore());
        // s'assure que le score conrespond bien à la somme des couts des quartiers construits
    }

}