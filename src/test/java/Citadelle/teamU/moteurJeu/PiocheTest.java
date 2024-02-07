package Citadelle.teamU.moteurJeu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiocheTest {
    @Test
    void PiocheTest() {
        Pioche pioche = new Pioche();
        int size = pioche.getPioche().size();
        assertNotNull(pioche.piocherQuartier());
        assertEquals(pioche.getPioche().size(), size -1);
    }
}