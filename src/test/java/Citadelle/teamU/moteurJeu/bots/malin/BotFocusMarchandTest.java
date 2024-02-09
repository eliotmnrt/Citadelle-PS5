package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.Assassin;
import Citadelle.teamU.cartes.roles.Condottiere;
import Citadelle.teamU.cartes.roles.Magicien;
import Citadelle.teamU.cartes.roles.Voleur;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotFocusMarchandTest {

    private BotFocusMarchand bot;
    Pioche pioche;
    List<Bot> botliste;

    @BeforeEach
    public void setBot() {
        pioche = spy(new Pioche());
        bot = spy(new BotFocusMarchand(pioche));
        botliste = new ArrayList<>();
        botliste.add(bot);
    }

    @Test
    void testChoixDeCartesVertes() {
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.EGLISE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.TAVERNE, bot.getQuartierMain().get(0));    //il garde la taverne
    }

    @Test
    void testChoixDeCartes() {
        doReturn(Quartier.TERRAIN_DE_BATAILLE, Quartier.EGLISE, Quartier.PALAIS).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.EGLISE, bot.getQuartierMain().get(0));    //il garde le temple car moins cher
    }

    @Test
    void testChoixDeCartesEtLaboratoire() {
        doReturn(Quartier.TAVERNE, Quartier.PALAIS, Quartier.EGLISE).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.TAVERNE, bot.getQuartierMain().get(0));    //il garde la taverne et pas le palais ni l eglise
    }

    @Test
    void testEntreCartesVertes() {

        doReturn(Quartier.COMPTOIR, Quartier.TAVERNE, Quartier.COMPTOIR).when(pioche).piocherQuartier();
        bot.setQuartierMain(new ArrayList<>()); // main vide
        doReturn(1).when(bot).randInt(3);       //on le force à piocher des quartiers

        bot.changerOr(6);
        bot.faireActionDeBase();        //il pioche 2 quartier
        assertEquals(1, bot.getQuartierMain().size());
        assertEquals(Quartier.COMPTOIR, bot.getQuartierMain().get(0));    //il garde la taverne et pas le palais ni l eglise
    }

    @Test
    void testDecisionDeBase() {
        List<Quartier> quartiersMain = new ArrayList<>();
        quartiersMain.add(Quartier.PRISON);
        quartiersMain.add(Quartier.MANOIR);         //il a pas assez d'argent pour le construire donc il est censé piocher
        quartiersMain.add(Quartier.TEMPLE);
        quartiersMain.add(Quartier.COMPTOIR);
        bot.setQuartierMain(quartiersMain);

        bot.construire();       //censé ne rien construire car pas assez d'argent pour construire comptoir
        assertEquals(0, bot.getQuartiersConstruits().size());

        bot.faireActionDeBase();
        verify(bot).changerOr(2);   //verifie qu'on pioche
        assertEquals(2 + 2, bot.getOr());

        bot.construire();        //il a assez d'argent donc il doit construire le comptoir
        verify(bot).ajoutQuartierConstruit(Quartier.COMPTOIR);
        assertEquals(Quartier.COMPTOIR, bot.getQuartiersConstruits().get(0));
        assertEquals(4 - 3, bot.getOr());

    }

    @Test
    void testChoixRole() {
        doReturn(0).when(bot).randInt(2);
        bot.choisirRole(new ArrayList<>(List.of(new Assassin(botliste, new ArrayList<>()), new Condottiere(botliste))));
        assertEquals(Assassin.class, bot.getRole().getClass());
        bot.choisirRole(new ArrayList<>(List.of(new Voleur(botliste, new ArrayList<>()), new Condottiere(botliste))));
        assertEquals(Voleur.class, bot.getRole().getClass());
    }

    @Test
    void testContruire() {
        bot.changerOr(-4);
        bot.setQuartierMain(new ArrayList<>(List.of(Quartier.PALAIS, Quartier.TAVERNE, Quartier.TERRAIN_DE_BATAILLE)));
        Quartier quartier = bot.construire();
        assertNull(quartier);
        bot.changerOr(70);
        quartier = bot.construire();
        assertEquals(Quartier.TAVERNE, quartier);

        bot.setQuartierMain(new ArrayList<>(List.of(Quartier.OBSERVATOIRE, Quartier.TAVERNE, Quartier.TERRAIN_DE_BATAILLE)));
        quartier = bot.construire();
        assertEquals(Quartier.TERRAIN_DE_BATAILLE, quartier);


    }

    @Test
    void testChoisirCartes() {

        bot.setQuartiersConstruits(List.of(Quartier.OBSERVATOIRE));
        List<Quartier> choix = bot.choisirCarte(List.of(Quartier.TAVERNE, Quartier.PALAIS, Quartier.TERRAIN_DE_BATAILLE));
        assertEquals(Quartier.TAVERNE, choix.get(0));



        bot.setQuartiersConstruits(List.of(Quartier.BIBLIOTHEQUE));
        List<Quartier> choix3 = bot.choisirCarte(List.of(Quartier.TAVERNE, Quartier.MANUFACTURE, Quartier.TERRAIN_DE_BATAILLE));
        assertEquals(List.of(Quartier.TAVERNE, Quartier.MANUFACTURE, Quartier.TERRAIN_DE_BATAILLE), choix3);
    }

    @Test
    void actionSpecialeMagicienQueVertTest(){
        bot.setRole(new Magicien(botliste));
        ArrayList<Quartier> queVert = new ArrayList<>(Arrays.asList(Quartier.HOTEL_DE_VILLE,Quartier.COMPTOIR,Quartier.MARCHE));
        bot.setQuartierMain(queVert);
        bot.faireActionSpecialRole();
        assertEquals(bot.getQuartierMain(),queVert);
    }
    @Test
    void actionSpecialeMagicienPiocheTest(){
        bot.setRole(new Magicien(botliste));
        doReturn(Quartier.CIMETIERE).when(pioche).piocherQuartier();
        ArrayList<Quartier> pasqueVert = new ArrayList<>(Arrays.asList(Quartier.HOTEL_DE_VILLE,Quartier.COMPTOIR,Quartier.MARCHE));
        bot.setQuartierMain(pasqueVert);
        bot.faireActionSpecialRole();
        pasqueVert.remove(Quartier.MARCHE);
        pasqueVert.add(Quartier.CIMETIERE);
        assertEquals(bot.getQuartierMain(),pasqueVert);
    }

}


