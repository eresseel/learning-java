package hu.learning.feladatlapok.bankszamla;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

    // ---------------------------------------------------------------
    // 2. lépés: befizetés
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Befizetés növeli az egyenleget")
    void befizetesNoveliAzEgyenleget() {
        // Act
        szamla.befizetes(1000);
        szamla.befizetes(500);
        // Assert
        assertEquals(1500, szamla.getEgyenleg());
    }

    @ParameterizedTest(name = "Befizetés {0} Ft-tal hibát dob")
    @ValueSource(longs = {0, -1, -1000})
    @DisplayName("Nem pozitív összeg befizetése hiba, és az egyenleg nem változik")
    void nemPozitivBefizetesHiba(long osszeg) {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> szamla.befizetes(osszeg));
        assertEquals(0, szamla.getEgyenleg());
    }

    // ---------------------------------------------------------------
    // 3. lépés: kifizetés
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Kifizetés csökkenti az egyenleget")
    void kifizetesCsokkentiAzEgyenleget() {
        // Arrange
        szamla.befizetes(1000);
        // Act
        szamla.kifizetes(300);
        // Assert
        assertEquals(700, szamla.getEgyenleg());
    }

    @Test
    @DisplayName("A teljes egyenleg felvehető (határeset)")
    void teljesEgyenlegFelveheto() {
        szamla.befizetes(1000);
        szamla.kifizetes(1000);
        assertEquals(0, szamla.getEgyenleg());
    }

    @Test
    @DisplayName("Fedezet nélküli kifizetés NincsFedezetException-t dob, az egyenleg nem változik")
    void fedezetNelkuliKifizetes() {
        // Arrange
        szamla.befizetes(1000);
        // Act + Assert
        NincsFedezetException hiba =
                assertThrows(NincsFedezetException.class, () -> szamla.kifizetes(1001));
        assertTrue(hiba.getMessage().contains("fedezet"));
        assertEquals(1000, szamla.getEgyenleg());
    }

    @ParameterizedTest(name = "Kifizetés {0} Ft-tal hibát dob")
    @ValueSource(longs = {0, -5})
    @DisplayName("Nem pozitív összeg kifizetése hiba")
    void nemPozitivKifizetesHiba(long osszeg) {
        assertThrows(IllegalArgumentException.class, () -> szamla.kifizetes(osszeg));
    }

    // ---------------------------------------------------------------
    // 4. lépés: átutalás
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Átutalás: a forrás csökken, a cél ugyanannyival nő")
    void atutalas() {
        // Arrange
        Bankszamla cel = new Bankszamla("333-444", 0.01);
        szamla.befizetes(5000);
        // Act
        szamla.atutalas(cel, 2000);
        // Assert
        assertEquals(3000, szamla.getEgyenleg());
        assertEquals(2000, cel.getEgyenleg());
    }

    @Test
    @DisplayName("Fedezethiánynál egyik számla sem változik")
    void atutalasFedezetHianyban() {
        // Arrange
        Bankszamla cel = new Bankszamla("333-444", 0.01);
        szamla.befizetes(100);
        // Act + Assert
        assertThrows(NincsFedezetException.class, () -> szamla.atutalas(cel, 101));
        assertEquals(100, szamla.getEgyenleg());
        assertEquals(0, cel.getEgyenleg());
    }

    @Test
    @DisplayName("Null célra vagy saját számlára nem lehet utalni")
    void ervenytelenCel() {
        szamla.befizetes(100);
        assertThrows(IllegalArgumentException.class, () -> szamla.atutalas(null, 10));
        assertThrows(IllegalArgumentException.class, () -> szamla.atutalas(szamla, 10));
        assertThrows(IllegalArgumentException.class,
                () -> szamla.atutalas(new Bankszamla("111-222", 0), 10));
    }
}
