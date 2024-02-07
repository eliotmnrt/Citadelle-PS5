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
    //tests du choix de roles pour bot richard dans le dernier tour en fonction du joueur qui a presque fini

    @Test
    void ChoixRoleBotPresqueFiniThisEstPremier(){
        //cas ou c'est this le joueur qui a presque fini et que que il a l'ordre 1
        //cas ou il y'a l'assassin, choisir l'assassin
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        roles.add(condottiere);
        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());

        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==assassin);




        roles.remove(0);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        roles.add(condottiere);
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==pretre);

        //cas ou il n'y a ni assassin ni pretre

        roles.remove(0);
        //roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(pretre);
        roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(1);
        botAleatoire.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==condottiere);


    }
    @Test
    void ChoixRoleBotPresqueFiniThisEstDeuxieme(){
        /*
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        roles.add(condottiere);
        //mm chose quand le bot est deuxième
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        bot.setQuartierConstruit(quart);
        // cas ou il peut choisir l'assassin
        roles.remove(0);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        roles.add(condottiere);
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==assassin);



        // il n'y a pas l'assassin -> choisir pretre
        roles.remove(0);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        roles.add(condottiere);
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        //roles.add(new Architecte(botliste));
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==pretre);

        //cas ou il n'y a ni assassin ni pretre
        roles.remove(0);
        //roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(new Roi(botliste));
        //roles.add(pretre);
        roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        roles.add(condottiere);
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(1);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(4);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueursProcheFinir().isEmpty());
        assertTrue(bot.getJoueursProcheFinir().contains(bot));
        assertTrue(bot.getRole()==condottiere);*/
    }




}
