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
        botAleatoire.setRole(track);       //botAleatoire est le magicien
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));

        doReturn(2).when(botAleatoire).randInt(3);      //on force à viser de dernier bot aka
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeCondottiere(track);

    }
    @Test
    public void CondottiereConstruitEcoleDeMagie(){
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
        assertEquals(7, botAleatoire.getOr()); // ecole de magie compte pour jaune n'est pas jaune
    }

    @Test
    public void donjonIndestructible(){

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
}