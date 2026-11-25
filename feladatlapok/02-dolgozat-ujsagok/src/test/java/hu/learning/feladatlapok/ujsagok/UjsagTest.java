package hu.learning.feladatlapok.ujsagok;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.reflect.Modifier;

/**
 * Az osztályhierarchia üzleti logikájának tesztjei (Feladat I. és III.).
 */
class UjsagTest {

    /** Az áfa az ár 5%-a. */
    @Test
    void afaTartalomAzAr5Szazaleka() {
        // Arrange
        Ujsag u = new Ujsag("Tech Magazin", "Bitlap", 1490);
        // Act
        double afa = u.afaTartalomSzamit();
        // Assert – lebegőpontos összehasonlításnál tűréshatárt adunk meg.
        assertEquals(74.5, afa, 0.0001);
    }

    /** Általános újságnál és hetilapnál a heti költség egy példány ára. */
    @Test
    void hetiKoltsegUjsagEsHetilapEseten() {
        assertEquals(990, new Ujsag("A", "B", 990).hetiArkalkulacio());
        assertEquals(690, new Hetilap("A", "B", 690, 4).hetiArkalkulacio());
    }

    /** Napilapnál a heti költség ár × heti megjelenés. */
    @Test
    void hetiKoltsegNapilapEseten() {
        Napilap n = new Napilap("Reggeli Hirmondo", "Fiktiv", 350, 6);
        assertEquals(2100, n.hetiArkalkulacio());
    }

    /** A hetiArkalkulacio() final – reflexióval ellenőrizzük a módosítót. */
    @Test
    void hetiArkalkulacioFinal() throws NoSuchMethodException {
        int modositok = Ujsag.class.getMethod("hetiArkalkulacio").getModifiers();
        assertTrue(Modifier.isFinal(modositok));
    }

    /** Üres megnevezés vagy kiadó nem megengedett. */
    @Test
    void uresSzovegesMezoKivetelt() {
        assertThrows(IllegalArgumentException.class, () -> new Ujsag("", "Kiado", 100));
        assertThrows(IllegalArgumentException.class, () -> new Ujsag("Nev", "   ", 100));
        Ujsag u = new Ujsag("Nev", "Kiado", 100);
        assertThrows(IllegalArgumentException.class, () -> u.setMegnevezes(null));
    }

    /** Negatív ár nem megengedett, a 0 igen. */
    @Test
    void negativArKivetelt() {
        Ujsag u = new Ujsag("Nev", "Kiado", 0);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> u.setAr(-1));
        assertTrue(e.getMessage().contains("negatív"));
    }

    /** Napilap csak 4..7 heti megjelenéssel hozható létre. */
    @ParameterizedTest
    @ValueSource(ints = {0, 3, 8})
    void napilapHetiMegjelenesHatarai(int ervenytelen) {
        assertThrows(IllegalArgumentException.class, () -> new Napilap("N", "K", 100, ervenytelen));
    }

    /** Hetilap megjelenési napja csak 1..7 lehet. */
    @ParameterizedTest
    @ValueSource(ints = {0, 8, -2})
    void hetilapNapjaHatarai(int ervenytelen) {
        assertThrows(IllegalArgumentException.class, () -> new Hetilap("H", "K", 100, ervenytelen));
    }
}
