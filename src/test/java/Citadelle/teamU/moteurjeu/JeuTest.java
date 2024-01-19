package Citadelle.teamU.moteurjeu;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class JeuTest {


    Bot bot1, bot2, bot3, bot4;

    @BeforeEach
    void setUp(){
        Pioche pioche = new Pioche();
        bot1 = new BotAleatoire(pioche);
        bot2 = new BotAleatoire(pioche);
        bot3 = new BotAleatoire(pioche);
        bot4 = new BotAleatoire(pioche);
    }
    @Test
    void TestConditionsFinDePartie(){
        Jeu game = new Jeu(bot1, bot2, bot3, bot4);
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