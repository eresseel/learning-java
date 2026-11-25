package hu.learning.junior.empjson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Dolgozólista JSON-ba alakítása a GSON külső könyvtárral.
 */
public class JsonExport {

    /**
     * A GSON példány: a {@code setPrettyPrinting()} tagolt, olvasható JSON-t
     * készít (éles adatcserénél a tömörebb, tagolás nélküli változat a szokásos).
     */
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * A listát JSON tömb szöveggé alakítja.
     *
     * @param dolgozok a dolgozók
     * @return JSON szöveg, pl. {@code [ { "ename": "KING", ... } ]}
     */
    public String jsonSzoveg(List<DolgozoAdat> dolgozok) {
        return gson.toJson(dolgozok);
    }

    /**
     * A listát JSON fájlba írja (UTF-8).
     *
     * @param dolgozok a dolgozók
     * @param fajl     a kimeneti fájl
     * @throws IOException ha a fájl nem írható
     */
    public void fajlba(List<DolgozoAdat> dolgozok, Path fajl) throws IOException {
        // A GSON közvetlenül Writer-be is tud írni; a try-with-resources lezárja a fájlt.
        try (Writer iro = Files.newBufferedWriter(fajl, StandardCharsets.UTF_8)) {
            gson.toJson(dolgozok, iro);
        }
    }
}
