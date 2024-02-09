package Citadelle.teamU.moteurJeu;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


class JeuTest {


    Bot bot1, bot2, bot3, bot4;
    Jeu game;

    @BeforeEach
    void setUp(){
        Pioche pioche = new Pioche();
        bot1 = new BotAleatoire(pioche);
        bot2 = new BotAleatoire(pioche);
        bot3 = new BotAleatoire(pioche);
        bot4 = new BotAleatoire(pioche);
        game = new Jeu(bot1, bot2, bot3, bot4);

    }
    @Test
    void TestConditionsFinDePartie(){
        game.start();
        int maxQuartiersConstruits = 0;
        for (Bot bot: game.getBotListe()){
            if(bot.getQuartiersConstruits().size() > maxQuartiersConstruits){
                maxQuartiersConstruits = bot.getQuartiersConstruits().size();
            }
        }
        assertTrue(maxQuartiersConstruits>=8);
    }
}