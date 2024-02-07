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
    Bot botAleatoire, botConstruitChere, botFocusRoi;
    Tour tour;
    Architecte architecte;

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
        Assassin assassinMock = spy(new Assassin(botliste,roles));
        roles.add(assassinMock);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(new Pretre(botliste));
        //roles.add(new Marchand(botliste));
        Architecte architecteMock = spy(new Architecte(botliste));
        roles.add(architecteMock);
        //roles.add(new Condottiere(botliste));
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        //CAS ou un bot avance qui peut prendre architecte
        botAleatoire.changerOr(70);
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        botAleatoire.setQuartierConstruit(quart);
        botAleatoire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);

        bot.setRole(roles.get(2));  //on lui donne un role pour l'acces à botliste
        List<Role> roleTemp = new ArrayList<>(roles);
        tour.setRolesTemp(roleTemp);
        bot.setNbTour(34);
        tour.distributionRoles();
        System.out.println(bot.getRole());
        assertTrue(bot.getRole() instanceof Assassin );

        bot.faireActionSpecialRole();
        verify(assassinMock).tuer(architecteMock);


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
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        botAleatoire.setQuartierConstruit(quart);
        botAleatoire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);

        bot.setRole(roles.get(2));
        tour.setRolesTemp(roles);
        bot.setNbTour(34);
        tour.distributionRoles();
        System.out.println(bot.getRole());
        assertTrue(bot.getRole() instanceof Architecte );
    }


    @Test
    void testChoixRoleJoueurAvance(){
        List<Role> roles = new ArrayList<>();
        Assassin assassinMock = spy(new Assassin(botliste,roles));
        roles.add(assassinMock);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        Roi roiMock = new Roi(botliste);
        roles.add(roiMock);
        roles.add(new Pretre(botliste));
        roles.add(new Marchand(botliste));
        Architecte architecteMock = spy(new Architecte(botliste));
        roles.add(architecteMock);
        Condottiere condottiereMock = spy(new Condottiere(botliste));
        roles.add(condottiereMock);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        //CAS ou un bot avance qui peut prendre architecte
        botAleatoire.changerOr(70);
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.COMPTOIR);
        botAleatoire.setQuartierConstruit(quart);
        botAleatoire.ajoutQuartierMain(Quartier.BIBLIOTHEQUE);

        bot.setRole(roles.get(2));  //on lui donne un role pour l'acces à botliste
        List<Role> roleTemp = new ArrayList<>(roles);
        roleTemp.remove(3);
        roleTemp.remove(0);
        tour.setRolesTemp(roleTemp);
        bot.setNbTour(34);
        tour.distributionRoles();
        System.out.println(bot.getRole());
        assertTrue(bot.getRole() instanceof Condottiere );

        bot.faireActionSpecialRole();
        verify(condottiereMock).destructionQuartier(bot, botAleatoire, Quartier.TEMPLE);



    }




}
