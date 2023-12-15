package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.Roi;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BotConstruitViteTest {
    private BotConstruitVite bot;
    @BeforeEach
    public void setBot(){
        Pioche pioche = new Pioche();
        bot = new BotConstruitVite();
    }

    @Test
    public void quartierMoinsChereTest(){
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.quartierMain.size()); //4 de base + 3 ajouts
        bot.setRole(new Roi());
        bot.construire();
        assertEquals(1, bot.quartierConstruit.get(0).getCout());
        assertEquals(1, bot.quartierConstruit.size());
        assertEquals(6, bot.quartierMain.size());
    }

    @Test
    public void piocheTest(){
        bot.setRole(new Roi());
        bot.faireActionDeBase();
        //Il prend de l'or
        System.out.println(bot.quartierMain);
        System.out.println(bot.quartierConstruit);
        System.out.println(bot.getOr());
        ArrayList<Quartier> testArray = new ArrayList<>();
        testArray.add(Quartier.MONASTERE);
        testArray.contains(Quartier.MONASTERE);
        if(bot.quartierMain.size() == 3){
            //Il a pris de l'or pcq il peut construire et construit 1 quartier qui coute moins que 3
            assertTrue(bot.getOr()==4-bot.quartierConstruit.get(0).getCout() );
            assertTrue(bot.quartierConstruit.size()==1);
            assertTrue(bot.quartierConstruit.get(0).getCout()<=3);
        } // Il pioche
        else if(bot.quartierMain.size() == 4){
            //il a pioche pcq il a que des trucs chère, pioche un truc pas chère et le construit
            //Les 4 dans sa mains sont donc chère
            assertTrue(bot.quartierMain.get(0).getCout()>3);
            assertTrue(bot.quartierMain.get(1).getCout()>3);
            assertTrue(bot.quartierMain.get(2).getCout()>3);
            assertTrue(bot.quartierMain.get(3).getCout()>3);
            assertTrue(bot.quartierConstruit.get(0).getCout()<=3);
            assertTrue(bot.quartierConstruit.size()==1);
        }
        else{
            //Il prend pioche et ne peut pas construire car tout est trop chère ou pas assez de piece
            assertTrue(bot.quartierMain.size()==5&&bot.quartierConstruit.size()==0);
            assertTrue(bot.quartierMain.get(0).getCout()>2&&bot.quartierMain.get(1).getCout()>3&&bot.quartierMain.get(2).getCout()>3&&bot.quartierMain.get(3).getCout()>3&&bot.quartierMain.get(4).getCout()>3);
            assertTrue(bot.quartierConstruit.isEmpty()||bot.quartierMain.get(0).getCout()>3);
        }
    }

}