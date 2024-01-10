package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiocheTest {
    @Test
    void PiocheTest() {
        Pioche pioche = new Pioche();
        int size = pioche.getPioche().size();
        assertEquals(pioche.piocherQuartier() instanceof Quartier, true );
        assertEquals(pioche.getPioche().size(), size -1);
    }
}