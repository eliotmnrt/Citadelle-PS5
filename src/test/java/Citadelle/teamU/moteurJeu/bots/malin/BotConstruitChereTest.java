package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Pioche;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doReturn;

class BotConstruitChereTest {
    private BotConstruitChere bot;
    private Pioche pioche;
    //Condottiere track;
    @BeforeEach
    public void setBot(){
        pioche = spy(new Pioche());
        bot = spy(new BotConstruitChere(pioche));
        List<Bot> botliste = new ArrayList<>();
        botliste.add(bot);
        //track = Mockito.spy(new Condottiere(botliste));
    }
    /**Test
    public void prendreOr(){c
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
    void prendreQuartier(){
        when(pioche.piocherQuartier()).thenReturn(Quartier.TAVERNE);
        while(!bot.getQuartierMain().isEmpty()){
            pioche.remettreDansPioche(bot.getQuartierMain().remove(0));
        } // main vide
        bot.ajoutQuartierMain(pioche.piocherQuartier());
        bot.faireActionDeBase();
        assertEquals(2,bot.getOr());
        assertEquals(2,bot.getQuartierMain().size()); //Cimetiere et celui qu'il a piocher
    }
    @Test
    void quartierPlusChereTest(){
        while(!bot.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide pour le bot1
        bot.ajoutQuartierMain(Quartier.TEMPLE);
        bot.ajoutQuartierMain(Quartier.PRISON);
        bot.ajoutQuartierMain(Quartier.CATHEDRALE);
        assertEquals(3, bot.getQuartierMain().size()); //4 de base + 3 ajouts
        //bot.setRole(new Roi(botliste));
        bot.changerOr(10);

        assertEquals(Quartier.CATHEDRALE, bot.construire());
        assertEquals(5, bot.getQuartiersConstruits().get(0).getCout());
        assertEquals(1, bot.getQuartiersConstruits().size());
        assertEquals(2, bot.getQuartierMain().size());

    }

    @Test
    void testChoixCartesObservatoire(){
        bot.setQuartierMain(new ArrayList<>());
        bot.setQuartiersConstruits(new ArrayList<>(List.of(Quartier.OBSERVATOIRE)));
        doReturn(Quartier.TEMPLE, Quartier.TERRAIN_DE_BATAILLE, Quartier.PALAIS).when(pioche).piocherQuartier();

        bot.faireActionDeBase();

        assertEquals(Quartier.PALAIS, bot.getQuartierMain().get(0));
    }

    @Test
    void actionMagicienAvecBotTest(){
        //notre bot à 0 carte dans sa main
        //L'autre bot à 1 carte (une église)

        while(!bot.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide

        ArrayList<Bot> arrayBot = new ArrayList<>();
        BotAleatoire bot1 = new BotAleatoire(pioche);

        while(!bot1.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot1.getQuartierMain().remove(0));} // main vide pour le bot1

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


        bot.setRolesRestants(arrayRole);
        doReturn(0).when(bot).randInt(anyInt());
        arrayRole.add(roi);
        //Il choisit de voler le dernier

        bot.actionSpecialeVoleur(new Voleur(arrayBot,arrayRole));
        assertEquals(2, bot.getOr());
        assertEquals(6, bot.getOrProchainTour());
        bot.choisirRole(arrayRole); //Le tour d'après
        assertEquals(8, bot.getOr());
    }
    @Test
    void pasConstruireTest(){
        while(!bot.getQuartierMain().isEmpty()){pioche.remettreDansPioche(bot.getQuartierMain().remove(0));} // main vide pour le bot1
        assertNull(bot.construire());
        //rien dans la main il ne peut pas construire
    }
    @Test
    void condottierePeutDetruire(){
        BotAleatoire aQuartierADetruire = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire = new BotAleatoire(pioche);
        while(!aQuartierADetruire.getQuartierMain().isEmpty()){
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



        while(!aPasQuartierADetruire.getQuartierMain().isEmpty()){
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
    void condottierePeutPasDetruire(){
        BotAleatoire aPasQuartierADetruire1 = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire2 = new BotAleatoire(pioche);
        while(!aPasQuartierADetruire1.getQuartierMain().isEmpty()){
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


        List<Quartier> avantDestruction1= aPasQuartierADetruire1.getQuartiersConstruits();
        List<Quartier> avantDestruction2= aPasQuartierADetruire2.getQuartiersConstruits();


        while(!aPasQuartierADetruire2.getQuartierMain().isEmpty()){
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
    void actionSpecialeCondottierePasPerteOr(){
        BotAleatoire aQuartierADetruire = new BotAleatoire(pioche);
        BotAleatoire aPasQuartierADetruire = new BotAleatoire(pioche);
        while(!aQuartierADetruire.getQuartierMain().isEmpty()){
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



        while(!aPasQuartierADetruire.getQuartierMain().isEmpty()){
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

    @Test
    void testManufacture(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.MANUFACTURE);
        bot.setQuartierMain(main);    //main = manufacture

        assertSame(Quartier.MANUFACTURE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.MANUFACTURE);       //lui coute 5ors, lui en reste 7
        assertSame(Quartier.MANUFACTURE, bot.getQuartiersConstruits().get(0));
        assertEquals(0, bot.getQuartierMain().size());

        bot.quartiersViolets();
        assertEquals(3, bot.getQuartierMain().size());
        assertEquals(4, bot.getOr());
    }

    @Test
    void testLaboratoire(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.LABORATOIRE);
        main.add(Quartier.MARCHE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.LABORATOIRE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.LABORATOIRE);       //lui coute 5ors, lui en reste 7
        bot.ajoutQuartierConstruit(Quartier.MARCHE);       //lui coute 2ors, lui en reste 5

        assertSame(Quartier.LABORATOIRE, bot.getQuartiersConstruits().get(0));
        assertEquals(1, bot.getQuartierMain().size());

        bot.quartiersViolets();     //on echange la carte marché, en doublon contre 1or

        assertEquals(0, bot.getQuartierMain().size());
        assertEquals(6, bot.getOr());
    }

    @Test
    void testBibliotheque(){
        bot.changerOr(10);      //il a 12 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.BIBLIOTHEQUE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartierMain().get(0));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);       //lui coute 6ors, lui en reste 6

        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartiersConstruits().get(0));
        assertEquals(1, bot.getQuartierMain().size());

        doReturn(0).when(bot).randInt(2);       //on le force à choisir de piocher des quartiers au lieu de prendre 2ors

        bot.faireActionDeBase();    //il pioche ses 2 quartiers et les garde car il a construit la bibliotheque

        assertEquals(3, bot.getQuartierMain().size());      //il a mtn 1 + 2 quartiers dans sa main
        assertEquals(6, bot.getOr());
    }
    @Test
    void testBibliothequeETObservatoire(){
        bot.changerOr(20);      //il a 22 ors
        ArrayList<Quartier> main = new ArrayList<>();
        main.add(Quartier.BIBLIOTHEQUE);
        main.add(Quartier.OBSERVATOIRE);
        main.add(Quartier.MARCHE);
        bot.setQuartierMain(main);


        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartierMain().get(0));
        assertSame(Quartier.OBSERVATOIRE, bot.getQuartierMain().get(1));
        assertTrue(bot.getQuartiersConstruits().isEmpty());

        bot.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);       //lui coute 6ors, lui en reste 16
        bot.ajoutQuartierConstruit(Quartier.OBSERVATOIRE);       //lui coute 5ors, lui en reste 11

        assertSame(Quartier.BIBLIOTHEQUE, bot.getQuartiersConstruits().get(0));
        assertSame(Quartier.OBSERVATOIRE, bot.getQuartiersConstruits().get(1));
        assertEquals(1, bot.getQuartierMain().size());

        doReturn(0).when(bot).randInt(2);       //on le force à choisir de piocher des quartiers au lieu de 2ors

        bot.faireActionDeBase();    //il pioche ses 3 quartiers et les garde car il a construit la bibliotheque

        assertEquals(4, bot.getQuartierMain().size());      //il a mtn 1 + 3 quartiers dans sa main
        assertEquals(11, bot.getOr());
    }
    
}