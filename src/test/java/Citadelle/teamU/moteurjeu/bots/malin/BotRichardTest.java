package Citadelle.teamU.moteurjeu.bots.malin;

import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurjeu.Tour;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.spi.SoundbankReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class BotRichardTest {
    BotRichard bot;
    Pioche pioche;

    List<Bot> botliste;
    Bot botAleatoire;
    Bot botConstruitChere;
    Bot botFocusRoi;
    Tour tour;

    @BeforeEach
    public void setBot() {
        pioche = spy(new Pioche());
        bot = spy(new BotRichard(pioche));
        botliste = new ArrayList<>();
        tour = new Tour(botliste);
        botAleatoire = new BotAleatoire(pioche);
        botConstruitChere = new BotConstruitChere(pioche);
        botFocusRoi = new BotFocusRoi(pioche);
        botliste.add(bot);
        botliste.add(botAleatoire);
        botliste.add(botConstruitChere);
        botliste.add(botFocusRoi);


    }

    @Test
        //tester si le bot Richard choisit soit l'assassin ou l'architecte si il est le premier à choisir
    void testPasPremierAChoisir() {


        bot.setOrdreChoixRole(4);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        tour.prochainTour();
        System.out.println(tour.getRolesTemp());
        assertFalse(bot.getPremierAChoisir());




        }


    @Test
    void testPremierAChoisir() {


        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);

        tour.prochainTour();
        System.out.println(tour.getRolesTemp());
        assertTrue(bot.getPremierAChoisir());
    }
    @Test
    void testChoixArchitecteAvance(){
        List<Role> roles = new ArrayList<>();
        roles.add(new Assassin(botliste,roles));
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(new Pretre(botliste));
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        //roles.add(new Condottiere(botliste));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        //CAS ou un bot avance qui peut prendre architecte
        botAleatoire.changerOr(70);
        botAleatoire.ajoutQuartierMain(Quartier.OBSERVATOIRE);
        botAleatoire.ajoutQuartierMain(Quartier.TEMPLE);
        botAleatoire.ajoutQuartierMain(Quartier.CHATEAU);
        botAleatoire.ajoutQuartierMain(Quartier.UNIVERSITE);
        botAleatoire.ajoutQuartierMain(Quartier.MARCHE);
        botAleatoire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        botAleatoire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);
        botAleatoire.ajoutQuartierConstruit(Quartier.TEMPLE);
        botAleatoire.ajoutQuartierConstruit(Quartier.CHATEAU);
        botAleatoire.ajoutQuartierConstruit(Quartier.UNIVERSITE);
        botAleatoire.ajoutQuartierConstruit(Quartier.MARCHE);
        System.out.println(botAleatoire.getQuartiersConstruits());
        bot.setRole(roles.get(2));
        tour.setRolesTemp(roles);
        bot.setNbTour(34);
        tour.distributionRoles();
        System.out.println(bot.getRole());
        assertTrue(bot.getRole() instanceof Assassin );

    }
    @Test
    void testChoixRoleArchitecteAvance2(){
        List<Role> roles = new ArrayList<>();
        //roles.add(new Assassin(botliste,roles));
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        roles.add(new Pretre(botliste));
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        //roles.add(new Condottiere(botliste));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        //CAS ou un bot avance qui peut prendre architecte
        botAleatoire.changerOr(70);
        botAleatoire.ajoutQuartierMain(Quartier.OBSERVATOIRE);
        botAleatoire.ajoutQuartierMain(Quartier.TEMPLE);
        botAleatoire.ajoutQuartierMain(Quartier.CHATEAU);
        botAleatoire.ajoutQuartierMain(Quartier.UNIVERSITE);
        botAleatoire.ajoutQuartierMain(Quartier.MARCHE);
        botAleatoire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);
        botAleatoire.ajoutQuartierConstruit(Quartier.BIBLIOTHEQUE);
        botAleatoire.ajoutQuartierConstruit(Quartier.TEMPLE);
        botAleatoire.ajoutQuartierConstruit(Quartier.CHATEAU);
        botAleatoire.ajoutQuartierConstruit(Quartier.UNIVERSITE);
        botAleatoire.ajoutQuartierConstruit(Quartier.MARCHE);
        System.out.println(botAleatoire.getQuartiersConstruits());
        bot.setRole(roles.get(2));
        tour.setRolesTemp(roles);
        bot.setNbTour(34);
        tour.distributionRoles();
        System.out.println(bot.getRole());
        assertTrue(bot.getRole() instanceof Architecte );
    }



    }
