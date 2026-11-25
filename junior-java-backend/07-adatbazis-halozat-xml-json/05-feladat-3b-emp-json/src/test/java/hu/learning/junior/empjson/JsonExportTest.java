package hu.learning.junior.empjson;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * A JSON export tesztje adatbázis NÉLKÜL: kézzel összerakott listát
 * alakítunk JSON-ná, majd visszaolvassuk.
 */
class JsonExportTest {

    /** Rövid, ismert tartalmú tesztadat. */
    private final List<DolgozoAdat> dolgozok = List.of(
            new DolgozoAdat("KING", "PRESIDENT", new BigDecimal("5000.00")),
            new DolgozoAdat("SMITH", "CLERK", new BigDecimal("800.00")));

    @Test
    void jsonSzovegKulcsaiEsErtekei() {
        // Act
        String json = new JsonExport().jsonSzoveg(dolgozok);
        // Assert: a record komponensnevei lesznek a kulcsok
        assertTrue(json.contains("\"ename\": \"KING\""), json);
        assertTrue(json.contains("\"job\": \"CLERK\""), json);
        assertTrue(json.contains("\"sal\": 800.00"), json);
    }

    @Test
    void fajlbaIrasEsVisszaolvasas(@TempDir Path mappa) throws IOException {
        // Arrange
        Path fajl = mappa.resolve("emp.json");
        // Act
        new JsonExport().fajlba(dolgozok, fajl);
        DolgozoAdat[] vissza = new Gson().fromJson(Files.readString(fajl), DolgozoAdat[].class);
        // Assert: ugyanazt kapjuk vissza (a GSON recordot is tud olvasni)
        assertEquals(dolgozok, List.of(vissza));
    }
}
