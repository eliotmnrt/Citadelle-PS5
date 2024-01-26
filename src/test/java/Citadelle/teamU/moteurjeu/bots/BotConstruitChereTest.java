package Citadelle.teamU.moteurjeu.bots;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

class BotConstruitChereTest {
    private BotConstruitChere bot;
    private ArrayList<Bot> botliste;
    private Pioche pioche;
    //Condottiere track;
    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = new BotConstruitChere(pioche);
        botliste = new ArrayList<>();
        botliste.add(bot);
        //track = Mockito.spy(new Condottiere(botliste));
    }
    /**@Test
    public void prendreOr(){
        piocheMock = mockStatic(Pioche.class);
        when(Pioche.piocherQuartier()).thenReturn(Quartier.CIMETIERE);
        while(bot.getQuartierMain().size()!=0){
            Pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(Pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(4,bot.getOr());
    }**/
    @Test
    public void prendreQuartier(){
        when(pioche.piocherQuartier()).thenReturn(Quartier.TAVERNE);
        while(bot.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(2,bot.getOr());
        assertEquals(2,bot.getQuartierMain().size()); //Cimetiere et celui qu'il a piocher
    }
    @Test
    public void quartierPlusChereTest(){
        while(bot.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide pour le bot1
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(3, bot.quartierMain.size()); //4 de base + 3 ajouts
        //bot.setRole(new Roi(botliste));
        bot.changerOr(10);

        assertEquals(Quartier.CATHEDRALE, bot.construire());
        assertEquals(5, bot.quartierConstruit.get(0).getCout());
        assertEquals(1, bot.quartierConstruit.size());
        assertEquals(2, bot.quartierMain.size());

    }
    @Test
    public void actionMagicienAvecBotTest(){
        //notre bot à 0 carte dans sa main
        //L'autre bot à 1 carte (une église)

        while(bot.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(bot1.getQuartierMain().size()!=0){pioche.remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1

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


        BotConstruitChere botSpy = spy(bot);
        botSpy.setRolesRestants(arrayRole);
        doReturn(0).when(botSpy).randInt(anyInt());
        arrayRole.add(roi);
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
    public void condottierePeutDetruire(){
        BotAleatoire aQuartierADetruire = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire = new BotAleatoire(pioche);
        while(aQuartierADetruire.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aQuartierADetruire.getQuartierMain().remove(0));
        } // main vide
        aQuartierADetruire.changerOr(100);
        aQuartierADetruire.ajoutQuartierMain(Quartier.TEMPLE); //coute 1 il doit detruire lui
        aQuartierADetruire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aQuartierADetruire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aQuartierADetruire.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.TEMPLE); //coute 1 il doit detruire lui
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 6
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 5



        while(aPasQuartierADetruire.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aPasQuartierADetruire.getQuartierMain().remove(0));
        } // main vide
        aPasQuartierADetruire.changerOr(100);
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 5



        ArrayList<Bot> arrayBot = new ArrayList<>();
        arrayBot.add(bot);
        arrayBot.add(aQuartierADetruire);
        arrayBot.add(aPasQuartierADetruire);



        Condottiere condott=Mockito.spy(new Condottiere(arrayBot));

        bot.actionSpecialeCondottiere(condott); // dans cette appel de fonctions le condottiere doit detruire le quartier Temple du aQuartierADetruire

        verify(condott).destructionQuartier(bot, aQuartierADetruire,Quartier.TEMPLE);
        assertFalse(aQuartierADetruire.getQuartiersConstruits().contains(Quartier.TEMPLE));
        assertEquals(aQuartierADetruire.getQuartiersConstruits(),new ArrayList<>(List.of(Quartier.TERRAIN_DE_BATAILLE,Quartier.BIBLIOTHEQUE,Quartier.OBSERVATOIRE)));


    }
    @Test
    public void condottierePeutPasDetruire(){
        BotAleatoire aPasQuartierADetruire1 = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire2 = new BotAleatoire(pioche);
        while(aPasQuartierADetruire1.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aPasQuartierADetruire1.getQuartierMain().remove(0));
        } // main vide

        //mettre de l'argent pour construire
        aPasQuartierADetruire1.changerOr(100);

        aPasQuartierADetruire1.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire1.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire1.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------

        aPasQuartierADetruire1.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 2
        aPasQuartierADetruire1.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 5
        aPasQuartierADetruire1.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 3


        ArrayList<Quartier> avantDestruction1= aPasQuartierADetruire1.getQuartiersConstruits();
        ArrayList<Quartier> avantDestruction2= aPasQuartierADetruire2.getQuartiersConstruits();


        while(aPasQuartierADetruire2.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aPasQuartierADetruire2.getQuartierMain().remove(0));
        } // main vide


        aPasQuartierADetruire2.changerOr(100);


        aPasQuartierADetruire2.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire2.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire2.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------
        aPasQuartierADetruire2.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire2.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire2.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 5



        ArrayList<Bot> arrayBot = new ArrayList<>();
        arrayBot.add(bot);
        arrayBot.add(aPasQuartierADetruire1);
        arrayBot.add(aPasQuartierADetruire2);



        Condottiere condott=Mockito.spy(new Condottiere(arrayBot));

        bot.actionSpecialeCondottiere(condott); // dans cette appel de fonctions le condottiere doit detruire le quartier Temple du aQuartierADetruire
        verify(condott, times(0)).destructionQuartier(any(),any(),any());
        assertEquals(aPasQuartierADetruire1.getQuartiersConstruits(),avantDestruction1);
        assertEquals(aPasQuartierADetruire2.getQuartiersConstruits(), avantDestruction2);

    }
    @Test
    public void actionSpecialeCondottierePasPerteOr(){
        BotAleatoire aQuartierADetruire = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire = new BotAleatoire(pioche);
        while(aQuartierADetruire.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aQuartierADetruire.getQuartierMain().remove(0));
        } // main vide


        aQuartierADetruire.changerOr(100);

        aQuartierADetruire.ajoutQuartierMain(Quartier.TEMPLE); //coute 1 il doit detruire lui
        aQuartierADetruire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aQuartierADetruire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aQuartierADetruire.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.TEMPLE); //coute 1 il doit detruire lui
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 6
        aQuartierADetruire.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 5



        while(aPasQuartierADetruire.getQuartierMain().size()!=0){
            pioche.remettreDansPioche(aPasQuartierADetruire.getQuartierMain().remove(0));
        } // main vide
        aPasQuartierADetruire.changerOr(100);
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire.ajoutQuartierMain(Quartier.OBSERVATOIRE); //coute 5
        //------
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.TERRAIN_DE_BATAILLE); //coute 3
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE); // coute 6
        aPasQuartierADetruire.ajoutQuartierConstruit(Quartier.OBSERVATOIRE); //coute 5



        ArrayList<Bot> arrayBot = new ArrayList<>();
        arrayBot.add(bot);
        arrayBot.add(aQuartierADetruire);
        arrayBot.add(aPasQuartierADetruire);

        Condottiere condott=Mockito.spy(new Condottiere(arrayBot));

        int argentAvantDestruction=bot.getOr();
        bot.actionSpecialeCondottiere(condott); // dans cette appel de fonctions le condottiere doit detruire le quartier Temple du aQuartierADetruire

        verify(condott).destructionQuartier(bot, aQuartierADetruire,Quartier.TEMPLE);
        assertEquals(bot.getOr(),argentAvantDestruction);



    }
    
}