package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Spy használata: valódi objektum, amelynek csak egyes metódusait írjuk felül.
 * <p>
 * Spy esetén a {@code doReturn(...).when(spy).metodus()} szintaxist használjuk,
 * mert a {@code when(spy.metodus())} a stubolás közben MEGHÍVNÁ a valódi metódust.
 */
class KamatServiceSpyTest {

    /** Valódi kalkulátor, spy-ba csomagolva. */
    private KamatKalkulator kalkulator;
    /** Valódi memóriás repository, spy-ba csomagolva. */
    private MemoriaSzamlaRepository repository;
    /** A tesztelt szolgáltatás. */
    private KamatService service;

    /** Spy-ok létrehozása valódi példányokból: spy(new ...). */
    @BeforeEach
    void setUp() {
        kalkulator = spy(new KamatKalkulator());
        repository = spy(new MemoriaSzamlaRepository());
        service = new KamatService(repository, kalkulator);
        // A valódi repositoryba valódi adatot teszünk – a spy ezt átengedi.
        repository.save(new Szamla("111", 100_000));
    }

    /** A spy beállítatlan metódusai a valódi logikát futtatják. */
    @Test
    void spyValodiMetodusaFut() {
        // Act: napiKamat nincs felülírva → valódi számolás.
        long kamat = kalkulator.napiKamat(100_000, 0.0365);

        // Assert
        assertEquals(10, kamat);
        // A felül nem írt aktualisKamatlab() is valódi → kivételt dob.
        assertThrows(IllegalStateException.class, () -> kalkulator.aktualisKamatlab());
    }

    /** doReturn().when(spy): a valódi metódus NEM fut le, csak a megadott értéket adja. */
    @Test
    void kamatJovairasFelulirtKamatlabbal() {
        // Arrange: csak a "külső" kamatláb-lekérdezést írjuk felül.
        doReturn(0.0365).when(kalkulator).aktualisKamatlab();

        // Act
        long kamat = service.kamatJovairas("111");

        // Assert: a napiKamat valódi számolása futott (100 000 * 3,65% / 365 = 10).
        assertEquals(10, kamat);
        assertEquals(100_010, repository.findBySzamlaszam("111").egyenleg());
        // A spy hívásai is ellenőrizhetők verify-jal.
        verify(kalkulator).napiKamat(100_000, 0.0365);
    }

    /** Miért nem when(spy.metodus())? Mert a valódi metódus a stubolás alatt lefut! */
    @Test
    void whenSzintaxisSpyonMeghivjaAValodiMetodust() {
        // Act + Assert: már a when() argumentumának kiértékelése kivételt dob,
        // mert spy.aktualisKamatlab() a valódi kódot hívja.
        assertThrows(IllegalStateException.class,
                () -> when(kalkulator.aktualisKamatlab()).thenReturn(0.05));
    }

    /** A spy-olt valódi repository tényleg tárol, közben a hívások verifikálhatók. */
    @Test
    void spyRepositoryValodiTarolasEsVerify() {
        // Arrange
        doReturn(0.0730).when(kalkulator).aktualisKamatlab();

        // Act
        service.kamatJovairas("111");

        // Assert: valódi állapot + interakció ellenőrzés egyszerre.
        assertTrue(repository.letezik("111"));
        assertEquals(1, repository.szamlakSzama());
        verify(repository).save(new Szamla("111", 100_020));
    }
}
