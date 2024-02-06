package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BotConstruitViteTest {

    private Pioche pioche;
    private BotConstruitVite bot;
    private ArrayList<Bot> botliste;
    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = new BotConstruitVite(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot);
    }
    @Test
     void prendreOr(){
        when(pioche.piocherQuartier()).thenReturn(Quartier.TAVERNE);
        while(!bot.getQuartierMain().isEmpty()){
            pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(4,bot.getOr());
    }
    /**Test
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
    void quartierMoinsChereTest(){
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(7, bot.getQuartierMain().size()); //4 de base + 3 ajouts
        bot.setRole(new Roi(botliste));
        bot.construire();
        assertEquals(1, bot.getQuartiersConstruits().get(0).getCout());
        assertEquals(1, bot.getQuartiersConstruits().size());
        assertEquals(6, bot.getQuartierMain().size());
    }
    @Test
    void actionMagicienAvecBotTest(){
        //notre bot à 0 carte dans sa main
        //L'autre bot à 1 carte (une église)

        while(!bot.getQuartierMain().isEmpty()){bot.getPioche().remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(!bot1.getQuartierMain().isEmpty()){bot.getPioche().remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1

        bot1.ajoutQuartierMain(Quartier.EGLISE);

        arrayBot.add(bot1);

        bot.actionSpecialeMagicien(new Magicien(arrayBot));
        assertEquals(1, bot.getQuartierMain().size());
        assertTrue(bot.getQuartierMain().contains(Quartier.EGLISE));
    }
    @Test
    void actionMagicienAvecPiocheTest(){
        //notre bot à 2 carte dans sa main
        //L'autre bot à 1 carte (une église)
        //Il échange 2 cartes avec la pioche (qui ne renvoie que des monastere)
        //piocheMock = mockStatic(Pioche.class);
        when(pioche.piocherQuartier()).thenReturn(Quartier.MONASTERE);

        while(!bot.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide
        bot.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        bot.ajoutQuartierMain(Quartier.COMPTOIR);

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(!bot1.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1
        bot1.ajoutQuartierMain(Quartier.EGLISE);

        arrayBot.add(bot1);

        assertEquals(2, bot.getQuartierMain().size());
        bot.actionSpecialeMagicien(new Magicien(arrayBot));
        assertEquals(2, bot.getQuartierMain().size());
        assertTrue(bot.getQuartierMain().contains(Quartier.MONASTERE));
    }
    @Test
    void voleurTest(){
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
        assertEquals(6, bot.getOrProchainTour());
        bot.choisirRole(arrayRole); //Le tour d'après
        assertEquals(8, bot.getOr());
    }

    @Test
    void voleurDernierValideTest(){
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
        assertEquals(6, botSpy.getOrProchainTour());
        botSpy.choisirRole(arrayRole); //Le tour d'après
        assertEquals(8, botSpy.getOr());
    }
    @Test
    void pasConstruireTest(){
        while(!bot.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide pour le bot1
        assertNull(bot.construire());
        //rien dans la main il ne peut pas construire
    }
    @Test
    void actionSpecialeCondottiere(){
        BotAleatoire botPleinDeQuartier = new BotAleatoire(pioche);
        BotAleatoire botPasBcpQuartier = new BotAleatoire(pioche);
        while(!botPleinDeQuartier.getQuartierMain().isEmpty()){
            pioche.remettreDansPioche(botPleinDeQuartier.getQuartierMain().remove(0));
        } // main vide

        botPleinDeQuartier.changerOr(200);
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
    void actionSpecialeCondottierePerteOr(){
        BotAleatoire botPleinDeQuartier = new BotAleatoire(pioche);
        BotAleatoire botPasBcpQuartier = new BotAleatoire(pioche);
        while(!botPleinDeQuartier.getQuartierMain().isEmpty()){
            pioche.remettreDansPioche(botPleinDeQuartier.getQuartierMain().remove(0));
        } // main vide

        botPleinDeQuartier.changerOr(200);
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