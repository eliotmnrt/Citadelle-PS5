package Citadelle.teamU.moteurjeu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JeuTest {

    @Test
    void TestConditionsFinDePartie(){
        Jeu game = new Jeu();
        assertEquals(8, game.getBot().get(0).getQuartiersConstruits().size());
    }
}