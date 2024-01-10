package Citadelle.teamU.cartes.roles;

import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.BotConstruitVite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class VoleurTest {

    private BotAleatoire botAleatoire, botAleatoire2;
    private BotConstruitVite botConstruitVite;
    private BotConstruitChere botConstruitChere;
    private ArrayList<Bot> botliste;
    ArrayList<Role> rolesTemp;
    Voleur track;
    Roi roi = new Roi(botliste);
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

        rolesTemp = new ArrayList<>();
        rolesTemp.add(new Magicien(botliste));
        rolesTemp.add(roi);
        rolesTemp.add(new Pretre(botliste));
        rolesTemp.add(new Marchand(botliste));
        track = Mockito.spy(new Voleur(botliste, rolesTemp));
        rolesTemp.add(track);
    }

    @Test
    void testBotAleatoireVole(){
        botAleatoire.setRole(track);                        //voleur
        botAleatoire2.setRole(track.getRoles().get(0));     //magicien
        botConstruitChere.setRole(track.getRoles().get(1)); //Roi
        botConstruitVite.setRole(track.getRoles().get(2));  //Pretre

        int orAvantVol = botAleatoire.getOr();
        int orVole = botAleatoire2.getOr();
        doReturn(0).when(botAleatoire).randInt(anyInt());   //on force le hasard à choisir le roi, pour certaines raisons ca correspond au 0;

        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeVoleur(track);
        verify(track).voler(botAleatoire, roi);
        assertEquals(0,botConstruitChere.getOr());
        assertEquals(orVole + orAvantVol, botAleatoire.getOr() + botAleatoire.getOrProchainTour());
    }

    @Test
    void botPasConVoleRolesRestants(){
        botAleatoire.setRole(track.getRoles().get(1));      //roi
        botAleatoire2.setRole(track.getRoles().get(0));     //magicien
        botConstruitChere.setRole(track);                   //magicien
        botConstruitVite.setRole(track.getRoles().get(2));  //Pretre
        rolesTemp.remove(1);        //on enleve le roi car deja pris
        rolesTemp.remove(0);        //on enleve le magicien car deja pris

        //il reste dans rolestemp: marchand pretre
        botConstruitChere.setRolesRestants(rolesTemp);

        doReturn(0).when(botConstruitChere).randInt(anyInt());     //on force le hasard pour que le voleur vole le pretre
        int orAvantVol = botConstruitChere.getOr();
        int orVole = botConstruitVite.getOr();

        botConstruitChere.faireActionSpecialRole();
        verify(botConstruitChere).actionSpecialeVoleur(track);
        verify(track).voler(botConstruitChere, rolesTemp.get(0));
        assertEquals(0,botConstruitVite.getOr());
        assertEquals(orVole + orAvantVol, botConstruitChere.getOr() + botConstruitChere.getOrProchainTour());
    }

}