package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A {@link SzamlaService} tesztje "kézzel" létrehozott mockkal ({@code mock(...)}).
 * <p>
 * A repository mock teljesen hamis objektum: csak az a viselkedése van,
 * amit {@code when(...).thenReturn(...)}-nel beállítunk.
 */
class SzamlaServiceMockTest {

    /** A hamis tárolási réteg. */
    private SzamlaRepository repository;
    /** A hamis auditnapló (ebben a tesztosztályban nem vizsgáljuk). */
    private AuditNaplo naplo;
    /** A tesztelt osztály, a mockot kapja függőségként. */
    private SzamlaService service;

    /** Minden teszt előtt friss mockot és szolgáltatást készítünk. */
    @BeforeEach
    void setUp() {
        // 1. lépés: mock objektum létrehozása.
        repository = mock(SzamlaRepository.class);
        naplo = mock(AuditNaplo.class);
        service = new SzamlaService(repository, naplo);
    }

    /** Beállítatlan mock metódus alapértelmezett értékeket ad vissza. */
    @Test
    void beallitatlanMockAlapertelmezettErtekeketAd() {
        // Act + Assert: semmit nem állítottunk be, ezért null / false / 0 jön vissza.
        assertNull(repository.findBySzamlaszam("111"));
        assertFalse(repository.letezik("111"));
        assertEquals(0, repository.szamlakSzama());
    }

    /** A when().thenReturn() által megadott számlát kapja a szolgáltatás. */
    @Test
    void egyenlegLekerdezesAMockAltalVisszaadottSzamlabol() {
        // Arrange – 2. lépés: viselkedés beállítása.
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 5_000));

        // Act – 3. lépés: a tesztelt metódus hívása.
        long egyenleg = service.egyenlegLekerdezes("111");

        // Assert
        assertEquals(5_000, egyenleg);
    }

    /** Csak a pontosan beállított paraméterre működik a stub, másra null jön. */
    @Test
    void masikSzamlaszamraNincsBeallitottViselkedes() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 5_000));

        // Act + Assert: a "222"-re a mock null-t ad, a service ezt hibává alakítja.
        assertThrows(IllegalArgumentException.class, () -> service.egyenlegLekerdezes("222"));
    }

    /** A thenReturn több értéket is kaphat: egymás utáni hívásokra sorban adja őket. */
    @Test
    void egymasUtaniHivasokraKulonbozoErtekek() {
        // Arrange
        when(repository.szamlakSzama()).thenReturn(1, 2, 3);

        // Act + Assert
        assertEquals(1, repository.szamlakSzama());
        assertEquals(2, repository.szamlakSzama());
        assertEquals(3, repository.szamlakSzama());
        // Az utolsó érték "ragad": a további hívások is 3-at adnak.
        assertEquals(3, repository.szamlakSzama());
    }
}
