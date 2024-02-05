package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.moteurjeu.*;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

import java.util.ArrayList;

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
    void CondoQuartierRougeTestAleatoire(){
        botAleatoire.setRole(track);
        botAleatoire.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botAleatoire.getOr());

        botAleatoire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botAleatoire.getQuartierMain().get(4));
        assertTrue(botAleatoire.getQuartiersConstruits().isEmpty());

        botAleatoire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botAleatoire.getQuartiersConstruits().get(0));
        assertEquals(4, botAleatoire.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botAleatoire.getOr());
        botAleatoire.faireActionSpecialRole();
        assertEquals(2, botAleatoire.getOr());

        botAleatoire.faireActionSpecialRole();
        assertEquals(3, botAleatoire.getOr());
    }
    @Test
    void CondoQuartierNonRougeTestAleatoire() {
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
    void CondoQuartierRougeTestConsChere(){
        botConstruitChere.setRole(track);
        botConstruitChere.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botConstruitChere.getOr());

        botConstruitChere.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botConstruitChere.getQuartierMain().get(4));
        assertTrue(botConstruitChere.getQuartiersConstruits().isEmpty());

        botConstruitChere.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botConstruitChere.getQuartiersConstruits().get(0));
        assertEquals(4, botConstruitChere.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botConstruitChere.getOr());
        botConstruitChere.faireActionSpecialRole();
        assertEquals(2, botConstruitChere.getOr());

        botConstruitChere.faireActionSpecialRole();
        assertEquals(3, botConstruitChere.getOr());
    }
    @Test
    void CondoQuartierNonRougeTestConsChere() {
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
    void CondoQuartierRougeTestConsVite(){
        botConstruitVite.setRole(track);
        botConstruitVite.changerOr(2); // 4 d'or au total (assez pour caserne)
        assertEquals(4, botConstruitVite.getOr());

        botConstruitVite.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botConstruitVite.getQuartierMain().get(4));
        assertTrue(botConstruitVite.getQuartiersConstruits().isEmpty());

        botConstruitVite.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        assertSame(Quartier.TERRAIN_DE_BATAILLE, botConstruitVite.getQuartiersConstruits().get(0));
        assertEquals(4, botConstruitVite.getQuartierMain().size());

        //Terrain de bataille est un quartier rouge, il doit avoir 1 or en plus
        assertEquals(1, botConstruitVite.getOr());
        botConstruitVite.faireActionSpecialRole();
        assertEquals(2, botConstruitVite.getOr());

        botConstruitVite.faireActionSpecialRole();
        assertEquals(3, botConstruitVite.getOr());
    }
    @Test
    void CondoQuartierNonRougeTestConsVite() {
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

    @Test
    void CondottiereConstruitEcoleDeMagie(){
        botAleatoire.changerOr(10);      //il a 12 ors
        botAleatoire.setRole(track);
        botAleatoire.ajoutQuartierMain(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, botAleatoire.getQuartierMain().get(4));
        assertTrue(botAleatoire.getQuartiersConstruits().isEmpty());

        botAleatoire.ajoutQuartierConstruit(Quartier.ECOLE_DE_MAGIE);
        assertSame(Quartier.ECOLE_DE_MAGIE, botAleatoire.getQuartiersConstruits().get(0));
        assertEquals(4, botAleatoire.getQuartierMain().size());

        assertEquals(6, botAleatoire.getOr());
        botAleatoire.faireActionSpecialRole();
        assertEquals(7, botAleatoire.getOr()); // ecole de magie compte pour rouge
    }

    @Test
    void donjonIndestructible(){

        botAleatoire.setRole(track);       //botAleatoire est le magicien
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));

        doReturn(1).when(botAleatoire).randInt(3);      //on force à viser de dernier bot aka botconstruitVite
        botConstruitVite.changerOr(10);
        botAleatoire.changerOr(10);
        botConstruitVite.ajoutQuartierMain(Quartier.DONJON);            // le seul quartier à detruire est le donjon, botAleatoire a assez d'argent pour le detruire
        botConstruitVite.ajoutQuartierConstruit(Quartier.DONJON);
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeCondottiere(track);
        verify(track, times(0)).destructionQuartier(eq(botAleatoire), eq(botConstruitVite), any());         //verifie que l'on a pas detruit le batiment
        assertEquals(Quartier.DONJON, botConstruitVite.getQuartiersConstruits().get(0));        //verifie présence donjon
    }


    @Test
    void testCimetiere(){
        botAleatoire.setRole(track);       //botAleatoire est le magicien
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));

        ArrayList<Quartier> construit = new ArrayList<>();
        construit.add(Quartier.TAVERNE);
        botConstruitVite.setQuartierConstruit(construit);       //taverne pour botconstruitvite
        assertEquals(1, botConstruitVite.getQuartiersConstruits().size());

        ArrayList<Quartier> cimetiere = new ArrayList<>();
        cimetiere.add(Quartier.CIMETIERE);
        botAleatoire2.setQuartierConstruit(cimetiere);          // cimetiere pour aleatoire2
        assertEquals(1, botAleatoire2.getQuartiersConstruits().size());
        int orAvant = botAleatoire2.getOr();


        doReturn(1).when(botAleatoire).randInt(3);      //on force aleatoire à viser de dernier bot aka botconstruitVite
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeCondottiere(track);
        verify(track).destructionQuartier(botAleatoire, botConstruitVite, Quartier.TAVERNE);
        verify(botAleatoire2).quartierCimetiere(Quartier.TAVERNE);


        assertEquals(0, botConstruitVite.getQuartiersConstruits().size());
        assertEquals(2, botAleatoire2.getQuartiersConstruits().size());
        assertEquals(orAvant-1, botAleatoire2.getOr());


    }
    @Test
    void scoreTest(){
        botAleatoire.setRole(track);
        botConstruitChere.ajoutQuartierMain(Quartier.TEMPLE);
        botConstruitChere.ajoutQuartierConstruit(Quartier.TEMPLE);
        assertEquals(botConstruitChere.getScore(),1);
        track.destructionQuartier(botAleatoire,botConstruitChere,Quartier.TEMPLE);
        assertEquals(botConstruitChere.getScore(),0);
    }


}