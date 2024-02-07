package Citadelle.teamU.moteurJeu.bots.malin;

import Citadelle.teamU.cartes.roles.*;
import Citadelle.teamU.moteurJeu.Tour;
import Citadelle.teamU.moteurJeu.bots.Bot;
import Citadelle.teamU.moteurJeu.bots.BotAleatoire;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurJeu.Pioche;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
class BotRichardTest {
    BotRichard bot;
    BotRichard bot2;
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
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
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
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
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
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==condottiere);


    }
    @Test
    void ChoixRoleBotPresqueFiniThisEstDeuxieme(){
        //cas particulier
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
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        //roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
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
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==assassin);

        roles.remove(0);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(condottiere);
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
        bot.setQuartierConstruit(quart);
        bot.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
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
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        botFocusRoi.setOrdreChoixRole(1);
        tour.setRolesTemp(roles);
        bot.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        verify(bot, atLeast(1)).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==bot);
        assertTrue(bot.getRole()==condottiere);


    }
    @Test
    void ChoixRoleBotPresqueFiniBotEstDeuxieme(){
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste));
        botFocusRoi.setQuartierConstruit(quart);
        botFocusRoi.setOrdreChoixRole(2);
        botAleatoire.setOrdreChoixRole(4);
        botConstruitChere.setOrdreChoixRole(3);
        bot.setOrdreChoixRole(1);
        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste, roles);
        Pretre pretre=new Pretre(botliste);
        Condottiere condottiere=new Condottiere(botliste);
        roles.add(assassin);
        roles.add(new Voleur(botliste, roles));
        //roles.add(new Magicien(botliste));
        //roles.add(new Roi(botliste));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste));
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
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==botFocusRoi);
        assertTrue(bot.getRole()==assassin);

    }
    @Test
    void ChoixRoleBotPresqueFiniBotEstTroisiemeEtThisEstPremierOuDeuxieme(){

        List<Bot> botliste2 = new ArrayList<>();
        Tour tour2= new Tour(botliste2);
        bot2=new BotRichard(pioche);
        botliste2.add(bot);
        botliste2.add(bot2);
        botliste2.add(botFocusRoi);
        botliste2.add(botConstruitChere);
        List<Quartier> quart = new ArrayList<>();
        quart.add(Quartier.OBSERVATOIRE);
        quart.add(Quartier.TEMPLE);
        quart.add(Quartier.CHATEAU);
        quart.add(Quartier.UNIVERSITE);
        quart.add(Quartier.MARCHE);
        quart.add(Quartier.TERRAIN_DE_BATAILLE);
        quart.add(Quartier.ECOLE_DE_MAGIE);
        bot.setRole(new Pretre(botliste2));
        bot2.setRole(new Marchand(botliste2));
        botFocusRoi.setQuartierConstruit(quart);
        botFocusRoi.setOrdreChoixRole(3);
        bot2.setOrdreChoixRole(2);
        botConstruitChere.setOrdreChoixRole(4);
        bot.setOrdreChoixRole(1);

        List<Role> roles = new ArrayList<>();
        Assassin assassin=new Assassin(botliste2, roles);
        Pretre pretre=new Pretre(botliste2);
        Condottiere condottiere=new Condottiere(botliste2);
        roles.add(assassin);
        roles.add(new Voleur(botliste2, roles));
        //roles.add(new Magicien(botliste2));
        //roles.add(new Roi(botliste2));
        roles.add(pretre);
        //roles.add(new Marchand(botliste));
        roles.add(new Architecte(botliste2));
        roles.add(condottiere);
        System.out.println(roles);
        System.out.println(bot.getQuartiersConstruits().size());
        System.out.println(bot.getOrdreChoixRole());
        tour2.setRolesTemp(roles);
        bot.setNbTour(10);
        bot2.setNbTour(10);
        System.out.println(tour.getNbTour());
        tour2.distributionRoles();
        System.out.println(roles);
        System.out.println("role choisi par bot richard "+bot.getRole());
        System.out.println("role choisi par bot richard2 "+bot2.getRole());
        verify(bot).joueurProcheFinir();
        assertFalse(bot.getJoueurProcheFinir()==null);
        assertTrue(bot.getJoueurProcheFinir()==botFocusRoi);
        assertTrue(bot.getRole()==condottiere);
        assertTrue(bot2.getRole()==assassin);


    }




}
