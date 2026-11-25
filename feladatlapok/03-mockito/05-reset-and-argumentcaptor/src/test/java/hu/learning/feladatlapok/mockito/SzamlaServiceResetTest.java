package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * {@code reset(mock)}: a mock alapállapotba állítása – törli a beállított
 * viselkedéseket ÉS a rögzített hívásokat is.
 * <p>
 * Megjegyzés: jól szervezett teszteknél ritkán kell (minden teszt friss mockot kap),
 * itt egy tesztmetóduson belül mutatjuk be a hatását.
 */
class SzamlaServiceResetTest {

    /** A reset törli a stubot: utána újra az alapértelmezett null jön vissza. */
    @Test
    void resetTorliABeallitottViselkedest() {
        // Arrange
        SzamlaRepository repository = mock(SzamlaRepository.class);
        AuditNaplo naplo = mock(AuditNaplo.class);
        SzamlaService service = new SzamlaService(repository, naplo);
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 2_000));

        // Act 1: a stub működik.
        assertEquals(2_000, service.egyenlegLekerdezes("111"));

        // Act 2: alapállapot.
        reset(repository);

        // Assert: nincs stub → null, amit a service "nincs ilyen számla" hibává alakít.
        assertNull(repository.findBySzamlaszam("111"));
        assertThrows(IllegalArgumentException.class, () -> service.egyenlegLekerdezes("111"));
    }

    /** A reset a hívásnaplót is törli: a korábbi hívások már nem verifikálhatók. */
    @Test
    void resetTorliAHivasokat() {
        // Arrange
        SzamlaRepository repository = mock(SzamlaRepository.class);
        AuditNaplo naplo = mock(AuditNaplo.class);
        SzamlaService service = new SzamlaService(repository, naplo);
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 2_000));
        service.befizetes("111", 1_000);
        verify(naplo).ir("BEFIZETES 111 1000");

        // Act
        reset(naplo, repository);

        // Assert: a reset után a mockok "tiszták", mintha senki nem hívta volna őket.
        verifyNoInteractions(naplo, repository);
    }
}
