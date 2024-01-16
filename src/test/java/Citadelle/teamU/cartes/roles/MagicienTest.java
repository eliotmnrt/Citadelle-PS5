package Citadelle.teamU.cartes.roles;
import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.bots.Bot;
import Citadelle.teamU.moteurjeu.bots.BotAleatoire;
import Citadelle.teamU.moteurjeu.bots.BotConstruitChere;
import Citadelle.teamU.moteurjeu.bots.BotConstruitVite;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

import Citadelle.teamU.moteurjeu.*;
import org.junit.jupiter.api.BeforeEach;

import org.mockito.Mockito;

import java.util.ArrayList;


class MagicienTest {

    private BotAleatoire botAleatoire, botAleatoire2;
    private BotConstruitVite botConstruitVite;
    private BotConstruitChere botConstruitChere;
    private ArrayList<Bot> botliste;
    Magicien track;
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
        track = Mockito.spy(new Magicien(botliste));
    }

    @Test
    void testAleatoire(){ //un peu bancal comme test
        botAleatoire.setRole(track);       //botAleatoire est le magicien
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(new Marchand(botliste));
        botConstruitVite.setRole(new Pretre(botliste));
        ArrayList<Quartier> mainAvant = new ArrayList<>(botAleatoire.getQuartierMain());
        botAleatoire.faireActionSpecialRole();
        verify(botAleatoire).actionSpecialeMagicien(track);
        assertNotEquals(mainAvant, botAleatoire.getQuartierMain());
    }

    @Test
    void  botPasConChangeAvecPioche() {
        botAleatoire.setRole(new Marchand(botliste));
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(track);               //botConstruitChere est le magicien
        botConstruitVite.setRole(new Pretre(botliste));
        //on retire une carte a chaque adversaire pour le forcer à échanger avec la pioche
        botAleatoire.getQuartierMain().remove(0);
        botAleatoire2.getQuartierMain().remove(0);
        botConstruitVite.getQuartierMain().remove(0);

        botConstruitChere.faireActionSpecialRole();
        verify(botConstruitChere).actionSpecialeMagicien(track);
        verify(track).changeAvecPioche(botConstruitChere, botConstruitChere.getQuartierMain());
    }

    @Test
    void botPasConChangeAvecBot() {
        botAleatoire.setRole(new Marchand(botliste));
        botAleatoire2.setRole(new Roi(botliste));
        botConstruitChere.setRole(track);               //botConstruitChere est le magicien
        botConstruitVite.setRole(new Pretre(botliste));
        //on ajoute une carte a un adversaire pour le forcer à échanger avec lui
        botAleatoire.getQuartierMain().add(botAleatoire.getPioche().piocherQuartier());

        botConstruitChere.faireActionSpecialRole();
        verify(botConstruitChere).actionSpecialeMagicien(track);
        verify(track).changeAvecBot(botConstruitChere, botAleatoire);
    }
}