package Citadelle.teamU.moteurjeu;

import Citadelle.teamU.cartes.Quartier;
import Citadelle.teamU.moteurjeu.Pioche;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PiocheTest {
    @Test
    void PiocheTest() {
        Pioche pioche = new Pioche();
        int size = Pioche.getPioche().size();
        assertEquals(Pioche.piocherQuartier() instanceof Quartier, true );
        assertEquals(Pioche.getPioche().size(), size -1);
    }
}