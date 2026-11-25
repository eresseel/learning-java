package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * {@code doNothing().when(spy).metodus(...)}: egy valódi objektum void
 * metódusának mellékhatását kapcsoljuk ki.
 * <p>
 * Mocknál a void metódusok amúgy is "semmit nem csinálnak", ezért a doNothing
 * igazán spy-nál hasznos, ahol egyébként a valódi kód futna.
 */
class KonzolNaploDoNothingTest {

    /** Valódi konzolnapló spy-ba csomagolva. */
    private KonzolNaplo naplo;
    /** A memóriás valódi repository. */
    private MemoriaSzamlaRepository repository;
    /** A tesztelt szolgáltatás valódi függőségekkel. */
    private SzamlaService service;

    @BeforeEach
    void setUp() {
        naplo = spy(new KonzolNaplo());
        repository = new MemoriaSzamlaRepository();
        repository.save(new Szamla("111", 1_000));
        service = new SzamlaService(repository, naplo);
    }

    /** doNothing nélkül a valódi ir() fut: kiír és számol. */
    @Test
    void doNothingNelkulValodiLogikaFut() {
        // Act
        service.befizetes("111", 100);

        // Assert: a valódi metódus lefutott, a számláló nőtt.
        assertEquals(1, naplo.getKiirtBejegyzesek());
    }

    /** doNothing után az ir() nem ír ki semmit, de a hívás verifikálható. */
    @Test
    void doNothingKikapcsoljaAMellekhatast() {
        // Arrange: a konzolra írás kikapcsolása.
        doNothing().when(naplo).ir(anyString());

        // Act
        service.befizetes("111", 100);
        service.kifizetes("111", 50);

        // Assert: a valódi kód nem futott (számláló 0), a hívás viszont megtörtént.
        assertEquals(0, naplo.getKiirtBejegyzesek());
        verify(naplo, times(2)).ir(anyString());
        assertEquals(1_050, repository.findBySzamlaszam("111").egyenleg());
    }
}
