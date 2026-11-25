import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * CSV fájlba mentést végző osztály. Csak a CsvMentheto interfészt ismeri,
 * így bármilyen (akár később megírt) osztály objektumait el tudja menteni,
 * anélkül, hogy ezt az osztályt módosítani kellene.
 */
public class CsvMento {

    /**
     * Elmenti az elemeket a megadott fájlba: első sor a fejléc, utána soronként egy elem.
     * <p>
     * A {@code List<? extends CsvMentheto>} paramétertípus azt jelenti: bármilyen lista,
     * amelynek elemei a CsvMentheto-t implementálják (List&lt;Kapcsolat&gt;, List&lt;Szolgaltatas&gt;...).
     *
     * @param fajlnev a cél fájl neve
     * @param elemek  a mentendő elemek
     * @return a kiírt adatsorok száma
     * @throws IOException ha a fájl nem írható (ellenőrzött kivétel)
     */
    public static int ment(String fajlnev, List<? extends CsvMentheto> elemek) throws IOException {
        // A try-with-resources a blokk végén automatikusan lezárja a fájlt (adatvesztés elkerülése).
        try (PrintWriter iro = new PrintWriter(Files.newBufferedWriter(Path.of(fajlnev), StandardCharsets.UTF_8))) {
            // 1. Fejléc: az első elemtől kérjük el (üres listánál nincs mit írni).
            if (!elemek.isEmpty()) {
                iro.println(elemek.get(0).csvFejlec());
            }
            // 2. Adatsorok: minden elem maga tudja, hogyan néz ki CSV-ben (polimorfizmus).
            for (CsvMentheto elem : elemek) {
                iro.println(elem.csvSor());
            }
        }
        return elemek.size();
    }
}
