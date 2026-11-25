package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link ArgumentCaptor}: "elkapja" a mocknak átadott paramétert, hogy utána
 * részletesen megvizsgálhassuk (pl. a service által ÉPÍTETT, mentett objektumot).
 */
@ExtendWith(MockitoExtension.class)
class SzamlaServiceCaptorTest {

    @Mock
    private SzamlaRepository repository;

    @Mock
    private AuditNaplo naplo;

    @InjectMocks
    private SzamlaService service;

    /** Annotációval létrehozott captor a mentett Szamla elkapásához. */
    @Captor
    private ArgumentCaptor<Szamla> szamlaCaptor;

    /** A mentett számla egyenlegét a captorral ellenőrizzük. */
    @Test
    void mentettSzamlaEgyenlegeHelyes() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        service.kifizetes("111", 250);

        // Assert: verify közben a captor elkapja a save() paraméterét.
        verify(repository).save(szamlaCaptor.capture());
        Szamla mentett = szamlaCaptor.getValue();
        assertEquals("111", mentett.szamlaszam());
        assertEquals(750, mentett.egyenleg());
    }

    /** Több hívásnál a getAllValues() sorrendben adja vissza az összes elkapott értéket. */
    @Test
    void naploUzenetekSorrendben() {
        // Arrange: kézzel létrehozott captor (az annotáció alternatívája).
        ArgumentCaptor<String> uzenetCaptor = ArgumentCaptor.forClass(String.class);
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        service.befizetes("111", 100);
        service.kifizetes("111", 40);

        // Assert
        verify(naplo, times(2)).ir(uzenetCaptor.capture());
        List<String> uzenetek = uzenetCaptor.getAllValues();
        assertEquals(List.of("BEFIZETES 111 100", "KIFIZETES 111 40"), uzenetek);
    }
}
