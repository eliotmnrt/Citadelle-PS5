package Citadelle.teamU.cartes.roles;

import static org.junit.jupiter.api.Assertions.*;
import Citadelle.teamU.moteurjeu.Pioche;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.malin.BotConstruitVite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

class AssassinTest {
    private BotAleatoire botAleatoire, botAleatoire2;
    private BotConstruitVite botConstruitVite;
    private BotConstruitChere botConstruitChere;
    private ArrayList<Bot> botliste;
    private Roi roi;
    ArrayList<Role> roles;
    Assassin mocAssassin; // assassin
    @BeforeEach
    void SetUp(){
        Pioche pioche = new Pioche();
        botAleatoire = Mockito.spy(new BotAleatoire(pioche));
        botAleatoire2 = Mockito.spy(new BotAleatoire(pioche));
        botConstruitVite = Mockito.spy(new BotConstruitVite(pioche));
        botConstruitChere = Mockito.spy(new BotConstruitChere(pioche));
        botliste = new ArrayList<>();
        botliste.add(botAleatoire);
        botliste.add(botAleatoire2);
        botliste.add(botConstruitVite);
        botliste.add(botConstruitChere);

        roles = new ArrayList<>();
        mocAssassin=Mockito.spy(new Assassin(botliste, roles));
        roi= new Roi(botliste);
        roles.add(mocAssassin);
        roles.add(new Voleur(botliste, roles));
        roles.add(new Magicien(botliste));
        roles.add(roi);
        roles.add(new Marchand(botliste));
        roles.add(new Pretre(botliste));
        roles.add(new Architecte(botliste));
        roles.add(new Condottiere(botliste));



    }
    @Test
    void testAleatoireTue(){
        botAleatoire.setRole(mocAssassin); //donne le role de l'asssassin
        botAleatoire2.setRole(mocAssassin.getRoles().get(2)); //donne le role du Magicien
        botConstruitChere.setRole(mocAssassin.getRoles().get(3)); //donne le role du Roi
        botConstruitVite.setRole(mocAssassin.getRoles().get(1)); //donne le role du voleur

        doReturn(2).when(botAleatoire).randInt(7);
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeAssassin(mocAssassin);
        verify(mocAssassin).tuer(roi);
        assertEquals(true, botConstruitChere.estMort());

    }

    @RepeatedTest(10)
    void testViteTue(){
        botConstruitVite.setRole(mocAssassin); //donne le role de l'asssassin
        botAleatoire2.setRole(mocAssassin.getRoles().get(2)); //donne le role du Magicien
        botConstruitChere.setRole(mocAssassin.getRoles().get(3)); //donne le role du Roi

        List<Role> rolesRestant = new ArrayList<>();
        rolesRestant.add(mocAssassin.getRoles().get(2));
        rolesRestant.add(mocAssassin.getRoles().get(3));
        botConstruitVite.setRolesRestants(rolesRestant);

        doReturn(1).when(botConstruitVite).randInt(anyInt());
        botConstruitVite.faireActionSpecialRole();
        verify(botConstruitVite).actionSpecialeAssassin(mocAssassin);
        //verify(mocAssassin).tuer(roi);
        assertTrue(botConstruitChere.estMort());
    }

    @RepeatedTest(10)
    void testChereTue(){
        botConstruitChere.setRole(mocAssassin); //donne le role de l'asssassin
        botAleatoire2.setRole(mocAssassin.getRoles().get(2)); //donne le role du Magicien
        botConstruitVite.setRole(mocAssassin.getRoles().get(3)); //donne le role du Roi

        List<Role> rolesRestant = new ArrayList<>();
        rolesRestant.add(mocAssassin.getRoles().get(2));
        rolesRestant.add(mocAssassin.getRoles().get(3));
        botConstruitChere.setRolesRestants(rolesRestant);

        doReturn(1).when(botConstruitChere).randInt(2);
        botConstruitChere.faireActionSpecialRole();
        verify(botConstruitChere).actionSpecialeAssassin(mocAssassin);
        //verify(mocAssassin).tuer(roi);
        assertTrue(botConstruitVite.estMort());

    }
  
}