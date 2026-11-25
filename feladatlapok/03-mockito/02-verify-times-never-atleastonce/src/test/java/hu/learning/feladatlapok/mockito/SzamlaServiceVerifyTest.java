package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Hívások ellenőrzése (4. lépés: verifikálás): {@code verify}, {@code times(n)},
 * {@code never()}, {@code atLeastOnce()} és argumentum-illesztők ({@code eq}, {@code any}).
 */
@ExtendWith(MockitoExtension.class)
class SzamlaServiceVerifyTest {

    @Mock
    private SzamlaRepository repository;

    @Mock
    private AuditNaplo naplo;

    @InjectMocks
    private SzamlaService service;

    /** verify(mock).metodus(...) – pontosan ezzel a paraméterrel, egyszer hívták-e. */
    @Test
    void befizetesElmentiAzUjAllapototEsNaploz() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        service.befizetes("111", 500);

        // Assert: a verify alapértelmezésben times(1)-et jelent.
        verify(repository).save(new Szamla("111", 1_500));
        verify(naplo).ir("BEFIZETES 111 500");
    }

    /** times(n): pontosan n hívás történt. */
    @Test
    void ketBefizetesKetNaplobejegyzes() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        service.befizetes("111", 100);
        service.befizetes("111", 200);

        // Assert: bármilyen szöveggel (anyString), de pontosan kétszer naplózott.
        verify(naplo, times(2)).ir(anyString());
        verify(repository, times(2)).save(any(Szamla.class));
    }

    /** never(): fedezethiány esetén a mentés egyszer sem hívódhat meg. */
    @Test
    void fedezethianyEsetenNincsMentes() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 100));

        // Act
        assertThrows(FedezethianyException.class, () -> service.kifizetes("111", 500));

        // Assert: save soha, az elutasítást viszont naplózta.
        verify(repository, never()).save(any());
        verify(naplo).ir(startsWith("ELUTASITVA"));
    }

    /** atLeastOnce(): legalább egyszer betöltötte a számlát. */
    @Test
    void lekerdezesLegalabbEgyszerBetolti() {
        // Arrange
        when(repository.findBySzamlaszam(anyString())).thenReturn(new Szamla("111", 1_000));

        // Act
        service.egyenlegLekerdezes("111");
        service.kifizetes("111", 10);

        // Assert: eq() illesztő – ha egy paraméternél illesztőt használunk,
        // akkor az összes paraméternél azt kell (itt csak egy paraméter van).
        verify(repository, atLeastOnce()).findBySzamlaszam(eq("111"));
    }

    /** Érvénytelen összegnél a service a repositoryhoz és a naplóhoz sem nyúl. */
    @Test
    void ervenytelenOsszegNelNincsInterakcio() {
        // Act
        assertThrows(IllegalArgumentException.class, () -> service.kifizetes("111", -5));

        // Assert
        verify(repository, never()).findBySzamlaszam(anyString());
        verifyNoMoreInteractions(repository, naplo);
    }
}
