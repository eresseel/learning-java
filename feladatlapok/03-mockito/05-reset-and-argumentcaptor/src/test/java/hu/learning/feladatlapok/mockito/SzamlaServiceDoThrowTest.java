package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@code doThrow(...).when(mock).metodus(...)}: kivételdobás szimulálása.
 * <p>
 * Void metódusnál (pl. {@code save}) a {@code when(mock.save(..))} szintaxis le sem
 * fordul, mert void értéket nem lehet paraméterként átadni – ezért kell a do... forma.
 */
@ExtendWith(MockitoExtension.class)
class SzamlaServiceDoThrowTest {

    @Mock
    private SzamlaRepository repository;

    @Mock
    private AuditNaplo naplo;

    @InjectMocks
    private SzamlaService service;

    /** A repository mentési hibáját a service SzamlaMuveletException-be csomagolja. */
    @Test
    void mentesiHibaBecsomagolva() {
        // Arrange: betöltés rendben, de a mentés "adatbázishibát" dob.
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));
        doThrow(new AdatbazisHibaException("Kapcsolat megszakadt"))
                .when(repository).save(any(Szamla.class));

        // Act
        SzamlaMuveletException ex = assertThrows(SzamlaMuveletException.class,
                () -> service.befizetes("111", 500));

        // Assert: az eredeti ok megmaradt, a hibát naplózta, sikeres befizetést nem.
        assertInstanceOf(AdatbazisHibaException.class, ex.getCause());
        assertEquals("Kapcsolat megszakadt", ex.getCause().getMessage());
        verify(naplo).ir("HIBA BEFIZETES 111: Kapcsolat megszakadt");
        verify(naplo, never()).ir("BEFIZETES 111 500");
    }

    /** Nem void metódusra is lehet doThrow: itt a betöltés hibázik. */
    @Test
    void betoltesiHibaTovabbmegy() {
        // Arrange
        doThrow(new AdatbazisHibaException("Időtúllépés"))
                .when(repository).findBySzamlaszam(anyString());

        // Act + Assert: ezt a service nem kezeli, változatlanul a hívóhoz jut.
        assertThrows(AdatbazisHibaException.class, () -> service.egyenlegLekerdezes("111"));
        verify(repository, never()).save(any());
    }

    /** A naplózás hibája is szimulálható: ekkor a kivétel a hívóig jut. */
    @Test
    void naploHibaSzimulalasa() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));
        doThrow(new IllegalStateException("Tele a lemez")).when(naplo).ir(anyString());

        // Act + Assert
        assertThrows(IllegalStateException.class, () -> service.kifizetes("111", 100));
        // A mentés a naplózás előtt megtörtént.
        verify(repository).save(new Szamla("111", 900));
    }
}
