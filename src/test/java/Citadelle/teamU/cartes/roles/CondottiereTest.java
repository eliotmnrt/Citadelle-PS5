package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.BotConstruitVite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.moteurjeu.*;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CondottiereTest {

    private BotAleatoire botAleatoire, botAleatoire2;
    private BotConstruitVite botConstruitVite;
    private BotConstruitChere botConstruitChere;
    private ArrayList<Bot> botliste;
    Condottiere track;


    @BeforeEach
    void setUp(){
        Pioche pioche = new Pioche();
        botAleatoire = Mockito.spy(new BotAleatoire(pioche));
        botAleatoire2 = Mockito.spy(new BotAleatoire(pioche));
        botConstruitVite = Mockito.spy(new BotConstruitVite(pioche));
        botConstruitChere =Mockito.spy(new BotConstruitChere(pioche));
        botliste = new ArrayList<>();
        botliste.add(botAleatoire);
        botliste.add(botAleatoire2);
        botliste.add(botConstruitVite);
        botliste.add(botConstruitChere);
        track = Mockito.spy(new Condottiere(botliste));
    }

    @Test
    void testAleatoire(){
        botAleatoire.setRole(track);       //botAleatoire est le condotierre
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));

        doReturn(2).when(botAleatoire).randInt(3);      //on force à viser de dernier bot aka
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeCondottiere(track);

    }

    @Test
    public void CondoQuartierRougeTest(){
        botAleatoire.setRole(track);
        botAleatoire.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botAleatoire.getOr());

        botAleatoire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botAleatoire.getQuartierMain().get(4), Quartier.TERRAIN_DE_BATAILLE);
        assertTrue(botAleatoire.getQuartiersConstruits().isEmpty());

        botAleatoire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botAleatoire.getQuartiersConstruits().get(0), Quartier.TERRAIN_DE_BATAILLE);
        assertEquals(4, botAleatoire.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botAleatoire.getOr());
        botAleatoire.faireActionSpecialRole();
        assertEquals(2, botAleatoire.getOr());

        botAleatoire.faireActionSpecialRole();
        assertEquals(3, botAleatoire.getOr());
    }
    @Test
    public void CondoQuartierNonRougeTest() {
        botAleatoire.setRole(track);
        botAleatoire.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(4, botAleatoire.getOr());

        botAleatoire.ajoutQuartierMain(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botAleatoire.getQuartierMain().get(4));
        assertTrue(botAleatoire.getQuartiersConstruits().isEmpty());

        botAleatoire.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botAleatoire.getQuartiersConstruits().get(0));
        assertEquals(4, botAleatoire.getQuartierMain().size());

        assertEquals(3, botAleatoire.getOr());
        botAleatoire.faireActionSpecialRole();
        assertEquals(3, botAleatoire.getOr()); // taverne n'est pas rouge
    }



}