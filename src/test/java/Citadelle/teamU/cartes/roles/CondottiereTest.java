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
        botAleatoire.changerOr(18);
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));
        ArrayList<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.LABORATOIRE);
        botAleatoire2.setQuartierConstruit(quart);
        doReturn(0).when(botAleatoire).randInt(3);      //on force à viser de dernier bot aka
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeCondottiere(track);
        verify(track).destructionQuartier(botAleatoire,botAleatoire2,Quartier.LABORATOIRE);
    }

    @Test
    public void CondoQuartierRougeTestAleatoire(){
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
    public void CondoQuartierNonRougeTestAleatoire() {
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

    @Test
    public void CondoQuartierRougeTestConsChere(){
        botConstruitChere.setRole(track);
        botConstruitChere.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botConstruitChere.getOr());

        botConstruitChere.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botConstruitChere.getQuartierMain().get(4), Quartier.TERRAIN_DE_BATAILLE);
        assertTrue(botConstruitChere.getQuartiersConstruits().isEmpty());

        botConstruitChere.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botConstruitChere.getQuartiersConstruits().get(0), Quartier.TERRAIN_DE_BATAILLE);
        assertEquals(4, botConstruitChere.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botConstruitChere.getOr());
        botConstruitChere.faireActionSpecialRole();
        assertEquals(2, botConstruitChere.getOr());

        botConstruitChere.faireActionSpecialRole();
        assertEquals(3, botConstruitChere.getOr());
    }
    @Test
    public void CondoQuartierNonRougeTestConsChere() {
        botConstruitChere.setRole(track);
        botConstruitChere.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(4, botConstruitChere.getOr());

        botConstruitChere.ajoutQuartierMain(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botConstruitChere.getQuartierMain().get(4));
        assertTrue(botConstruitChere.getQuartiersConstruits().isEmpty());

        botConstruitChere.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botConstruitChere.getQuartiersConstruits().get(0));
        assertEquals(4, botConstruitChere.getQuartierMain().size());

        assertEquals(3, botConstruitChere.getOr());
        botConstruitChere.faireActionSpecialRole();
        assertEquals(3, botConstruitChere.getOr()); // taverne n'est pas rouge
    }

    @Test
    public void CondoQuartierRougeTestConsVite(){
        botConstruitVite.setRole(track);
        botConstruitVite.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botConstruitVite.getOr());

        botConstruitVite.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botConstruitVite.getQuartierMain().get(4), Quartier.TERRAIN_DE_BATAILLE);
        assertTrue(botConstruitVite.getQuartiersConstruits().isEmpty());

        botConstruitVite.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(botConstruitVite.getQuartiersConstruits().get(0), Quartier.TERRAIN_DE_BATAILLE);
        assertEquals(4, botConstruitVite.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botConstruitVite.getOr());
        botConstruitVite.faireActionSpecialRole();
        assertEquals(2, botConstruitVite.getOr());

        botConstruitVite.faireActionSpecialRole();
        assertEquals(3, botConstruitVite.getOr());
    }
    @Test
    public void CondoQuartierNonRougeTestConsVite() {
        botConstruitVite.setRole(track);
        botConstruitVite.changerOr(2); // 4 d'or au total (assez pour taverne)
        assertEquals(4, botConstruitVite.getOr());

        botConstruitVite.ajoutQuartierMain(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botConstruitVite.getQuartierMain().get(4));
        assertTrue(botConstruitVite.getQuartiersConstruits().isEmpty());

        botConstruitVite.ajoutQuartierConstruit(Quartier.TAVERNE);
        assertSame(Quartier.TAVERNE, botConstruitVite.getQuartiersConstruits().get(0));
        assertEquals(4, botConstruitVite.getQuartierMain().size());

        assertEquals(3, botConstruitVite.getOr());
        botConstruitVite.faireActionSpecialRole();
        assertEquals(3, botConstruitVite.getOr()); // taverne n'est pas rouge
    }


}