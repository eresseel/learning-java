package hu.learning.feladatlapok.bankszamla;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A {@link Bankszamla} egységtesztjei. TDD-ben ezek a tesztek születtek
 * meg ELŐSZÖR – mindegyik pirosan indult, majd a kód zöldre hozta.
 * Minden teszt Arrange (előkészítés) / Act (végrehajtás) / Assert (ellenőrzés)
 * szerkezetű.
 */
@DisplayName("Bankszámla")
class BankszamlaTest {

    /** Minden teszt előtt friss, üres számla (a tesztek függetlenek egymástól). */
    private Bankszamla szamla;

    /** Előkészítés: minden teszt előtt új számlát nyitunk. */
    @BeforeEach
    void setUp() {
        szamla = new Bankszamla("111-222", 0.01);
    }

    // ---------------------------------------------------------------
    // 1. lépés: számla létrehozása
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Új számla: a számlaszám és a kamat beállítódik, az egyenleg 0")
    void ujSzamlaAdatai() {
        // Arrange + Act: a setUp már létrehozta a számlát.
        // Assert
        assertEquals("111-222", szamla.getSzamlaszam());
        assertEquals(0, szamla.getEgyenleg());
        assertEquals(0.01, szamla.getNapiKamat(), 1e-9);
    }

    @Test
    @DisplayName("Üres vagy null számlaszámmal nem lehet számlát nyitni")
    void uresSzamlaszamHiba() {
        // Act + Assert: a konstruktornak kivételt kell dobnia.
        assertThrows(IllegalArgumentException.class, () -> new Bankszamla("", 0.01));
        assertThrows(IllegalArgumentException.class, () -> new Bankszamla("   ", 0.01));
        assertThrows(IllegalArgumentException.class, () -> new Bankszamla(null, 0.01));
    }

    @Test
    @DisplayName("Negatív napi kamattal nem lehet számlát nyitni")
    void negativKamatHiba() {
        assertThrows(IllegalArgumentException.class, () -> new Bankszamla("111-222", -0.5));
    }
}
