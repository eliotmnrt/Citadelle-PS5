package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BotConstruitViteTest {

    private Pioche pioche;
    private BotConstruitVite bot;
    ArrayList<Bot> botliste;
    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = new BotConstruitVite(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot);
    }
    @Test
    public void prendreOr(){
        when(pioche.piocherQuartier()).thenReturn(Quartier.TAVERNE);
        while(bot.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(4,bot.getOr());
    }
    /**@Test
    public void prendreQuartier(){
        //piocheMock = mockStatic(Pioche.class);
        when(Pioche.piocherQuartier()).thenReturn(Quartier.CIMETIERE);
        while(bot.getQuartierMain().size()!=0){
            Pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(2,bot.getOr());
        assertEquals(2,bot.getQuartierMain().size()); //Cimetiere et celui qu'il a piocher
    }**/
    @Test
    public void quartierMoinsChereTest(){
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.quartierMain.size()); //4 de base + 3 ajouts
        bot.setRole(new Roi(botliste));
        bot.construire();
        assertEquals(1, bot.quartierConstruit.get(0).getCout());
        assertEquals(1, bot.quartierConstruit.size());
        assertEquals(6, bot.quartierMain.size());
    }
    @Test
    public void actionMagicienAvecBotTest(){
        //notre bot à 0 carte dans sa main
        //L'autre bot à 1 carte (une église)

        while(bot.getQuartierMain().size()!=0){bot.getPioche().remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(bot1.getQuartierMain().size()!=0){bot.getPioche().remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1

        bot1.ajoutQuartierMain(Quartier.EGLISE);

        arrayBot.add(bot1);

        bot.actionSpecialeMagicien(new Magicien(arrayBot));
        assertEquals(1, bot.quartierMain.size());
        assertTrue(bot.getQuartierMain().contains(Quartier.EGLISE));
    }
    @Test
    public void actionMagicienAvecPiocheTest(){
        //notre bot à 2 carte dans sa main
        //L'autre bot à 1 carte (une église)
        //Il échange 2 cartes avec la pioche (qui ne renvoie que des monastere)
        //piocheMock = mockStatic(Pioche.class);
        when(pioche.piocherQuartier()).thenReturn(Quartier.MONASTERE);

        while(bot.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide
        bot.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        bot.ajoutQuartierMain(Quartier.COMPTOIR);

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(bot1.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1
        bot1.ajoutQuartierMain(Quartier.EGLISE);

        arrayBot.add(bot1);

        assertEquals(2, bot.quartierMain.size());
        bot.actionSpecialeMagicien(new Magicien(arrayBot));
        assertEquals(2, bot.quartierMain.size());
        assertTrue(bot.getQuartierMain().contains(Quartier.MONASTERE));
    }
    @Test
    public void voleurTest(){
        //On est pas le dernier
        ArrayList<Bot> arrayBot = new ArrayList<>();
        ArrayList<Role> arrayRole = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);
        arrayBot.add(bot1);
        bot1.changerOr(4); //Bot 1 à 6 or
        Roi roi = new Roi(arrayBot);
        arrayRole.add(roi);
        arrayRole.add(roi);
        //2 roles pour qu'il ne fasse pas au hasard avec tout le monde (pas condition reel il va forcement tomber sur le bot1)
        bot1.setRole(roi);
        bot.setRolesRestants(arrayRole);
        bot.actionSpecialeVoleur(new Voleur(arrayBot,arrayRole));
        assertEquals(2, bot.getOr());
        assertEquals(6, bot.orProchainTour);
        bot.choisirRole(arrayRole); //Le tour d'après
        assertEquals(8, bot.getOr());
    }
    @Test
    public void voleurDernierValideTest(){
        //On est dernier et on choisit un role que qq a
        ArrayList<Bot> arrayBot = new ArrayList<>();
        ArrayList<Role> arrayRole = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);
        arrayBot.add(bot1);
        bot1.changerOr(4); //Bot 1 à 6 or
        Roi roi = new Roi(arrayBot);
        arrayRole.add(roi);
        bot1.setRole(roi);


        BotConstruitVite botSpy = spy(bot);
        botSpy.setRolesRestants(arrayRole);
        arrayRole.add(roi);
        doReturn(0).when(botSpy).randInt(anyInt());
        //Il choisit de voler le dernier

        botSpy.actionSpecialeVoleur(new Voleur(arrayBot,arrayRole));
        assertEquals(2, botSpy.getOr());
        assertEquals(6, botSpy.orProchainTour);
        botSpy.choisirRole(arrayRole); //Le tour d'après
        assertEquals(8, botSpy.getOr());
    }
    @Test
    public void pasConstruireTest(){
        while(bot.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide pour le bot1
        assertNull(bot.construire());
        //rien dans la main il ne peut pas construire
    }
    @Test
    public void actionSpecialeCondottiere(){
        BotAleatoire botPleinDeQuartier = new BotAleatoire(pioche);
        BotAleatoire botPasBcpQuartier = new BotAleatoire(pioche);
        while(botPleinDeQuartier.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(botPleinDeQuartier.getQuartierMain().remove(0));
        } // main vide
        botPleinDeQuartier.changerOr(100);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.TEMPLE); //coute 1 il doit detruire lui
        botPleinDeQuartier.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.OBSERVATOIRE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.FORTERESSE);

        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.TEMPLE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.OBSERVATOIRE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.FORTERESSE);

        ArrayList<Bot> arrayBot = new ArrayList<>();
        arrayBot.add(bot);
        arrayBot.add(botPleinDeQuartier);
        arrayBot.add(botPasBcpQuartier);
        bot.actionSpecialeCondottiere(new Condottiere(arrayBot));
        assertFalse(botPleinDeQuartier.getQuartiersConstruits().contains(Quartier.TEMPLE));
    }
    @Test
    public void actionSpecialeCondottierePerteOr(){
        BotAleatoire botPleinDeQuartier = new BotAleatoire(pioche);
        BotAleatoire botPasBcpQuartier = new BotAleatoire(pioche);
        while(botPleinDeQuartier.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(botPleinDeQuartier.getQuartierMain().remove(0));
        } // main vide
        botPleinDeQuartier.changerOr(100);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.ECHOPPE); //coute 2 il doit detruire lui
        botPleinDeQuartier.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.OBSERVATOIRE);
        botPleinDeQuartier.ajoutQuartierMain(Quartier.FORTERESSE);

        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.ECHOPPE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.OBSERVATOIRE);
        botPleinDeQuartier.ajoutQuartierConstruit(Quartier.FORTERESSE);

        ArrayList<Bot> arrayBot = new ArrayList<>();
        arrayBot.add(bot);
        arrayBot.add(botPleinDeQuartier);
        arrayBot.add(botPasBcpQuartier);
        bot.actionSpecialeCondottiere(new Condottiere(arrayBot));
        assertFalse(botPleinDeQuartier.getQuartiersConstruits().contains(Quartier.ECHOPPE));
        assertEquals(1,bot.getOr()); //Il a 2 or de base et le quartier coute 2 a construire donc 1 a détruire
    }
}