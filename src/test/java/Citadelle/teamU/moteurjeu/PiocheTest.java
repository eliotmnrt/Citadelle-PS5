package fr.cotedazur.univ.polytech.startingpoint.moteurjeu;

import fr.cotedazur.univ.polytech.startingpoint.cartes.Quartier;
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