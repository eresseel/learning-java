package hu.learning.junior.autokereskedes;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Az XML ↔ objektum leképezés tesztjei.
 */
class AutoXmlTest {

    /**
     * @return a tesztkönyvtárban lévő minta XML útvonala
     */
    private static Path minta() throws URISyntaxException {
        return Path.of(AutoXmlTest.class.getResource("/autokereskedes.xml").toURI());
    }

    @Test
    void beolvasas() throws Exception {
        // Act
        Autokereskedes k = AutoXml.beolvas(minta());
        // Assert
        assertEquals("Példa Autóház Kft.", k.nev());
        assertEquals(4, k.autok().size());
        assertEquals(new Auto(1, "Toyota", "Corolla", 2019, 6490000, "ezüst"), k.autok().getFirst());
    }

    @Test
    void hozzaadasEsKiirasUtanVisszaolvashato(@TempDir Path mappa) throws Exception {
        // Arrange: beolvasás + egy új autó
        Autokereskedes k = AutoXml.beolvas(minta());
        List<Auto> autok = new ArrayList<>(k.autok());
        autok.add(new Auto(5, "Dacia", "Duster", 2022, 6990000, "zöld"));
        Path ki = mappa.resolve("uj.xml");
        // Act
        AutoXml.kiir(new Autokereskedes(k.nev(), autok), ki);
        Autokereskedes vissza = AutoXml.beolvas(ki);
        // Assert: oda-vissza alakítás után ugyanaz (ékezetekkel együtt)
        assertEquals(new Autokereskedes(k.nev(), autok), vissza);
        assertTrue(Files.readString(ki).contains("<szin>zöld</szin>"));
    }

    @Test
    void hibasXml(@TempDir Path mappa) throws IOException {
        Path rossz = Files.writeString(mappa.resolve("rossz.xml"), "<autokereskedes><auto>");
        assertThrows(IOException.class, () -> AutoXml.beolvas(rossz));
    }

    @Test
    void doctypeTiltva(@TempDir Path mappa) throws IOException {
        // XXE védelem: DOCTYPE-ot tartalmazó XML-t nem fogadunk el
        Path xxe = Files.writeString(mappa.resolve("xxe.xml"),
                "<?xml version=\"1.0\"?><!DOCTYPE a [<!ENTITY x SYSTEM \"file:///etc/passwd\">]>"
                        + "<autokereskedes nev=\"&x;\"/>");
        assertThrows(IOException.class, () -> AutoXml.beolvas(xxe));
    }
}
