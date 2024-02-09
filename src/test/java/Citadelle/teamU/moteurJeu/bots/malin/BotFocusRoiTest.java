package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotFocusRoiTest {

    private BotFocusRoi bot;
    Pioche pioche;
    List<Bot> botliste;

    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = spy(new BotFocusRoi(pioche));
        botliste = new ArrayList<>();
        botliste.add(bot);
    }

    @Test
    void testChoixDeCartesJaunes(){
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.TEMPLE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.PALAIS, bot.getQuartierMain().get(0));    //il garde le palais et pas la taverne
    }

    @Test
    void testChoixDeCartes(){
        doReturn(Quartier.TERRAIN_DE_BATAILLE, Quartier.TEMPLE, Quartier.PORT).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.TEMPLE, bot.getQuartierMain().get(0));    //il garde le temple car moins cher
    }

    @Test
    void testChoixDeCartesEtLaboratoire(){
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.TEMPLE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.PALAIS, bot.getQuartierMain().get(0));    //il garde le palais et pas le taverne ni le temple
    }

    @Test
    void testDecisionDeBase(){
        List<Quartier> quartiersMain = new ArrayList<>();
        quartiersMain.add(Quartier.TAVERNE);
        quartiersMain.add(Quartier.MANOIR);         //il a pas assez d'argent pour le construire donc il est censé piocher
        quartiersMain.add(Quartier.MARCHE);
        quartiersMain.add(Quartier.COMPTOIR);
        bot.setQuartierMain(quartiersMain);

        bot.construire();       //censé ne rien construire car pas assez d'argent pour construire manoir
        assertEquals(0, bot.getQuartiersConstruits().size());

        bot.faireActionDeBase();
        verify(bot).changerOr(2);   //verifie qu'on pioche
        assertEquals(2+2, bot.getOr());

        bot.construire();        //il a assez d'argent donc il doit construire son manoir
        verify(bot).ajoutQuartierConstruit(Quartier.MANOIR);
        assertEquals(Quartier.MANOIR, bot.getQuartiersConstruits().get(0));
        assertEquals(4-3, bot.getOr());

    }

    @Test
    void testChoisirCartes(){
        bot.setQuartiersConstruits(List.of(Quartier.OBSERVATOIRE));
        List<Quartier> choix = bot.choisirCarte(List.of(Quartier.TAVERNE, Quartier.PALAIS, Quartier.TERRAIN_DE_BATAILLE));
        assertEquals(Quartier.PALAIS, choix.get(0));

        List<Quartier> choix2 = bot.choisirCarte(List.of(Quartier.MANUFACTURE, Quartier.TAVERNE, Quartier.TERRAIN_DE_BATAILLE));
        assertEquals(Quartier.TAVERNE, choix2.get(0));

        bot.setQuartiersConstruits(List.of(Quartier.BIBLIOTHEQUE));
        List<Quartier> choix3 = bot.choisirCarte(List.of(Quartier.TAVERNE, Quartier.MANUFACTURE, Quartier.TERRAIN_DE_BATAILLE));
        assertEquals(List.of(Quartier.TAVERNE, Quartier.MANUFACTURE, Quartier.TERRAIN_DE_BATAILLE), choix3);
    }

    @Test
    void testChoixRole(){
        doReturn(0).when(bot).randInt(2);
        bot.choisirRole(new ArrayList<>(List.of(new Assassin(botliste, new ArrayList<>()), new Condottiere(botliste))));
        assertEquals(Assassin.class, bot.getRole().getClass());

        bot.setNbQuartiersJaunesConstruits(2);
        bot.choisirRole(new ArrayList<>(List.of(new Voleur(botliste, new ArrayList<>()), new Condottiere(botliste))));
        assertEquals(Voleur.class, bot.getRole().getClass());
    }

    @Test
    void testContruire(){
        bot.changerOr(-4);
        bot.setQuartierMain(new ArrayList<>(List.of(Quartier.PALAIS, Quartier.TAVERNE, Quartier.TERRAIN_DE_BATAILLE)));
        Quartier quartier = bot.construire();
        assertNull(quartier);
        bot.changerOr(70);
        quartier = bot.construire();
        assertEquals(Quartier.PALAIS, quartier);

        bot.setQuartierMain(new ArrayList<>(List.of(Quartier.OBSERVATOIRE, Quartier.TAVERNE, Quartier.TERRAIN_DE_BATAILLE)));
        quartier = bot.construire();
        assertEquals(Quartier.OBSERVATOIRE, quartier);


    }

}