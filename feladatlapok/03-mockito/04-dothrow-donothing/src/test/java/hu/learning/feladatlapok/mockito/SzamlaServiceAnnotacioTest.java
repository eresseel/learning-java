package hu.learning.feladatlapok.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Ugyanaz a technika annotációkkal: a {@link MockitoExtension} hozza létre
 * a {@code @Mock} mezőket, és a {@code @InjectMocks} mezőbe konstruktoron
 * keresztül beinjektálja.
 */
@ExtendWith(MockitoExtension.class)
class SzamlaServiceAnnotacioTest {

    /** Mockito által létrehozott hamis repository. */
    @Mock
    private SzamlaRepository repository;

    /** Mockito által létrehozott hamis auditnapló. */
    @Mock
    private AuditNaplo naplo;

    /** A tesztelt szolgáltatás, a fenti mockot kapja meg. */
    @InjectMocks
    private SzamlaService service;

    /** Befizetés után az új egyenleg = régi + összeg. */
    @Test
    void befizetesNoveliAzEgyenleget() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        long uj = service.befizetes("111", 500);

        // Assert
        assertEquals(1_500, uj);
    }

    /** Kifizetés fedezet esetén csökkenti az egyenleget. */
    @Test
    void kifizetesCsokkentiAzEgyenleget() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 1_000));

        // Act
        long uj = service.kifizetes("111", 300);

        // Assert
        assertEquals(700, uj);
    }

    /** Fedezet hiányában FedezethianyException-t kapunk. */
    @Test
    void kifizetesFedezetNelkulKivetelt() {
        // Arrange
        when(repository.findBySzamlaszam("111")).thenReturn(new Szamla("111", 100));

        // Act + Assert
        FedezethianyException ex = assertThrows(FedezethianyException.class,
                () -> service.kifizetes("111", 500));
        assertEquals("Nincs elegendő fedezet a(z) 111 számlán!", ex.getMessage());
    }

    /** Nem pozitív összeg esetén a mockhoz sem kell nyúlni: azonnal hiba. */
    @Test
    void nemPozitivBefizetesHibas() {
        // Act + Assert (stub sem kell, a validáció előbb fut)
        assertThrows(IllegalArgumentException.class, () -> service.befizetes("111", 0));
    }
}
